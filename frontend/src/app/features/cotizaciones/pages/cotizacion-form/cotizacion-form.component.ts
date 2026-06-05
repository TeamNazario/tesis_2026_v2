import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { catchError, debounceTime, distinctUntilChanged, finalize, forkJoin, map, of, switchMap } from 'rxjs';
import { UsuarioResponse } from '../../../../core/auth/models/auth.models';
import { AuthService } from '../../../../core/auth/services/auth.service';
import { PermissionService } from '../../../../core/auth/services/permission.service';
import { CatalogoItem, ClienteV1 } from '../../../../core/models/v1.models';
import { CatalogoV1Service } from '../../../../core/services/catalogo-v1.service';
import { ClienteV1Service } from '../../../../core/services/cliente-v1.service';
import { DomainApiService } from '../../../../core/services/domain-api.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { ProductService } from '../../../../core/services/product.service';
import { ProductResponse } from '../../../../core/models/product.models';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { MaterialModule } from '../../../../shared/material/material.module';
import {
  CotizacionCalcularItemResponse,
  CotizacionCalcularResumenResponse,
  CotizacionApiError,
  CotizacionCreateRequest,
} from '../../models/cotizacion.model';
import { CotizacionService } from '../../services/cotizacion.service';

@Component({
  selector: 'app-cotizacion-form',
  imports: [CommonModule, ReactiveFormsModule, RouterLink, MaterialModule, PageHeaderComponent],
  templateUrl: './cotizacion-form.component.html',
  styleUrl: './cotizacion-form.component.scss',
})
export class CotizacionFormComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly service = inject(CotizacionService);
  private readonly clienteService = inject(ClienteV1Service);
  private readonly productService = inject(ProductService);
  private readonly catalogoService = inject(CatalogoV1Service);
  private readonly domainApi = inject(DomainApiService);
  private readonly notifications = inject(NotificationService);
  private readonly auth = inject(AuthService);
  readonly permissions = inject(PermissionService);

  readonly clientes = signal<ClienteV1[]>([]);
  private readonly allClientes = signal<ClienteV1[]>([]);
  readonly productos = signal<ProductResponse[]>([]);
  readonly vendedores = signal<UsuarioResponse[]>([]);
  readonly estados = signal<CatalogoItem[]>([]);
  readonly items = signal<CotizacionCalcularItemResponse[]>([]);
  readonly resumen = signal<CotizacionCalcularResumenResponse | null>(null);
  readonly isLoading = signal(false);
  readonly isCalculating = signal(false);
  readonly isSaving = signal(false);
  readonly isSearchingClientes = signal(false);
  readonly editingProductId = signal<number | null>(null);
  readonly selectedClientId = signal(0);
  readonly selectedProductId = signal(0);

  readonly itemColumns = ['item', 'producto', 'unidad', 'cantidad', 'precio', 'importe', 'acciones'];

  readonly clienteSearch = this.fb.nonNullable.control('');

  readonly selectedCliente = computed(() => {
    const id = this.selectedClientId() || this.form.controls.idCliente.value;
    return this.clientes().find((cliente) => cliente.idCliente === id) ?? null;
  });

  readonly form = this.fb.nonNullable.group({
    idCliente: [0, [Validators.required, Validators.min(1)]],
    idVendedor: [0, [Validators.required, Validators.min(1)]],
    moneda: ['SOLES', [Validators.required]],
    fechaVencimiento: [''],
    direccionDespacho: [''],
    depProvDis: [''],
    flagCubierto: [false],
    observaciones: [''],
    idEstadoCotizacion: [0],
  });

  readonly productForm = this.fb.nonNullable.group({
    idProducto: [0, [Validators.required, Validators.min(1)]],
    cantidad: [1, [Validators.required, Validators.min(1)]],
  });

  ngOnInit(): void {
    if (this.permissions.isSeller()) {
      this.form.controls.idVendedor.disable({ emitEvent: false });
      const idUsuario = this.permissions.currentUserId();
      if (idUsuario) {
        this.form.controls.idVendedor.setValue(idUsuario, { emitEvent: false });
      }
    }
    this.loadCatalogs();
    this.clienteSearch.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((value) =>
          this.searchClientes(value).pipe(
            catchError(() => {
              this.notifications.error('No se pudo buscar clientes.');
              this.isSearchingClientes.set(false);
              return of([]);
            }),
          ),
        ),
      )
      .subscribe({
        next: (clientes) => this.clientes.set(clientes),
    });
    this.form.controls.idCliente.valueChanges.subscribe((idCliente) => {
      this.selectedClientId.set(this.toNumber(idCliente));
      this.applyClienteDefaults(this.toNumber(idCliente));
    });
    this.form.controls.moneda.valueChanges.subscribe(() => this.clearItems());
    this.productForm.controls.idProducto.valueChanges.subscribe((idProducto) =>
      this.selectedProductId.set(this.toNumber(idProducto)),
    );
  }

  addItem(): void {
    this.ensureSelectedClienteFromSearch();
    if (this.form.controls.idCliente.invalid) {
      this.form.markAllAsTouched();
      this.notifications.error('Selecciona un cliente antes de agregar productos.');
      return;
    }
    if (this.productForm.invalid) {
      this.form.markAllAsTouched();
      this.productForm.markAllAsTouched();
      return;
    }
    const raw = this.productForm.getRawValue();
    const idProducto = this.toNumber(raw.idProducto);
    const cantidad = this.toNumber(raw.cantidad);
    const validationMessage = this.validateProductSelection(idProducto, cantidad);
    if (validationMessage) {
      this.notifications.error(validationMessage);
      return;
    }
    this.isCalculating.set(true);
    this.service
      .calcularItem({
        idCliente: this.form.controls.idCliente.value,
        idProducto,
        cantidad,
        moneda: this.form.controls.moneda.value,
      })
      .pipe(finalize(() => this.isCalculating.set(false)))
      .subscribe({
        next: (item) => {
          this.items.update((current) => {
            const nextItems = current.filter((existing) => existing.idProducto !== item.idProducto);
            return [...nextItems, item];
          });
          this.clearProductForm();
          this.refreshResumen();
        },
        error: (error: HttpErrorResponse) => {
          this.applyFieldErrors(error);
          this.notifications.error(error.error?.message ?? 'No se pudo calcular el producto seleccionado.');
        },
      });
  }

  editItem(item: CotizacionCalcularItemResponse): void {
    this.editingProductId.set(item.idProducto);
    this.productForm.setValue({ idProducto: item.idProducto, cantidad: item.cantidad });
  }

  cancelEdit(): void {
    this.clearProductForm();
  }

  removeItem(idProducto: number): void {
    this.items.update((current) => current.filter((item) => item.idProducto !== idProducto));
    if (this.editingProductId() === idProducto) {
      this.clearProductForm();
    }
    this.refreshResumen();
  }

  save(generatePdf: boolean): void {
    if (this.form.invalid || !this.items().length) {
      this.form.markAllAsTouched();
      this.notifications.error('Completa los datos obligatorios y agrega al menos un producto.');
      return;
    }
    this.isSaving.set(true);
    const request = this.buildRequest();
    const create$ = this.service.createCotizacion(request);
    const flow$ = generatePdf
      ? create$.pipe(switchMap((cotizacion) => this.service.generarPdf(cotizacion.idCotizacion).pipe(map(() => cotizacion))))
      : create$;

    flow$.pipe(finalize(() => this.isSaving.set(false))).subscribe({
      next: (cotizacion) => {
        this.notifications.success(
          generatePdf
            ? 'Cotizacion generada correctamente. El stock fue reservado por 24 horas y el PDF fue generado.'
            : 'Cotizacion generada correctamente. El stock fue reservado por 24 horas.',
        );
        this.router.navigate(['/cotizaciones', cotizacion.idCotizacion]);
      },
      error: (error: HttpErrorResponse) => {
        this.applyFieldErrors(error);
        this.notifications.error(error.error?.message ?? 'No se pudo registrar la cotización.');
      },
    });
  }

  formatMoney(value: number, moneda = this.form.controls.moneda.value): string {
    const symbol = this.normalize(moneda).includes('dolar') ? 'US$' : 'S/';
    return `${symbol} ${Number(value ?? 0).toLocaleString('es-PE', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  }

  productName(idProducto: number): string {
    return this.productos().find((producto) => producto.id === idProducto)?.nombre ?? `Producto #${idProducto}`;
  }

  productMinimumLabel(): string {
    const product = this.currentProduct();
    if (!product) {
      return 'Selecciona un producto para ver condiciones de venta.';
    }
    return `Mínimo ${product.cantMinVenta}. Stock disponible ${product.stockDisponible}.`;
  }

  canSave(): boolean {
    return this.form.valid && this.items().length > 0 && !this.isSaving();
  }

  userLabel(user: UsuarioResponse): string {
    return [user.nombres, user.apellidoPaterno, user.apellidoMaterno].filter(Boolean).join(' ');
  }

  clientLabel(cliente: ClienteV1): string {
    return `${cliente.ruc} - ${cliente.razonSocial}`;
  }

  selectCliente(cliente: ClienteV1, isUserInput = true): void {
    if (!isUserInput) {
      return;
    }
    this.clienteSearch.setValue(this.clientLabel(cliente), { emitEvent: false });
    this.selectedClientId.set(cliente.idCliente);
    this.form.patchValue({ idCliente: cliente.idCliente });
  }

  private loadCatalogs(): void {
    this.isLoading.set(true);
    forkJoin({
      clientes: this.clienteService.findAll(),
      productos: this.productService.getProducts(),
      vendedores: this.permissions.canViewAllSellers() ? this.domainApi.getUsuarios() : of(this.currentUserAsVendedor()),
      estados: this.catalogoService.estadosCotizacion(),
    })
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: ({ clientes, productos, vendedores, estados }) => {
          this.allClientes.set(clientes);
          this.clientes.set(clientes);
          this.productos.set(productos);
          const idUsuario = this.permissions.currentUserId();
          this.vendedores.set(this.permissions.isSeller() && idUsuario
            ? vendedores.filter((vendedor) => vendedor.idUsuario === idUsuario)
            : vendedores);
          this.estados.set(estados);
          const firstEstado = estados[0]?.id ?? 0;
          this.form.patchValue({ idEstadoCotizacion: firstEstado });
        },
        error: () => this.notifications.error('No se pudieron cargar los datos para crear la cotización.'),
      });
  }

  private applyClienteDefaults(idCliente: number, clearProducts = true): void {
    const cliente = this.clientes().find((item) => item.idCliente === idCliente);
    if (!cliente) {
      return;
    }
    this.form.patchValue({
      idVendedor: this.permissions.isSeller()
        ? this.permissions.currentUserId() ?? this.form.controls.idVendedor.value
        : cliente.idVendedorAsignado ?? this.form.controls.idVendedor.value,
      direccionDespacho: cliente.direccion ?? '',
      depProvDis: [cliente.departamento, cliente.provincia, cliente.distrito].filter(Boolean).join(' / '),
    });
    if (clearProducts) {
      this.clearItems();
    }
  }

  private refreshResumen(): void {
    if (!this.items().length) {
      this.resumen.set(null);
      return;
    }
    this.service
      .calcularResumen({
        idCliente: this.form.controls.idCliente.value,
        moneda: this.form.controls.moneda.value,
        detalles: this.items().map((item) => ({ idProducto: item.idProducto, cantidad: item.cantidad })),
      })
      .subscribe({
        next: (resumen) => this.resumen.set(resumen),
        error: (error: HttpErrorResponse) =>
          this.notifications.error(error.error?.message ?? 'No se pudo recalcular el resumen de la cotización.'),
      });
  }

  private clearItems(): void {
    this.items.set([]);
    this.resumen.set(null);
    this.clearProductForm();
  }

  private clearProductForm(): void {
    this.editingProductId.set(null);
    this.selectedProductId.set(0);
    this.productForm.reset({ idProducto: 0, cantidad: 1 });
  }

  private searchClientes(value: string) {
    const query = value.trim();
    const selectedByText = this.findClienteByLabel(query);
    if (selectedByText) {
      this.selectedClientId.set(selectedByText.idCliente);
      this.form.patchValue({ idCliente: selectedByText.idCliente }, { emitEvent: false });
      this.isSearchingClientes.set(false);
      return of(this.allClientes());
    }
    if (this.currentClienteMatches(query)) {
      this.isSearchingClientes.set(false);
      return of(this.allClientes());
    }
    this.selectedClientId.set(0);
    this.form.patchValue({ idCliente: 0 }, { emitEvent: false });
    this.clearItems();
    if (!query) {
      this.isSearchingClientes.set(false);
      return of(this.allClientes());
    }
    this.isSearchingClientes.set(true);
    const ruc = /^\d+$/.test(query) ? query : undefined;
    const razonSocial = ruc ? undefined : query;
    return this.clienteService.buscar(ruc, razonSocial).pipe(finalize(() => this.isSearchingClientes.set(false)));
  }

  private buildRequest(): CotizacionCreateRequest {
    const raw = this.form.getRawValue();
    return {
      idCliente: raw.idCliente,
      idVendedor: this.permissions.isSeller() ? this.permissions.currentUserId() ?? raw.idVendedor : raw.idVendedor,
      moneda: raw.moneda,
      direccionDespacho: raw.direccionDespacho || undefined,
      depProvDis: raw.depProvDis || undefined,
      flagCubierto: raw.flagCubierto ? 1 : 0,
      observaciones: raw.observaciones || undefined,
      detalles: this.items().map((item) => ({
        idProducto: item.idProducto,
        cantidad: item.cantidad,
        precioUni: item.precioUnitario,
      })),
    };
  }

  private normalize(value: string): string {
    return (value ?? '').trim().toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');
  }

  private validateProductSelection(idProducto: number, cantidad: number): string | null {
    const product = this.productos().find((item) => item.id === idProducto);
    if (!product) {
      return 'Selecciona un producto válido.';
    }
    if (cantidad <= 0) {
      return 'La cantidad debe ser mayor a cero.';
    }
    if (cantidad < product.cantMinVenta) {
      return `La cantidad mínima de venta para ${product.nombre} es ${product.cantMinVenta}.`;
    }
    if (cantidad > product.stockDisponible) {
      return `Stock insuficiente. Disponible: ${product.stockDisponible}.`;
    }
    const isEditingSameProduct = this.editingProductId() === idProducto;
    const duplicated = this.items().some((item) => item.idProducto === idProducto);
    if (duplicated && !isEditingSameProduct) {
      return 'Este producto ya fue agregado. Edita la cantidad desde la tabla.';
    }
    return null;
  }

  private currentProduct(): ProductResponse | null {
    const idProducto = this.selectedProductId() || this.toNumber(this.productForm.controls.idProducto.value);
    return this.productos().find((producto) => producto.id === idProducto) ?? null;
  }

  private ensureSelectedClienteFromSearch(): void {
    if (this.form.controls.idCliente.valid) {
      return;
    }
    const cliente = this.findClienteByLabel(this.clienteSearch.value.trim());
    if (!cliente) {
      return;
    }
    this.selectedClientId.set(cliente.idCliente);
    this.form.patchValue({ idCliente: cliente.idCliente }, { emitEvent: false });
    this.applyClienteDefaults(cliente.idCliente, false);
  }

  private findClienteByLabel(value: string): ClienteV1 | null {
    if (!value) {
      return null;
    }
    const normalizedValue = this.normalize(value);
    return (
      this.allClientes().find((cliente) => this.normalize(this.clientLabel(cliente)) === normalizedValue) ??
      this.clientes().find((cliente) => this.normalize(this.clientLabel(cliente)) === normalizedValue) ??
      null
    );
  }

  private currentClienteMatches(value: string): boolean {
    const idCliente = this.selectedClientId() || this.toNumber(this.form.controls.idCliente.value);
    const cliente = this.allClientes().find((item) => item.idCliente === idCliente);
    return Boolean(cliente && this.normalize(this.clientLabel(cliente)) === this.normalize(value));
  }

  private toNumber(value: unknown): number {
    const parsed = Number(value);
    return Number.isFinite(parsed) ? parsed : 0;
  }

  private currentUserAsVendedor(): UsuarioResponse[] {
    const user = this.auth.currentUser();
    return user ? [user] : [];
  }

  private applyFieldErrors(error: HttpErrorResponse): void {
    const apiError = error.error as CotizacionApiError | undefined;
    const fieldErrors = apiError?.fieldErrors;
    if (!fieldErrors) {
      return;
    }
    Object.entries(fieldErrors).forEach(([field, message]) => {
      const formControl = this.form.get(field);
      const productControl = this.productForm.get(field);
      const control = formControl ?? productControl;
      control?.setErrors({ backend: message });
      control?.markAsTouched();
    });
  }
}

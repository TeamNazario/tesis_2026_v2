import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { finalize, forkJoin, switchMap } from 'rxjs';
import { UsuarioResponse } from '../../../../core/auth/models/auth.models';
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

  readonly clientes = signal<ClienteV1[]>([]);
  readonly productos = signal<ProductResponse[]>([]);
  readonly vendedores = signal<UsuarioResponse[]>([]);
  readonly estados = signal<CatalogoItem[]>([]);
  readonly items = signal<CotizacionCalcularItemResponse[]>([]);
  readonly resumen = signal<CotizacionCalcularResumenResponse | null>(null);
  readonly isLoading = signal(false);
  readonly isCalculating = signal(false);
  readonly isSaving = signal(false);

  readonly selectedCliente = computed(() => {
    const id = this.form.controls.idCliente.value;
    return this.clientes().find((cliente) => cliente.idCliente === id) ?? null;
  });

  readonly form = this.fb.nonNullable.group({
    idCliente: [0, [Validators.required, Validators.min(1)]],
    idVendedor: [0, [Validators.required, Validators.min(1)]],
    moneda: ['SOLES', [Validators.required]],
    fechaVencimiento: ['', [Validators.required]],
    direccionDespacho: [''],
    depProvDis: [''],
    flagCubierto: [false],
    observaciones: [''],
    idEstadoCotizacion: [0, [Validators.required, Validators.min(1)]],
  });

  readonly productForm = this.fb.nonNullable.group({
    idProducto: [0, [Validators.required, Validators.min(1)]],
    cantidad: [1, [Validators.required, Validators.min(1)]],
  });

  ngOnInit(): void {
    this.loadCatalogs();
    this.form.controls.idCliente.valueChanges.subscribe((idCliente) => this.applyClienteDefaults(idCliente));
    this.form.controls.moneda.valueChanges.subscribe(() => this.clearItems());
  }

  addItem(): void {
    if (this.form.controls.idCliente.invalid || this.productForm.invalid) {
      this.form.markAllAsTouched();
      this.productForm.markAllAsTouched();
      return;
    }
    const raw = this.productForm.getRawValue();
    this.isCalculating.set(true);
    this.service
      .calcularItem({
        idCliente: this.form.controls.idCliente.value,
        idProducto: raw.idProducto,
        cantidad: raw.cantidad,
        moneda: this.form.controls.moneda.value,
      })
      .pipe(finalize(() => this.isCalculating.set(false)))
      .subscribe({
        next: (item) => {
          this.items.update((current) => [...current.filter((existing) => existing.idProducto !== item.idProducto), item]);
          this.productForm.reset({ idProducto: 0, cantidad: 1 });
          this.refreshResumen();
        },
        error: () => this.notifications.error('No se pudo calcular el producto seleccionado.'),
      });
  }

  removeItem(idProducto: number): void {
    this.items.update((current) => current.filter((item) => item.idProducto !== idProducto));
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
      ? create$.pipe(switchMap((cotizacion) => this.service.generarPdf(cotizacion.idCotizacion).pipe(switchMap(() => [cotizacion]))))
      : create$;

    flow$.pipe(finalize(() => this.isSaving.set(false))).subscribe({
      next: (cotizacion) => {
        this.notifications.success(generatePdf ? 'Cotizacion registrada y PDF generado.' : 'Cotizacion registrada correctamente.');
        this.router.navigate(['/cotizaciones', cotizacion.idCotizacion]);
      },
      error: () => this.notifications.error('No se pudo registrar la cotizacion.'),
    });
  }

  formatMoney(value: number, moneda = this.form.controls.moneda.value): string {
    const symbol = this.normalize(moneda).includes('dolar') ? 'US$' : 'S/';
    return `${symbol} ${Number(value ?? 0).toLocaleString('es-PE', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  }

  productName(idProducto: number): string {
    return this.productos().find((producto) => producto.id === idProducto)?.nombre ?? `Producto #${idProducto}`;
  }

  userLabel(user: UsuarioResponse): string {
    return [user.nombres, user.apellidoPaterno, user.apellidoMaterno].filter(Boolean).join(' ');
  }

  private loadCatalogs(): void {
    this.isLoading.set(true);
    forkJoin({
      clientes: this.clienteService.findAll(),
      productos: this.productService.getProducts(),
      vendedores: this.domainApi.getUsuarios(),
      estados: this.catalogoService.estadosCotizacion(),
    })
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: ({ clientes, productos, vendedores, estados }) => {
          this.clientes.set(clientes);
          this.productos.set(productos);
          this.vendedores.set(vendedores);
          this.estados.set(estados);
          const firstEstado = estados[0]?.id ?? 0;
          this.form.patchValue({ idEstadoCotizacion: firstEstado });
        },
        error: () => this.notifications.error('No se pudieron cargar los datos para crear la cotizacion.'),
      });
  }

  private applyClienteDefaults(idCliente: number): void {
    const cliente = this.clientes().find((item) => item.idCliente === idCliente);
    if (!cliente) {
      return;
    }
    this.form.patchValue({
      idVendedor: cliente.idVendedorAsignado ?? this.form.controls.idVendedor.value,
      direccionDespacho: cliente.direccion ?? '',
      depProvDis: [cliente.departamento, cliente.provincia, cliente.distrito].filter(Boolean).join(' / '),
    });
    this.clearItems();
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
        error: () => this.notifications.error('No se pudo recalcular el resumen de la cotizacion.'),
      });
  }

  private clearItems(): void {
    this.items.set([]);
    this.resumen.set(null);
  }

  private buildRequest(): CotizacionCreateRequest {
    const raw = this.form.getRawValue();
    return {
      idCliente: raw.idCliente,
      idVendedor: raw.idVendedor,
      fechaVencimiento: `${raw.fechaVencimiento}T00:00:00`,
      moneda: raw.moneda,
      direccionDespacho: raw.direccionDespacho || undefined,
      depProvDis: raw.depProvDis || undefined,
      flagCubierto: raw.flagCubierto ? 1 : 0,
      observaciones: raw.observaciones || undefined,
      idEstadoCotizacion: raw.idEstadoCotizacion,
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
}

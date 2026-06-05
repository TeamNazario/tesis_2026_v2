import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Subject, catchError, finalize, forkJoin, of, switchMap, takeUntil, throwError } from 'rxjs';
import { ApiError } from '../../../../core/http/api-error.model';
import { PermissionService } from '../../../../core/auth/services/permission.service';
import { CatalogoV1Service } from '../../../../core/services/catalogo-v1.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { ProductService } from '../../../../core/services/product.service';
import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { MaterialModule } from '../../../../shared/material/material.module';
import {
  PrecioTipoClienteCreateRequest,
  PrecioTipoClienteResponse,
  PrecioTipoClienteUpdateRequest,
} from '../../models/precio-tipo-cliente.model';
import { PrecioTipoClienteService } from '../../services/precio-tipo-cliente.service';

type FormMode = 'create' | 'edit';

@Component({
  selector: 'app-precio-tipo-cliente-form',
  imports: [ReactiveFormsModule, RouterLink, MaterialModule, PageHeaderComponent, EmptyStateComponent],
  templateUrl: './precio-tipo-cliente-form.component.html',
  styleUrl: './precio-tipo-cliente-form.component.scss',
})
export class PrecioTipoClienteFormComponent implements OnInit, OnDestroy {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly destroy$ = new Subject<void>();
  private readonly permissions = inject(PermissionService);
  private readonly pricesService = inject(PrecioTipoClienteService);
  private readonly productsService = inject(ProductService);
  private readonly catalogosService = inject(CatalogoV1Service);
  private readonly notifications = inject(NotificationService);

  readonly productos = signal<{ id: number; nombre: string; unidadMedida?: string }[]>([]);
  readonly tiposCliente = signal<{ id: number; descripcion: string }[]>([]);
  readonly estadosProducto = signal<{ id: number; descripcion: string }[]>([]);
  readonly isLoading = signal(false);
  readonly isSaving = signal(false);
  readonly hasError = signal(false);
  readonly catalogError = signal<string | null>(null);
  readonly currentPrice = signal<PrecioTipoClienteResponse | null>(null);

  readonly form = this.fb.nonNullable.group({
    idProducto: [0, [Validators.required, Validators.min(1)]],
    idTipoCliente: [0, [Validators.required, Validators.min(1)]],
    precioUnitario: [0, [Validators.required, Validators.min(0.01)]],
    moneda: ['SOLES', [Validators.required]],
    idEstadoProducto: [0, [Validators.required, Validators.min(1)]],
  });

  private readonly priceId = Number(this.route.snapshot.paramMap.get('id') ?? 0);
  readonly mode: FormMode = (this.route.snapshot.data['mode'] as FormMode) ?? (this.priceId > 0 ? 'edit' : 'create');

  readonly canManagePrices = signal(this.permissions.canEditPrices());

  ngOnInit(): void {
    if (!this.canManagePrices()) {
      this.notifications.error('No tienes permisos para acceder a esta pantalla.');
      this.router.navigate(['/dashboard']);
      return;
    }

    this.loadData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get isEditMode(): boolean {
    return this.mode === 'edit';
  }

  get title(): string {
    return this.isEditMode ? 'Editar precio' : 'Nuevo precio';
  }

  get description(): string {
    return 'Gestiona el precio unitario del producto por tipo de cliente con una interfaz clara y consistente.';
  }

  get selectedProductLabel(): string {
    const item = this.currentPrice();
    if (!item) {
      return '-';
    }
    return item.producto?.trim() || this.resolveProductLabel(item.idProducto);
  }

  get selectedTipoClienteLabel(): string {
    const item = this.currentPrice();
    if (!item) {
      return '-';
    }
    return item.tipoCliente?.trim() || this.resolveTipoClienteLabel(item.idTipoCliente);
  }

  get selectedStateLabel(): string {
    const item = this.currentPrice();
    if (!item) {
      return '-';
    }
    return item.estadoProducto?.trim() || this.resolveEstadoLabel(item.idEstadoProducto);
  }

  save(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid || this.isSaving()) {
      return;
    }

    const raw = this.form.getRawValue();
    const request: PrecioTipoClienteCreateRequest = {
      idProducto: Number(raw.idProducto ?? 0),
      idTipoCliente: Number(raw.idTipoCliente ?? 0),
      precioUnitario: Number(raw.precioUnitario ?? 0),
      moneda: raw.moneda,
      idEstadoProducto: Number(raw.idEstadoProducto ?? 0),
    };
    const updateRequest: PrecioTipoClienteUpdateRequest = request;

    this.isSaving.set(true);
    const save$ = this.isEditMode
      ? this.pricesService.updatePrecio(this.priceId, updateRequest)
      : this.pricesService.getPreciosByProducto(request.idProducto).pipe(
          switchMap((existing) => {
            if (this.pricesService.hasDuplicateCombination(existing, request.idProducto, request.idTipoCliente)) {
              return throwError(() => new Error('duplicate'));
            }
            return this.pricesService.createPrecio(request);
          }),
        );

    save$.pipe(finalize(() => this.isSaving.set(false)), takeUntil(this.destroy$)).subscribe({
      next: () => {
        this.notifications.success(this.isEditMode ? 'Precio actualizado correctamente.' : 'Precio registrado correctamente.');
        this.router.navigate(['/precios-tipo-cliente']);
      },
      error: (error: HttpErrorResponse | Error) => this.handleSaveError(error),
    });
  }

  cancel(): void {
    this.router.navigate(['/precios-tipo-cliente']);
  }

  controlError(name: 'idProducto' | 'idTipoCliente' | 'precioUnitario' | 'moneda' | 'idEstadoProducto'): string | null {
    const control = this.form.controls[name];
    if (!control.touched || !control.errors) {
      return null;
    }
    if (control.errors['required']) {
      return 'Este campo es obligatorio.';
    }
    if (control.errors['min']) {
      return name === 'precioUnitario' ? 'El precio debe ser mayor a 0.' : 'Selecciona una opcion valida.';
    }
    if (control.errors['server']) {
      return control.errors['server'] as string;
    }
    return 'Revisa este campo.';
  }

  private loadData(): void {
    this.isLoading.set(true);
    this.hasError.set(false);
    this.catalogError.set(null);

    const price$ = this.isEditMode && this.priceId > 0
      ? this.pricesService.getPrecioById(this.priceId).pipe(
          catchError((error: HttpErrorResponse) => {
            this.hasError.set(true);
            this.notifications.error(error.error?.message ?? 'No se pudo cargar el precio solicitado.');
            return of(null);
          }),
        )
      : of(null);

    forkJoin({
      productos: this.productsService.getProducts().pipe(
        catchError(() => {
          this.catalogError.set('No se pudo cargar la lista de productos.');
          return of([] as { id: number; nombre: string; unidadMedida?: string }[]);
        }),
      ),
      tiposCliente: this.catalogosService.tiposClienteActivos().pipe(
        catchError(() => {
          this.catalogError.set('No se pudo cargar la lista de tipos de cliente.');
          return of([] as { id: number; descripcion: string }[]);
        }),
      ),
      estadosProducto: this.catalogosService.estadosProducto().pipe(
        catchError(() => {
          this.catalogError.set('No se pudo cargar la lista de estados del producto.');
          return of([] as { id: number; descripcion: string }[]);
        }),
      ),
      price: price$,
    })
      .pipe(finalize(() => this.isLoading.set(false)), takeUntil(this.destroy$))
      .subscribe({
        next: ({ productos, tiposCliente, estadosProducto, price }) => {
          this.productos.set(productos.map((item) => ({ id: item.id, nombre: item.nombre, unidadMedida: item.unidadMedida })));
          this.tiposCliente.set(tiposCliente);
          this.estadosProducto.set(estadosProducto);
          if (price) {
            this.currentPrice.set(price);
            this.applyPrice(price);
          } else {
            this.applyCreateDefaults();
          }
        },
      });
  }

  private applyPrice(price: PrecioTipoClienteResponse): void {
    this.form.patchValue({
      idProducto: price.idProducto,
      idTipoCliente: price.idTipoCliente,
      precioUnitario: price.precioUnitario,
      moneda: price.moneda || 'SOLES',
      idEstadoProducto: price.idEstadoProducto,
    });
    this.form.controls.idProducto.disable({ emitEvent: false });
    this.form.controls.idTipoCliente.disable({ emitEvent: false });
  }

  private applyCreateDefaults(): void {
    const estadoActivo = this.estadosProducto().find((item) => this.normalize(item.descripcion).includes('activo'));
    if (estadoActivo) {
      this.form.controls.idEstadoProducto.setValue(estadoActivo.id);
    }
    this.form.controls.moneda.setValue('SOLES');
  }

  private handleSaveError(error: HttpErrorResponse | Error): void {
    if (error instanceof Error && error.message === 'duplicate') {
      this.notifications.error('Ya existe un precio configurado para este producto y tipo de cliente.');
      return;
    }

    if (error instanceof HttpErrorResponse) {
      if (error.status === 400) {
        this.applyFieldErrors(error.error as ApiError);
        this.notifications.error(error.error?.message ?? 'Hay errores de validacion en el formulario.');
        return;
      }
      if (error.status === 403) {
        this.notifications.error('No tienes permisos para acceder a este recurso.');
        return;
      }
      if (error.status === 404) {
        this.notifications.error('Producto, tipo de cliente o precio no encontrado.');
        return;
      }
      if (error.status === 409) {
        this.notifications.error('Ya existe un precio configurado para este producto y tipo de cliente.');
        return;
      }
      this.notifications.error(error.error?.message ?? 'Ocurrio un error inesperado. Intenta nuevamente.');
      return;
    }

    this.notifications.error('Ocurrio un error inesperado. Intenta nuevamente.');
  }

  private applyFieldErrors(error?: ApiError): void {
    const fieldErrors = error?.fieldErrors ?? error?.fields ?? error?.errors;
    if (!fieldErrors) {
      return;
    }

    Object.entries(fieldErrors).forEach(([field, messages]) => {
      const control = this.form.get(field);
      if (!control) {
        return;
      }
      const message = Array.isArray(messages) ? messages[0] : String(messages);
      control.setErrors({ server: message });
      control.markAsTouched();
    });
  }

  private resolveProductLabel(idProducto: number): string {
    return this.productos().find((item) => item.id === idProducto)?.nombre ?? `Producto #${idProducto}`;
  }

  private resolveTipoClienteLabel(idTipoCliente: number): string {
    return this.tiposCliente().find((item) => item.id === idTipoCliente)?.descripcion ?? `Tipo #${idTipoCliente}`;
  }

  private resolveEstadoLabel(idEstadoProducto: number): string {
    return this.estadosProducto().find((item) => item.id === idEstadoProducto)?.descripcion ?? `Estado #${idEstadoProducto}`;
  }

  private normalize(value?: string): string {
    return (value ?? '')
      .trim()
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '');
  }
}

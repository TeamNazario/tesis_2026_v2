import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Subject, catchError, finalize, forkJoin, of, takeUntil } from 'rxjs';
import { PermissionService } from '../../../../core/auth/services/permission.service';
import { CatalogoV1Service } from '../../../../core/services/catalogo-v1.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { ProductService } from '../../../../core/services/product.service';
import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { MaterialModule } from '../../../../shared/material/material.module';
import { PrecioTipoClienteResponse } from '../../models/precio-tipo-cliente.model';
import { PrecioTipoClienteService } from '../../services/precio-tipo-cliente.service';

@Component({
  selector: 'app-precio-tipo-cliente-detail',
  imports: [RouterLink, MaterialModule, PageHeaderComponent, EmptyStateComponent],
  templateUrl: './precio-tipo-cliente-detail.component.html',
  styleUrl: './precio-tipo-cliente-detail.component.scss',
})
export class PrecioTipoClienteDetailComponent implements OnInit, OnDestroy {
  private readonly route = inject(ActivatedRoute);
  private readonly permissions = inject(PermissionService);
  private readonly notifications = inject(NotificationService);
  private readonly pricesService = inject(PrecioTipoClienteService);
  private readonly productsService = inject(ProductService);
  private readonly catalogosService = inject(CatalogoV1Service);
  private readonly destroy$ = new Subject<void>();
  private readonly priceId = Number(this.route.snapshot.paramMap.get('id') ?? 0);

  readonly canManagePrices = computed(() => this.permissions.canEditPrices());
  readonly isLoading = signal(false);
  readonly hasError = signal(false);
  readonly catalogError = signal<string | null>(null);
  readonly price = signal<PrecioTipoClienteResponse | null>(null);
  readonly productos = signal<{ id: number; nombre: string; unidadMedida?: string }[]>([]);
  readonly tiposCliente = signal<{ id: number; descripcion: string }[]>([]);
  readonly estadosProducto = signal<{ id: number; descripcion: string }[]>([]);

  ngOnInit(): void {
    this.loadData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get title(): string {
    return 'Detalle del precio';
  }

  get description(): string {
    return 'Consulta la configuracion del precio y su contexto comercial.';
  }

  get productLabel(): string {
    const item = this.price();
    if (!item) {
      return '-';
    }
    return item.producto?.trim() || this.resolveProductLabel(item.idProducto);
  }

  get tipoClienteLabel(): string {
    const item = this.price();
    if (!item) {
      return '-';
    }
    return item.tipoCliente?.trim() || this.resolveTipoClienteLabel(item.idTipoCliente);
  }

  get estadoLabel(): string {
    const item = this.price();
    if (!item) {
      return '-';
    }
    return item.estadoProducto?.trim() || this.resolveEstadoLabel(item.idEstadoProducto);
  }

  get unidadMedida(): string {
    const item = this.price();
    if (!item) {
      return '-';
    }
    return item.unidadMedida?.trim() || this.resolveProductUnit(item.idProducto) || '-';
  }

  formatMoney(value: number, moneda: string): string {
    const formatted = new Intl.NumberFormat('es-PE', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }).format(value ?? 0);
    return `${this.currencySymbol(moneda)} ${formatted}`;
  }

  getCurrencyLabel(moneda: string): string {
    const normalized = this.normalize(moneda);
    return normalized.includes('dolar') ? 'Dolares' : 'Soles';
  }

  getCurrencyTone(moneda: string): 'ok' | 'warn' {
    return this.normalize(moneda).includes('dolar') ? 'warn' : 'ok';
  }

  stateTone(): 'ok' | 'warn' | 'danger' | '' {
    const item = this.price();
    if (!item) {
      return '';
    }
    const id = item.idEstadoProducto;
    if (id === 1) {
      return 'ok';
    }
    if (id === 2) {
      return 'warn';
    }
    if (id === 3) {
      return 'danger';
    }
    const label = this.normalize(this.estadoLabel);
    if (label.includes('activo') || label.includes('habilitado')) {
      return 'ok';
    }
    if (label.includes('inactivo') || label.includes('deshabilitado')) {
      return 'warn';
    }
    if (label.includes('bloqueado')) {
      return 'danger';
    }
    return '';
  }

  private loadData(): void {
    this.isLoading.set(true);
    this.hasError.set(false);

    const price$ = this.priceId > 0
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
          this.price.set(price);
        },
      });
  }

  private resolveProductLabel(idProducto: number): string {
    return this.productos().find((item) => item.id === idProducto)?.nombre ?? `Producto #${idProducto}`;
  }

  private resolveProductUnit(idProducto: number): string | undefined {
    return this.productos().find((item) => item.id === idProducto)?.unidadMedida;
  }

  private resolveTipoClienteLabel(idTipoCliente: number): string {
    return this.tiposCliente().find((item) => item.id === idTipoCliente)?.descripcion ?? `Tipo #${idTipoCliente}`;
  }

  private resolveEstadoLabel(idEstadoProducto: number): string {
    return this.estadosProducto().find((item) => item.id === idEstadoProducto)?.descripcion ?? `Estado #${idEstadoProducto}`;
  }

  private currencySymbol(moneda: string): string {
    return this.normalize(moneda).includes('dolar') ? 'US$' : 'S/';
  }

  private normalize(value?: string): string {
    return (value ?? '')
      .trim()
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '');
  }
}

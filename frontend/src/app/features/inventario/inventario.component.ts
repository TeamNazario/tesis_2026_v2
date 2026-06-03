import { Component, computed, inject, signal } from '@angular/core';
import { catchError, finalize, forkJoin, map, of } from 'rxjs';
import { ProductoResponse } from '../../core/models/domain.models';
import { PrecioTipoClienteV1 } from '../../core/models/v1.models';
import { DomainApiService } from '../../core/services/domain-api.service';
import { PrecioTipoClienteV1Service } from '../../core/services/precio-tipo-cliente-v1.service';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { StatusChipComponent } from '../../shared/components/status-chip/status-chip.component';
import { MaterialModule } from '../../shared/material/material.module';

type PrecioInventario = PrecioTipoClienteV1 & {
  tipoClienteLabel: string;
  precioLabel: string;
};

@Component({
  selector: 'app-inventario',
  imports: [MaterialModule, PageHeaderComponent, EmptyStateComponent, StatusChipComponent],
  template: `
    <app-page-header
      title="Inventario"
      description="Explora el stock disponible, la cantidad minima de venta y la matriz de precios por tipo de cliente."
    />

    @if (hasPricesError()) {
      <div class="prices-warning">
        No se pudieron cargar todos los precios por tipo de cliente. El inventario sigue disponible.
      </div>
    }

    <section class="toolbar">
      <div class="search-card">
        <div class="search-copy">
          <h2>Buscar producto</h2>
          <p>Filtra por nombre, unidad de medida o estado del producto.</p>
        </div>
        <mat-form-field appearance="outline" class="search-field">
          <mat-label>Buscar producto</mat-label>
          <input
            matInput
            [value]="searchTerm()"
            (input)="onSearch(($any($event.target)).value)"
            placeholder="Ej. Biofluido, 20L, habilitado"
          />
          @if (searchTerm()) {
            <button mat-icon-button matSuffix type="button" aria-label="Limpiar búsqueda" (click)="clearSearch()">
              <mat-icon>close</mat-icon>
            </button>
          } @else {
            <mat-icon matSuffix>search</mat-icon>
          }
        </mat-form-field>
      </div>

      <div class="stats-grid">
        <article class="stat-card accent">
          <span>Productos visibles</span>
          <strong>{{ filteredProductos().length }}</strong>
          <p>De {{ productos().length }} cargados</p>
        </article>
        <article class="stat-card">
          <span>Stock bajo</span>
          <strong>{{ lowStockCount() }}</strong>
          <p>Requieren revisión prioritaria</p>
        </article>
        <article class="stat-card">
          <span>Con precios</span>
          <strong>{{ productsWithPrices() }}</strong>
          <p>Segmentos comerciales configurados</p>
        </article>
      </div>
    </section>

    @if (isLoading()) {
      <section class="loading-shell">
        <app-empty-state title="Cargando inventario" description="Estamos preparando los productos y sus precios por tipo de cliente." />
      </section>
    } @else if (hasError()) {
      <app-empty-state icon="error" title="No se pudo cargar" description="No fue posible cargar el inventario. Intenta nuevamente." />
    } @else if (!filteredProductos().length) {
      <app-empty-state
        title="Sin productos"
        description="No encontramos productos que coincidan con la búsqueda actual."
      />
    } @else {
      <section class="inventory-grid">
        @for (item of filteredProductos(); track item.idProducto) {
          <article class="product-card">
            <div class="product-top">
              <div class="product-title">
                <h3>{{ item.nombreProducto }}</h3>
                <p>{{ item.unidadMedida || 'Producto BIOFLUID' }}</p>
              </div>
              <app-status-chip [label]="getStockLabel(item)" />
            </div>

            <div class="metrics">
              <div>
                <span>Stock fisico</span>
                <strong>{{ item.stockFisico }}</strong>
              </div>
              <div>
                <span>Reservado</span>
                <strong>{{ item.stockReservado }}</strong>
              </div>
              <div>
                <span>Disponible</span>
                <strong>{{ item.stockDisponible }}</strong>
              </div>
              <div>
                <span>Min. venta</span>
                <strong>{{ item.cantMinVenta }}</strong>
              </div>
            </div>

            <div class="progress-copy">
              <span>Disponibilidad</span>
              <strong>{{ stockPercent(item) }}%</strong>
            </div>
            <mat-progress-bar mode="determinate" [value]="stockPercent(item)" />

            <div class="price-section">
              <div class="section-heading">
                <h4>Precios por tipo de cliente</h4>
                <span>{{ getPrices(item.idProducto).length }} tarifas</span>
              </div>

              @if (getPrices(item.idProducto).length) {
                <div class="price-list">
                  @for (price of getPrices(item.idProducto); track price.idPrecio) {
                    <div class="price-pill">
                      <div>
                        <strong>{{ price.tipoClienteLabel }}</strong>
                        <span>{{ price.estadoProducto || 'Vigente' }}</span>
                      </div>
                      <b>{{ price.precioLabel }}</b>
                    </div>
                  }
                </div>
              } @else {
                <p class="empty-prices">No hay precios configurados para este producto.</p>
              }
            </div>
          </article>
        }
      </section>
    }
  `,
  styles: `
    :host {
      display: block;
    }

    .toolbar {
      display: grid;
      gap: 1rem;
      margin: 1rem 0 1.5rem;
    }

    .search-card,
    .stat-card,
    .product-card {
      border: 1px solid rgba(15, 23, 42, 0.08);
      background: linear-gradient(180deg, #ffffff 0%, #fbfcfd 100%);
      box-shadow: 0 18px 36px rgba(15, 23, 42, 0.06);
    }

    .search-card {
      display: grid;
      grid-template-columns: 1.2fr 1fr;
      gap: 1rem;
      align-items: center;
      padding: 1.25rem;
      border-radius: 1.5rem;
      background:
        radial-gradient(circle at top left, rgba(0, 110, 37, 0.08), transparent 30%),
        linear-gradient(180deg, #ffffff 0%, #fbfcfd 100%);
    }

    .search-copy h2,
    .product-title h3,
    .section-heading h4 {
      margin: 0;
      font-family: Manrope, sans-serif;
      letter-spacing: -0.02em;
    }

    .search-copy p,
    .product-title p,
    .empty-prices,
    .stat-card p,
    .price-pill span,
    .section-heading span,
    .metrics span {
      color: var(--bio-on-surface-variant);
    }

    .search-field {
      width: 100%;
    }

    .stats-grid {
      display: grid;
      grid-template-columns: repeat(3, minmax(0, 1fr));
      gap: 1rem;
    }

    .stat-card {
      padding: 1rem 1.1rem;
      border-radius: 1.25rem;
    }

    .stat-card.accent {
      background: linear-gradient(135deg, rgba(0, 110, 37, 0.12), rgba(255, 255, 255, 0.98));
    }

    .stat-card span {
      display: block;
      font-size: 0.86rem;
    }

    .stat-card strong {
      display: block;
      margin: 0.25rem 0;
      font-size: 1.8rem;
      line-height: 1;
      font-family: Manrope, sans-serif;
      color: #0b3f1a;
    }

    .inventory-grid {
      display: grid;
      grid-template-columns: repeat(2, minmax(0, 1fr));
      gap: 1rem;
    }

    .product-card {
      padding: 1.25rem;
      border-radius: 1.5rem;
      display: grid;
      gap: 1rem;
    }

    .product-top {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      gap: 1rem;
    }

    .metrics {
      display: grid;
      grid-template-columns: repeat(4, minmax(0, 1fr));
      gap: 0.75rem;
      padding: 0.25rem 0;
    }

    .metrics div {
      padding: 0.75rem;
      border-radius: 1rem;
      background: rgba(15, 23, 42, 0.03);
    }

    .metrics span {
      display: block;
      font-size: 0.8rem;
      margin-bottom: 0.2rem;
    }

    .metrics strong {
      display: block;
      font-size: 1.2rem;
      font-family: Manrope, sans-serif;
      color: #0b3f1a;
    }

    .progress-copy {
      display: flex;
      justify-content: space-between;
      gap: 1rem;
      font-size: 0.86rem;
    }

    .price-section {
      display: grid;
      gap: 0.75rem;
      padding-top: 0.25rem;
      border-top: 1px solid rgba(15, 23, 42, 0.08);
    }

    .section-heading {
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 1rem;
    }

    .price-list {
      display: grid;
      gap: 0.65rem;
    }

    .price-pill {
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 1rem;
      padding: 0.75rem 0.9rem;
      border-radius: 1rem;
      background: rgba(0, 110, 37, 0.05);
    }

    .price-pill div {
      display: grid;
      gap: 0.1rem;
    }

    .price-pill strong {
      font-size: 0.95rem;
    }

    .price-pill b {
      font-size: 1rem;
      color: #0b3f1a;
      white-space: nowrap;
    }

    .empty-prices {
      margin: 0;
      font-size: 0.92rem;
    }

    .loading-shell {
      padding: 1rem 0;
    }

    .prices-warning {
      margin: 1rem 0 0;
      padding: 0.9rem 1rem;
      border-radius: 1rem;
      background: rgba(171, 45, 87, 0.08);
      color: #7d1238;
      border: 1px solid rgba(171, 45, 87, 0.18);
    }

    @media (max-width: 1100px) {
      .search-card,
      .inventory-grid {
        grid-template-columns: 1fr;
      }

      .stats-grid {
        grid-template-columns: repeat(3, minmax(0, 1fr));
      }

      .metrics {
        grid-template-columns: repeat(2, minmax(0, 1fr));
      }
    }

    @media (max-width: 720px) {
      .search-card,
      .product-card {
        padding: 1rem;
      }

      .stats-grid,
      .metrics {
        grid-template-columns: 1fr;
      }

      .product-top,
      .section-heading,
      .price-pill {
        flex-direction: column;
        align-items: flex-start;
      }
    }
  `,
})
export class InventarioComponent {
  private readonly api = inject(DomainApiService);
  private readonly preciosService = inject(PrecioTipoClienteV1Service);

  readonly productos = signal<ProductoResponse[]>([]);
  readonly precios = signal<PrecioTipoClienteV1[]>([]);
  readonly searchTerm = signal('');
  readonly isLoading = signal(false);
  readonly isLoadingPrices = signal(false);
  readonly hasError = signal(false);
  readonly hasPricesError = signal(false);

  readonly filteredProductos = computed(() => {
    const term = this.normalize(this.searchTerm());
    if (!term) {
      return this.productos();
    }
    return this.productos().filter((item) =>
      [
        item.nombreProducto,
        item.unidadMedida,
        item.estadoProducto ?? '',
        item.descEstadoProducto ?? '',
        String(item.cantMinVenta ?? ''),
      ]
        .join(' ')
        .toLowerCase()
        .includes(term),
    );
  });

  readonly preciosPorProducto = computed(() => {
    const grouped = new Map<number, PrecioInventario[]>();
    for (const price of this.precios()) {
      const entry: PrecioInventario = {
        ...price,
        tipoClienteLabel: price.tipoCliente?.trim() || 'Tipo de cliente',
        precioLabel: this.formatMoney(price.precioUnitario, price.moneda),
      };
      const current = grouped.get(price.idProducto) ?? [];
      current.push(entry);
      grouped.set(price.idProducto, current);
    }
    for (const items of grouped.values()) {
      items.sort((left, right) => left.tipoClienteLabel.localeCompare(right.tipoClienteLabel, 'es'));
    }
    return grouped;
  });

  readonly lowStockCount = computed(() => this.productos().filter((item) => item.stockDisponible <= item.stockMinimo).length);
  readonly productsWithPrices = computed(() => this.productos().filter((item) => this.getPrices(item.idProducto).length > 0).length);

  constructor() {
    this.loadData();
  }

  onSearch(value: string): void {
    this.searchTerm.set(value);
  }

  clearSearch(): void {
    this.searchTerm.set('');
  }

  stockPercent(item: ProductoResponse): number {
    return item.stockFisico ? Math.round((item.stockDisponible / item.stockFisico) * 100) : 0;
  }

  getStockLabel(item: ProductoResponse): string {
    if (item.stockDisponible <= 0) {
      return 'Sin stock';
    }
    if (item.stockDisponible <= item.stockMinimo) {
      return 'Stock bajo';
    }
    return 'Disponible';
  }

  getPrices(idProducto: number): PrecioInventario[] {
    return this.preciosPorProducto().get(idProducto) ?? [];
  }

  private loadData(): void {
    this.isLoading.set(true);
    this.hasError.set(false);
    this.api
      .getProductos()
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: (productos) => {
          this.productos.set(productos);
          this.loadPricesForProducts(productos);
        },
        error: () => {
          this.hasError.set(true);
        },
      });
  }

  private loadPricesForProducts(productos: ProductoResponse[]): void {
    this.isLoadingPrices.set(true);
    this.hasPricesError.set(false);
    if (!productos.length) {
      this.precios.set([]);
      this.isLoadingPrices.set(false);
      return;
    }

    const requests = productos.map((producto) =>
      this.preciosService.findByProductoId(producto.idProducto).pipe(
        map((precios) => ({ idProducto: producto.idProducto, precios })),
        catchError(() => of({ idProducto: producto.idProducto, precios: [] as PrecioTipoClienteV1[], failed: true })),
      ),
    );

    forkJoin(requests)
      .pipe(finalize(() => this.isLoadingPrices.set(false)))
      .subscribe({
        next: (results) => {
          const hasFailures = results.some((result) => 'failed' in result && result.failed);
          this.precios.set(results.flatMap((result) => result.precios));
          this.hasPricesError.set(hasFailures);
        },
        error: () => {
          this.precios.set([]);
          this.hasPricesError.set(true);
        },
      });
  }

  private formatMoney(amount: number, currency: string): string {
    return new Intl.NumberFormat('es-PE', {
      style: 'currency',
      currency: currency || 'PEN',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    }).format(amount ?? 0);
  }

  private normalize(value: string): string {
    return value
      .trim()
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '');
  }
}

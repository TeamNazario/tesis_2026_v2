import { Component, inject, signal } from '@angular/core';
import { ProductoResponse } from '../../core/models/domain.models';
import { DomainApiService } from '../../core/services/domain-api.service';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { StatusChipComponent } from '../../shared/components/status-chip/status-chip.component';
import { MaterialModule } from '../../shared/material/material.module';

@Component({
  selector: 'app-inventario',
  imports: [MaterialModule, PageHeaderComponent, EmptyStateComponent, StatusChipComponent],
  template: `
    <app-page-header title="Inventario" description="Control de stock fisico, reservado y disponible para evitar sobreventa." />
    @if (productos().length) {
      <section class="inventory-grid">
        @for (item of productos(); track item.idProducto) {
          <article>
            <div class="heading">
              <h2>{{ item.nombreProducto }}</h2>
              <app-status-chip [label]="item.stockDisponible <= item.stockMinimo ? 'Stock bajo' : 'Disponible'" />
            </div>
            <p>{{ item.unidadMedida || 'Producto BIOFLUID' }}</p>
            <div class="stock">
              <span><strong>{{ item.stockFisico }}</strong>Fisico</span>
              <span><strong>{{ item.stockReservado }}</strong>Reservado</span>
              <span><strong>{{ item.stockDisponible }}</strong>Disponible</span>
            </div>
            <mat-progress-bar mode="determinate" [value]="stockPercent(item)" />
          </article>
        }
      </section>
    } @else {
      <app-empty-state title="Sin productos en inventario" description="Los productos apareceran cuando el backend responda /productos." />
    }
  `,
  styles: `
    .inventory-grid {
      display: grid;
      grid-template-columns: repeat(3, minmax(0, 1fr));
      gap: 1rem;
    }
    article {
      padding: 1.25rem;
      border-radius: 1rem;
      background: #fff;
    }
    .heading,
    .stock {
      display: flex;
      justify-content: space-between;
      gap: 1rem;
    }
    h2 {
      margin: 0;
      font-family: Manrope, sans-serif;
    }
    p {
      color: var(--bio-on-surface-variant);
    }
    .stock {
      margin: 1rem 0;
    }
    .stock span,
    .stock strong {
      display: block;
    }
    .stock strong {
      font-family: Manrope, sans-serif;
      color: #006e25;
    }
    @media (max-width: 1100px) {
      .inventory-grid {
        grid-template-columns: repeat(2, minmax(0, 1fr));
      }
    }
    @media (max-width: 720px) {
      .inventory-grid,
      .heading,
      .stock {
        grid-template-columns: 1fr;
        flex-direction: column;
      }
    }
  `,
})
export class InventarioComponent {
  private readonly api = inject(DomainApiService);
  readonly productos = signal<ProductoResponse[]>([]);

  constructor() {
    this.api.getProductos().subscribe({ next: (productos) => this.productos.set(productos) });
  }

  stockPercent(item: ProductoResponse): number {
    return item.stockFisico ? Math.round((item.stockDisponible / item.stockFisico) * 100) : 0;
  }
}

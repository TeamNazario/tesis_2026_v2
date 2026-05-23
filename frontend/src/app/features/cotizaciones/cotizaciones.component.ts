import { Component, inject, signal } from '@angular/core';
import { CotizacionResponse } from '../../core/models/domain.models';
import { DomainApiService } from '../../core/services/domain-api.service';
import { FileDownloadService } from '../../core/services/file-download.service';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { StatusChipComponent } from '../../shared/components/status-chip/status-chip.component';
import { MaterialModule } from '../../shared/material/material.module';

@Component({
  selector: 'app-cotizaciones',
  imports: [MaterialModule, PageHeaderComponent, EmptyStateComponent, StatusChipComponent],
  template: `
    <app-page-header title="Cotizaciones" description="Cotizaciones generadas por chatbot y vendedores, con vigencia de 24 horas y control de PDF." />
    <div class="panel">
      @if (cotizaciones().length) {
        <table mat-table [dataSource]="cotizaciones()">
          <ng-container matColumnDef="cliente">
            <th mat-header-cell *matHeaderCellDef>Cliente</th>
            <td mat-cell *matCellDef="let item">{{ item.cliente?.nombre }}</td>
          </ng-container>
          <ng-container matColumnDef="origen">
            <th mat-header-cell *matHeaderCellDef>Origen</th>
            <td mat-cell *matCellDef="let item">{{ item.origenCotizacion }}</td>
          </ng-container>
          <ng-container matColumnDef="monto">
            <th mat-header-cell *matHeaderCellDef>Total</th>
            <td mat-cell *matCellDef="let item">S/ {{ item.montoTotal }}</td>
          </ng-container>
          <ng-container matColumnDef="estado">
            <th mat-header-cell *matHeaderCellDef>Estado</th>
            <td mat-cell *matCellDef="let item"><app-status-chip [label]="item.estadoCotizacion" /></td>
          </ng-container>
          <ng-container matColumnDef="acciones">
            <th mat-header-cell *matHeaderCellDef>Acciones</th>
            <td mat-cell *matCellDef="let item">
              <button mat-icon-button matTooltip="Descargar PDF" (click)="downloadPdf(item)">
                <mat-icon>picture_as_pdf</mat-icon>
              </button>
            </td>
          </ng-container>
          <tr mat-header-row *matHeaderRowDef="columns"></tr>
          <tr mat-row *matRowDef="let row; columns: columns"></tr>
        </table>
      } @else {
        <app-empty-state title="Sin cotizaciones" description="Las cotizaciones apareceran cuando existan registros." />
      }
    </div>
  `,
  styles: `
    .panel {
      overflow-x: auto;
      padding: 1rem;
      border-radius: 1rem;
      background: #fff;
    }
    table {
      width: 100%;
    }
  `,
})
export class CotizacionesComponent {
  private readonly api = inject(DomainApiService);
  private readonly downloads = inject(FileDownloadService);
  readonly cotizaciones = signal<CotizacionResponse[]>([]);
  readonly columns = ['cliente', 'origen', 'monto', 'estado', 'acciones'];

  constructor() {
    this.api.getCotizaciones().subscribe({ next: (items) => this.cotizaciones.set(items) });
  }

  downloadPdf(item: CotizacionResponse): void {
    this.api.downloadCotizacionPdf(item.idCotizacion).subscribe({
      next: (blob) => this.downloads.save(blob, `cotizacion-${item.idCotizacion}.pdf`),
    });
  }
}

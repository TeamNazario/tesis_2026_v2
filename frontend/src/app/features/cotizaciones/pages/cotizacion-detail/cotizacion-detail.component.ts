import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { FileDownloadService } from '../../../../core/services/file-download.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { StatusChipComponent } from '../../../../shared/components/status-chip/status-chip.component';
import { MaterialModule } from '../../../../shared/material/material.module';
import { CotizacionResponse } from '../../models/cotizacion.model';
import { CotizacionService } from '../../services/cotizacion.service';

@Component({
  selector: 'app-cotizacion-detail',
  imports: [CommonModule, RouterLink, MaterialModule, PageHeaderComponent, EmptyStateComponent, StatusChipComponent],
  templateUrl: './cotizacion-detail.component.html',
  styleUrl: './cotizacion-detail.component.scss',
})
export class CotizacionDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly service = inject(CotizacionService);
  private readonly downloads = inject(FileDownloadService);
  private readonly notifications = inject(NotificationService);

  readonly cotizacion = signal<CotizacionResponse | null>(null);
  readonly isLoading = signal(false);
  readonly isPdfLoading = signal(false);
  readonly id = Number(this.route.snapshot.paramMap.get('id'));

  ngOnInit(): void {
    this.loadCotizacion();
  }

  loadCotizacion(): void {
    this.isLoading.set(true);
    this.service
      .getCotizacionById(this.id)
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: (item) => this.cotizacion.set(item),
        error: () => this.notifications.error('No se pudo cargar la cotizacion.'),
      });
  }

  generatePdf(): void {
    this.isPdfLoading.set(true);
    this.service
      .generarPdf(this.id)
      .pipe(finalize(() => this.isPdfLoading.set(false)))
      .subscribe({
        next: () => {
          this.notifications.success('PDF generado correctamente.');
          this.loadCotizacion();
        },
        error: () => this.notifications.error('No se pudo generar el PDF.'),
      });
  }

  downloadPdf(): void {
    const cotizacion = this.cotizacion();
    if (!cotizacion) {
      return;
    }
    this.isPdfLoading.set(true);
    this.service
      .descargarPdf(cotizacion.idCotizacion)
      .pipe(finalize(() => this.isPdfLoading.set(false)))
      .subscribe({
        next: (blob) => this.downloads.save(blob, `COTIZACION-C001-${String(cotizacion.idCotizacion).padStart(6, '0')}.pdf`),
        error: () => this.notifications.error('No se pudo descargar el PDF.'),
      });
  }

  formatMoney(value: number, moneda: string): string {
    const symbol = this.normalize(moneda).includes('dolar') ? 'US$' : 'S/';
    return `${symbol} ${Number(value ?? 0).toLocaleString('es-PE', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  }

  formatDate(value?: string): string {
    return value ? new Date(value).toLocaleDateString('es-PE') : '-';
  }

  private normalize(value: string): string {
    return (value ?? '').trim().toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');
  }
}

import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { filter, finalize } from 'rxjs';
import { CatalogoItem } from '../../../../core/models/v1.models';
import { CatalogoV1Service } from '../../../../core/services/catalogo-v1.service';
import { FileDownloadService } from '../../../../core/services/file-download.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { ConfirmDialogComponent } from '../../../../shared/components/confirm-dialog/confirm-dialog.component';
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
  private readonly catalogoService = inject(CatalogoV1Service);
  private readonly downloads = inject(FileDownloadService);
  private readonly notifications = inject(NotificationService);
  private readonly dialog = inject(MatDialog);

  readonly cotizacion = signal<CotizacionResponse | null>(null);
  readonly estados = signal<CatalogoItem[]>([]);
  readonly isLoading = signal(false);
  readonly isPdfLoading = signal(false);
  readonly isStatusLoading = signal(false);
  readonly id = Number(this.route.snapshot.paramMap.get('id'));

  ngOnInit(): void {
    this.loadEstados();
    this.loadCotizacion();
  }

  loadEstados(): void {
    this.catalogoService.estadosCotizacion().subscribe({
      next: (estados) => this.estados.set(estados),
      error: () => this.notifications.error('No se pudieron cargar los estados de cotización.'),
    });
  }

  loadCotizacion(): void {
    this.isLoading.set(true);
    this.service
      .getCotizacionById(this.id)
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: (item) => this.cotizacion.set(item),
        error: () => this.notifications.error('No se pudo cargar la cotización.'),
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
        next: (blob) => this.downloads.save(blob, this.pdfFileName(cotizacion)),
        error: () => this.notifications.error('No se pudo descargar el PDF de cotización.'),
      });
  }

  changeStatus(estado: CatalogoItem): void {
    const cotizacion = this.cotizacion();
    if (!cotizacion) {
      return;
    }
    const isApproval = this.normalize(estado.descripcion).includes('aprobad');
    this.dialog
      .open(ConfirmDialogComponent, {
        width: '420px',
        data: {
          title: 'Cambiar estado',
          message: isApproval
            ? 'Al aprobar la cotizacion, el stock reservado sera descontado del stock fisico. ¿Deseas continuar?'
            : 'Al cambiar el estado, el stock reservado sera liberado si la cotizacion estaba generada. ¿Deseas continuar?',
          confirmText: 'Cambiar estado',
        },
      })
      .afterClosed()
      .pipe(filter(Boolean))
      .subscribe(() => {
        this.isStatusLoading.set(true);
        this.service
          .cambiarEstado(cotizacion.idCotizacion, { idEstadoCotizacion: estado.id })
          .pipe(finalize(() => this.isStatusLoading.set(false)))
          .subscribe({
            next: (updated) => {
              this.cotizacion.set(updated);
              this.notifications.success('Estado actualizado correctamente.');
            },
            error: (error: HttpErrorResponse) =>
              this.notifications.error(error.error?.message ?? 'No se pudo cambiar el estado de la cotizacion.'),
          });
      });
  }

  formatMoney(value: number, moneda: string): string {
    const symbol = this.normalize(moneda).includes('dolar') ? 'US$' : 'S/';
    return `${symbol} ${Number(value ?? 0).toLocaleString('es-PE', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  }

  formatDate(value?: string): string {
    return value ? new Date(value).toLocaleDateString('es-PE') : '-';
  }

  quoteNumber(item: CotizacionResponse): string {
    return item.numeroCotizacion || `C001-${String(item.idCotizacion).padStart(6, '0')}`;
  }

  availableStates(item: CotizacionResponse): CatalogoItem[] {
    if (!this.isGenerada(item)) {
      return [];
    }
    return this.estados().filter((estado) => {
      const label = this.normalize(estado.descripcion);
      if (estado.id === item.idEstadoCotizacion) {
        return false;
      }
      if (label.includes('aprobad')) {
        return item.puedeAprobarse === true;
      }
      return label.includes('rechaz') || label.includes('anulad');
    });
  }

  hasPdf(item: CotizacionResponse): boolean {
    return Boolean(item.pdfPath);
  }

  isExpired(item: CotizacionResponse): boolean {
    if (item.vencida) {
      return true;
    }
    return Boolean(item.fechaVencimiento) && new Date(item.fechaVencimiento) <= new Date() && this.isGenerada(item);
  }

  formatRemaining(item: CotizacionResponse): string {
    if (this.isExpired(item)) {
      return 'Vencida';
    }
    const seconds = item.tiempoRestanteSegundos
      ?? Math.max(0, Math.floor((new Date(item.fechaVencimiento).getTime() - Date.now()) / 1000));
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    return `${hours}h ${minutes}m`;
  }

  private normalize(value: string): string {
    return (value ?? '').trim().toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');
  }

  private isGenerada(item: CotizacionResponse): boolean {
    return this.normalize(item.descEstadoCotizacion).includes('generad');
  }

  private pdfFileName(item: CotizacionResponse): string {
    return `COTIZACION-${this.quoteNumber(item)}.pdf`;
  }
}

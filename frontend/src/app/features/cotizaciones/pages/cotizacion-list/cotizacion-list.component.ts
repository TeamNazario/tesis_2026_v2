import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { finalize, forkJoin } from 'rxjs';
import { UsuarioResponse } from '../../../../core/auth/models/auth.models';
import { CatalogoItem, ClienteV1 } from '../../../../core/models/v1.models';
import { CatalogoV1Service } from '../../../../core/services/catalogo-v1.service';
import { ClienteV1Service } from '../../../../core/services/cliente-v1.service';
import { DomainApiService } from '../../../../core/services/domain-api.service';
import { FileDownloadService } from '../../../../core/services/file-download.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { EmptyStateComponent } from '../../../../shared/components/empty-state/empty-state.component';
import { PageHeaderComponent } from '../../../../shared/components/page-header/page-header.component';
import { StatusChipComponent } from '../../../../shared/components/status-chip/status-chip.component';
import { MaterialModule } from '../../../../shared/material/material.module';
import { CotizacionFilter, CotizacionResponse } from '../../models/cotizacion.model';
import { CotizacionService } from '../../services/cotizacion.service';

@Component({
  selector: 'app-cotizacion-list',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink,
    MaterialModule,
    PageHeaderComponent,
    EmptyStateComponent,
    StatusChipComponent,
  ],
  templateUrl: './cotizacion-list.component.html',
  styleUrl: './cotizacion-list.component.scss',
})
export class CotizacionListComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly service = inject(CotizacionService);
  private readonly clienteService = inject(ClienteV1Service);
  private readonly catalogoService = inject(CatalogoV1Service);
  private readonly domainApi = inject(DomainApiService);
  private readonly downloads = inject(FileDownloadService);
  private readonly notifications = inject(NotificationService);

  readonly cotizaciones = signal<CotizacionResponse[]>([]);
  readonly clientes = signal<ClienteV1[]>([]);
  readonly vendedores = signal<UsuarioResponse[]>([]);
  readonly estados = signal<CatalogoItem[]>([]);
  readonly isLoading = signal(false);
  readonly pdfLoadingId = signal<number | null>(null);

  readonly columns = [
    'numero',
    'cliente',
    'ruc',
    'vendedor',
    'fechaEmision',
    'fechaVencimiento',
    'moneda',
    'importeTotal',
    'estado',
    'pdf',
    'acciones',
  ];

  readonly filtersForm = this.fb.group({
    search: [''],
    idCliente: ['ALL'],
    idVendedor: ['ALL'],
    idEstadoCotizacion: ['ALL'],
    fechaInicio: [''],
    fechaFin: [''],
  });

  ngOnInit(): void {
    this.loadCatalogs();
    this.loadCotizaciones();
  }

  loadCatalogs(): void {
    forkJoin({
      clientes: this.clienteService.findAll(),
      vendedores: this.domainApi.getUsuarios(),
      estados: this.catalogoService.estadosCotizacion(),
    }).subscribe({
      next: ({ clientes, vendedores, estados }) => {
        this.clientes.set(clientes);
        this.vendedores.set(vendedores);
        this.estados.set(estados);
      },
      error: () => this.notifications.error('No se pudieron cargar los filtros de cotizaciones.'),
    });
  }

  loadCotizaciones(): void {
    this.isLoading.set(true);
    this.service
      .getCotizaciones(this.buildFilters())
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: (items) => this.cotizaciones.set(items),
        error: () => this.notifications.error('No se pudieron cargar las cotizaciones.'),
      });
  }

  clearFilters(): void {
    this.filtersForm.reset({
      search: '',
      idCliente: 'ALL',
      idVendedor: 'ALL',
      idEstadoCotizacion: 'ALL',
      fechaInicio: '',
      fechaFin: '',
    });
    this.loadCotizaciones();
  }

  generatePdf(item: CotizacionResponse): void {
    this.pdfLoadingId.set(item.idCotizacion);
    this.service
      .generarPdf(item.idCotizacion)
      .pipe(finalize(() => this.pdfLoadingId.set(null)))
      .subscribe({
        next: () => {
          this.notifications.success('PDF generado correctamente.');
          this.loadCotizaciones();
        },
        error: () => this.notifications.error('No se pudo generar el PDF.'),
      });
  }

  downloadPdf(item: CotizacionResponse): void {
    this.pdfLoadingId.set(item.idCotizacion);
    this.service
      .descargarPdf(item.idCotizacion)
      .pipe(finalize(() => this.pdfLoadingId.set(null)))
      .subscribe({
        next: (blob) => this.downloads.save(blob, this.pdfFileName(item)),
        error: () => this.notifications.error('No se pudo descargar el PDF.'),
      });
  }

  formatMoney(value: number, moneda: string): string {
    const symbol = this.normalize(moneda).includes('dolar') ? 'US$' : 'S/';
    return `${symbol} ${Number(value ?? 0).toLocaleString('es-PE', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  }

  formatDate(value?: string): string {
    if (!value) {
      return '-';
    }
    return new Date(value).toLocaleDateString('es-PE');
  }

  clientLabel(item: ClienteV1): string {
    return `${item.ruc} - ${item.razonSocial}`;
  }

  userLabel(user: UsuarioResponse): string {
    return [user.nombres, user.apellidoPaterno, user.apellidoMaterno].filter(Boolean).join(' ');
  }

  private buildFilters(): CotizacionFilter {
    const raw = this.filtersForm.getRawValue();
    return {
      search: raw.search || null,
      idCliente: this.numberFilter(raw.idCliente),
      idVendedor: this.numberFilter(raw.idVendedor),
      idEstadoCotizacion: this.numberFilter(raw.idEstadoCotizacion),
      fechaInicio: raw.fechaInicio || null,
      fechaFin: raw.fechaFin || null,
    };
  }

  private numberFilter(value: string | number | null | undefined): number | null {
    if (value === null || value === undefined || value === '' || value === 'ALL') {
      return null;
    }
    return Number(value);
  }

  private pdfFileName(item: CotizacionResponse): string {
    return `COTIZACION-C001-${String(item.idCotizacion).padStart(6, '0')}.pdf`;
  }

  private normalize(value: string): string {
    return (value ?? '').trim().toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');
  }
}

import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { catchError, debounceTime, distinctUntilChanged, finalize, forkJoin, map, of, Subject, takeUntil } from 'rxjs';
import { ClienteCreateRequest, ClienteFilter, ClienteResponseVm, ClienteUpdateRequest } from '../../core/models/cliente.models';
import { ContactoClienteResponseVm } from '../../core/models/contacto-cliente.models';
import { ClienteService } from '../../core/services/cliente.service';
import { ContactoClienteService } from '../../core/services/contacto-cliente.service';
import { NotificationService } from '../../core/services/notification.service';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { MaterialModule } from '../../shared/material/material.module';
import { ClienteDetailDialogComponent } from './components/cliente-detail-dialog/cliente-detail-dialog.component';
import { ClienteFormDialogComponent } from './components/cliente-form-dialog/cliente-form-dialog.component';
import { ConfirmStatusDialogComponent } from './components/confirm-status-dialog/confirm-status-dialog.component';

@Component({
  selector: 'app-clientes',
  imports: [ReactiveFormsModule, MaterialModule, PageHeaderComponent, EmptyStateComponent ],
  templateUrl: './clientes.component.html',
  styleUrl: './clientes.component.scss',
})
export class ClientesComponent implements OnInit, OnDestroy {
  readonly clienteService = inject(ClienteService);
  private readonly contactoService = inject(ContactoClienteService);
  private readonly notifications = inject(NotificationService);
  private readonly dialog = inject(MatDialog);
  private readonly fb = inject(FormBuilder);
  private readonly destroy$ = new Subject<void>();

  readonly clientes = signal<ClienteResponseVm[]>([]);
  readonly filteredClientes = signal<ClienteResponseVm[]>([]);
  readonly pageItems = signal<ClienteResponseVm[]>([]);

  readonly isLoading = signal(false);
  readonly isSaving = signal(false);
  readonly hasError = signal(false);

  readonly pageSizeOptions = [5, 10, 20, 50];
  readonly pageSize = signal(10);
  readonly pageIndex = signal(0);

  readonly displayedColumns = ['ruc', 'razonSocial', 'tipoCliente', 'ubicacion', 'contacto', 'estado', 'acciones'];

  readonly filtersForm = this.fb.nonNullable.group({
    search: [''],
    estado: ['ALL'],
    tipoCliente: [''],
    zona: [''],
    departamento: [''],
    provincia: [''],
    distrito: [''],
    conCotizaciones: ['ALL'],
    conContactos: ['ALL'],
  });

  readonly uniqueTipos = computed(() =>
    Array.from(new Set(this.clientes().map((item) => item.tipoCliente).filter((value): value is string => !!value))).sort(),
  );

  readonly uniqueDepartamentos = computed(() =>
    Array.from(new Set(this.clientes().map((item) => item.departamento).filter((value): value is string => !!value))).sort(),
  );

  ngOnInit(): void {
    this.loadClientes();
    this.filtersForm.controls.search.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe(() => this.applyFilters());

    this.filtersForm.valueChanges.pipe(debounceTime(150), takeUntil(this.destroy$)).subscribe(() => this.applyFilters());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.updatePage();
  }

  clearFilters(): void {
    this.filtersForm.setValue({
      search: '',
      estado: 'ALL',
      tipoCliente: '',
      zona: '',
      departamento: '',
      provincia: '',
      distrito: '',
      conCotizaciones: 'ALL',
      conContactos: 'ALL',
    });
  }

  openCreateCliente(): void {
    const dialogRef = this.dialog.open(ClienteFormDialogComponent, {
      width: '1020px',
      maxWidth: '95vw',
      data: { mode: 'create' },
    });

    dialogRef.afterClosed().subscribe((payload: ClienteCreateRequest | undefined) => {
      if (!payload) {
        return;
      }
      this.isSaving.set(true);
      this.clienteService
        .createCliente(payload)
        .pipe(finalize(() => this.isSaving.set(false)))
        .subscribe({
          next: () => {
            this.notifications.success('Cliente registrado correctamente.');
            this.loadClientes();
          },
          error: (error) => this.handleMutationError(error),
        });
    });
  }

  openEditCliente(cliente: ClienteResponseVm): void {
    const dialogRef = this.dialog.open(ClienteFormDialogComponent, {
      width: '1020px',
      maxWidth: '95vw',
      data: { mode: 'edit', cliente },
    });

    dialogRef.afterClosed().subscribe((payload: ClienteUpdateRequest | undefined) => {
      if (!payload) {
        return;
      }
      this.isSaving.set(true);
      this.clienteService
        .updateCliente(cliente.id, payload)
        .pipe(finalize(() => this.isSaving.set(false)))
        .subscribe({
          next: () => {
            this.notifications.success('Cliente actualizado correctamente.');
            this.loadClientes();
          },
          error: (error) => this.handleMutationError(error),
        });
    });
  }

  openDetalleCliente(cliente: ClienteResponseVm): void {
    const dialogRef = this.dialog.open(ClienteDetailDialogComponent, {
      width: '1200px',
      maxWidth: '96vw',
      data: cliente,
    });

    dialogRef.afterClosed().subscribe((updated) => {
      if (updated) {
        this.loadClientes();
      }
    });
  }

  toggleClienteStatus(cliente: ClienteResponseVm): void {
    const active = this.clienteService.isActive(cliente);
    const nextStatusId = active ? 2 : 1;
    const nextLabel = active ? 'Inactivo' : 'Activo';

    const dialogRef = this.dialog.open(ConfirmStatusDialogComponent, {
      width: '520px',
      maxWidth: '95vw',
      data: {
        title: 'Confirmar cambio de estado',
        message: `Se cambiara el estado del cliente ${cliente.razonSocial} a ${nextLabel}.`,
      },
    });

    dialogRef.afterClosed().subscribe((confirmed) => {
      if (!confirmed) {
        return;
      }

      this.isSaving.set(true);
      this.clienteService
        .changeClienteStatus(cliente.id, nextStatusId)
        .pipe(finalize(() => this.isSaving.set(false)))
        .subscribe({
          next: () => {
            this.notifications.success('El estado del cliente fue actualizado correctamente.');
            this.loadClientes();
          },
          error: (error) => this.handleMutationError(error),
        });
    });
  }

  getStatusLabel(cliente: ClienteResponseVm): string {
    return this.clienteService.getStatusLabel(cliente);
  }

  private loadClientes(): void {
    this.isLoading.set(true);
    this.hasError.set(false);

    this.clienteService
      .getClientes()
      .pipe(
        catchError(() => {
          this.hasError.set(true);
          return of([] as ClienteResponseVm[]);
        }),
      )
      .subscribe((clientes) => {
        if (!clientes.length) {
          this.clientes.set([]);
          this.filteredClientes.set([]);
          this.pageItems.set([]);
          this.isLoading.set(false);
          return;
        }

        forkJoin(
          clientes.map((cliente) =>
            this.contactoService.getContactosByCliente(cliente.id).pipe(
              catchError(() => of([] as ContactoClienteResponseVm[])),
              map(mapContacts(cliente)),
            ),
          ),
        )
          .pipe(finalize(() => this.isLoading.set(false)))
          .subscribe((enriched) => {
            this.clientes.set(enriched);
            this.applyFilters();
          });
      });
  }

  private applyFilters(): void {
    const raw = this.filtersForm.getRawValue();
    const filter: ClienteFilter = {
      search: raw.search ?? '',
      estado: (raw.estado as ClienteFilter['estado']) ?? 'ALL',
      tipoCliente: raw.tipoCliente ?? '',
      zona: raw.zona ?? '',
      departamento: raw.departamento ?? '',
      provincia: raw.provincia ?? '',
      distrito: raw.distrito ?? '',
      conCotizaciones: (raw.conCotizaciones as ClienteFilter['conCotizaciones']) ?? 'ALL',
      conContactos: (raw.conContactos as ClienteFilter['conContactos']) ?? 'ALL',
      page: this.pageIndex(),
      size: this.pageSize(),
      sort: 'razonSocial',
      direction: 'asc',
    };

    const filtered = this.clienteService.applyLocalSearch(this.clientes(), filter.search, filter);
    this.filteredClientes.set(filtered);
    this.pageIndex.set(0);
    this.updatePage();
  }

  private updatePage(): void {
    const start = this.pageIndex() * this.pageSize();
    const end = start + this.pageSize();
    this.pageItems.set(this.filteredClientes().slice(start, end));
  }

  private handleMutationError(error: HttpErrorResponse): void {
    if (error.status === 400) {
      this.notifications.error('Hay errores de validacion en los datos del cliente.');
      return;
    }
    if (error.status === 404) {
      this.notifications.error('El cliente solicitado no fue encontrado.');
      return;
    }
    if (error.status === 409) {
      this.notifications.error('Conflicto de datos. Revisa si el RUC ya existe.');
      return;
    }
    this.notifications.error(error.error?.message ?? 'No se pudo completar la operacion con clientes.');
  }
}

function mapContacts(cliente: ClienteResponseVm) {
  return (contactos: ContactoClienteResponseVm[]): ClienteResponseVm => {
    const principal = contactos.find((item) => item.principal) ?? contactos[0];
    return {
      ...cliente,
      cantidadContactos: contactos.length,
      telefonoPrincipal: principal?.telefono ?? cliente.telefonoPrincipal,
      correoPrincipal: principal?.correo ?? cliente.correoPrincipal,
    };
  };
}



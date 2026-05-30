import { Component, OnInit, inject, signal } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { finalize } from 'rxjs';
import { ClienteResponseVm } from '../../../../core/models/cliente.models';
import {
  ContactoClienteCreateRequest,
  ContactoClienteResponseVm,
  ContactoClienteUpdateRequest,
} from '../../../../core/models/contacto-cliente.models';
import { ContactoClienteService } from '../../../../core/services/contacto-cliente.service';
import { NotificationService } from '../../../../core/services/notification.service';
import { MaterialModule } from '../../../../shared/material/material.module';
import { ContactoFormDialogComponent } from '../contacto-form-dialog/contacto-form-dialog.component';

@Component({
  selector: 'app-cliente-detail-dialog',
  imports: [MaterialModule],
  templateUrl: './cliente-detail-dialog.component.html',
  styleUrl: './cliente-detail-dialog.component.scss',
})
export class ClienteDetailDialogComponent implements OnInit {
  readonly cliente = inject<ClienteResponseVm>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject(MatDialogRef<ClienteDetailDialogComponent>);
  private readonly contactoService = inject(ContactoClienteService);
  private readonly notifications = inject(NotificationService);
  private readonly dialog = inject(MatDialog);

  readonly contactos = signal<ContactoClienteResponseVm[]>([]);
  readonly filteredContactos = signal<ContactoClienteResponseVm[]>([]);
  readonly contactSearch = signal('');
  readonly contactStatus = signal<'ALL' | 'ACTIVE' | 'INACTIVE'>('ALL');
  readonly loading = signal(false);

  ngOnInit(): void {
    this.loadContactos();
  }

  close(updated = false): void {
    this.dialogRef.close(updated);
  }

  setSearch(value: string): void {
    this.contactSearch.set(value);
    this.applyContactFilters();
  }

  setStatus(value: 'ALL' | 'ACTIVE' | 'INACTIVE'): void {
    this.contactStatus.set(value);
    this.applyContactFilters();
  }

  openCreateContact(): void {
    const dialogRef = this.dialog.open(ContactoFormDialogComponent, {
      width: '900px',
      maxWidth: '95vw',
      data: { mode: 'create', clienteId: this.cliente.id },
    });

    dialogRef.afterClosed().subscribe((payload: ContactoClienteCreateRequest | undefined) => {
      if (!payload) {
        return;
      }
      this.loading.set(true);
      this.contactoService
        .createContacto(payload)
        .pipe(finalize(() => this.loading.set(false)))
        .subscribe({
          next: () => {
            this.notifications.success('Contacto registrado correctamente.');
            this.loadContactos();
          },
          error: () => this.notifications.error('No se pudo registrar el contacto.'),
        });
    });
  }

  openEditContact(contacto: ContactoClienteResponseVm): void {
    const dialogRef = this.dialog.open(ContactoFormDialogComponent, {
      width: '900px',
      maxWidth: '95vw',
      data: { mode: 'edit', clienteId: this.cliente.id, contacto },
    });

    dialogRef.afterClosed().subscribe((payload: ContactoClienteUpdateRequest | undefined) => {
      if (!payload) {
        return;
      }
      this.loading.set(true);
      this.contactoService
        .updateContacto(contacto.id, payload)
        .pipe(finalize(() => this.loading.set(false)))
        .subscribe({
          next: () => {
            this.notifications.success('Contacto actualizado correctamente.');
            this.loadContactos();
          },
          error: () => this.notifications.error('No se pudo actualizar el contacto.'),
        });
    });
  }

  setPrincipal(contacto: ContactoClienteResponseVm): void {
    this.notifications.error(`El backend v1 aun no expone la accion para marcar a ${contacto.nombreCompleto} como principal.`);
  }

  toggleContactoStatus(contacto: ContactoClienteResponseVm): void {
    const nextEstado = (contacto.estado?.id ?? 1) === 1 ? 2 : 1;
    this.loading.set(true);
    this.contactoService
      .changeContactoStatus(this.cliente.id, contacto.id, nextEstado)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: () => {
          this.notifications.success('Estado de contacto actualizado correctamente.');
          this.loadContactos();
        },
        error: () => this.notifications.error('No se pudo actualizar el estado del contacto.'),
      });
  }

  getStatusLabel(contacto: ContactoClienteResponseVm): string {
    const value = contacto.estado?.nombre?.trim();
    return value || ((contacto.estado?.id ?? 1) === 1 ? 'Activo' : 'Inactivo');
  }

  private loadContactos(): void {
    this.loading.set(true);
    this.contactoService
      .getContactosByCliente(this.cliente.id)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (items) => {
          this.contactos.set(items);
          this.applyContactFilters();
        },
        error: () => {
          this.contactos.set([]);
          this.filteredContactos.set([]);
        },
      });
  }

  private applyContactFilters(): void {
    const search = this.contactSearch().trim().toLowerCase();
    const status = this.contactStatus();
    const filtered = this.contactos().filter((item) => {
      const searchable = [item.nombreCompleto, item.correo ?? '', item.telefono ?? '', item.whatsapp ?? '', item.cargo ?? '']
        .join(' ')
        .toLowerCase();
      if (search && !searchable.includes(search)) {
        return false;
      }
      if (status === 'ACTIVE' && (item.estado?.id ?? 1) !== 1) {
        return false;
      }
      if (status === 'INACTIVE' && (item.estado?.id ?? 1) === 1) {
        return false;
      }
      return true;
    });
    this.filteredContactos.set(filtered);
  }
}

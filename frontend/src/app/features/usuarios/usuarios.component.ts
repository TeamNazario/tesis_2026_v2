import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { finalize, forkJoin } from 'rxjs';
import { UsuarioRequest, UsuarioResponse } from '../../core/auth/models/auth.models';
import { CatalogoItem } from '../../core/models/v1.models';
import { CatalogoV1Service } from '../../core/services/catalogo-v1.service';
import { DomainApiService } from '../../core/services/domain-api.service';
import { NotificationService } from '../../core/services/notification.service';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { MaterialModule } from '../../shared/material/material.module';
import { UsuarioFormDialogComponent } from './components/usuario-form-dialog/usuario-form-dialog.component';

@Component({
  selector: 'app-usuarios',
  imports: [ReactiveFormsModule, MaterialModule, PageHeaderComponent, EmptyStateComponent],
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.scss',
})
export class UsuariosComponent implements OnInit {
  private readonly api = inject(DomainApiService);
  private readonly catalogos = inject(CatalogoV1Service);
  private readonly notifications = inject(NotificationService);
  private readonly dialog = inject(MatDialog);
  private readonly fb = inject(FormBuilder);

  readonly usuarios = signal<UsuarioResponse[]>([]);
  readonly perfiles = signal<CatalogoItem[]>([]);
  readonly tiposDocumento = signal<CatalogoItem[]>([]);
  readonly estadosUsuario = signal<CatalogoItem[]>([]);
  readonly isLoading = signal(false);
  readonly hasError = signal(false);
  readonly displayedColumns = ['usuario', 'documento', 'perfil', 'estado', 'intentos', 'acciones'];

  readonly filtersForm = this.fb.nonNullable.group({
    search: [''],
    perfil: ['ALL'],
    estado: ['ALL'],
  });
  readonly filters = signal(this.filtersForm.getRawValue());

  readonly filteredUsuarios = computed(() => {
    const raw = this.filters();
    const search = raw.search.trim().toLowerCase();
    return this.usuarios().filter((item) => {
      const searchable = [
        item.nombres,
        item.apellidoPaterno,
        item.apellidoMaterno ?? '',
        item.correo,
        item.nroDocumento,
        item.celular ?? '',
      ].join(' ').toLowerCase();
      if (search && !searchable.includes(search)) {
        return false;
      }
      if (raw.perfil !== 'ALL' && item.perfil?.id !== Number(raw.perfil)) {
        return false;
      }
      if (raw.estado !== 'ALL' && item.estado?.id !== Number(raw.estado)) {
        return false;
      }
      return true;
    });
  });

  readonly activeCount = computed(() => this.usuarios().filter((item) => this.isActive(item)).length);
  readonly blockedCount = computed(() => this.usuarios().filter((item) => this.isBlocked(item)).length);

  ngOnInit(): void {
    this.filtersForm.valueChanges.subscribe(() => this.filters.set(this.filtersForm.getRawValue()));
    this.loadInitialData();
  }

  clearFilters(): void {
    this.filtersForm.setValue({ search: '', perfil: 'ALL', estado: 'ALL' });
  }

  openCreateUsuario(): void {
    this.openUsuarioDialog('create');
  }

  openEditUsuario(usuario: UsuarioResponse): void {
    this.openUsuarioDialog('edit', usuario);
  }

  getFullName(usuario: UsuarioResponse): string {
    return [usuario.nombres, usuario.apellidoPaterno, usuario.apellidoMaterno].filter(Boolean).join(' ');
  }

  getInitials(usuario: UsuarioResponse): string {
    return [usuario.nombres, usuario.apellidoPaterno]
      .filter(Boolean)
      .map((value) => value.charAt(0).toUpperCase())
      .join('');
  }

  getStatusClass(usuario: UsuarioResponse): string {
    if (this.isBlocked(usuario)) {
      return 'blocked';
    }
    return this.isActive(usuario) ? 'active' : 'inactive';
  }

  getStatusLabel(usuario: UsuarioResponse): string {
    if (usuario.estado?.id === 1) {
      return 'Habilitado';
    }
    if (usuario.estado?.id === 2) {
      return 'Inhabilitado';
    }
    if (usuario.estado?.id === 3) {
      return 'Bloqueado';
    }
    return usuario.estado?.nombre || 'Sin estado';
  }

  private loadInitialData(): void {
    this.isLoading.set(true);
    this.hasError.set(false);
    forkJoin({
      usuarios: this.api.getUsuarios(),
      perfiles: this.catalogos.perfiles(),
      tiposDocumento: this.catalogos.tiposDocumento(),
      estadosUsuario: this.catalogos.estadosUsuario(),
    })
      .pipe(finalize(() => this.isLoading.set(false)))
      .subscribe({
        next: ({ usuarios, perfiles, tiposDocumento, estadosUsuario }) => {
          this.usuarios.set(usuarios);
          this.perfiles.set(perfiles);
          this.tiposDocumento.set(tiposDocumento);
          this.estadosUsuario.set(estadosUsuario);
        },
        error: () => {
          this.hasError.set(true);
          this.notifications.error('No se pudo cargar el modulo de usuarios.');
        },
      });
  }

  private openUsuarioDialog(mode: 'create' | 'edit', usuario?: UsuarioResponse): void {
    const dialogRef = this.dialog.open(UsuarioFormDialogComponent, {
      width: '820px',
      maxWidth: '96vw',
      data: {
        mode,
        usuario,
        perfiles: this.perfiles(),
        tiposDocumento: this.tiposDocumento(),
        estadosUsuario: this.estadosUsuario(),
      },
    });

    dialogRef.afterClosed().subscribe((request: UsuarioRequest | undefined) => {
      if (!request) {
        return;
      }
      this.saveUsuario(mode, request, usuario);
    });
  }

  private saveUsuario(mode: 'create' | 'edit', request: UsuarioRequest, usuario?: UsuarioResponse): void {
    this.isLoading.set(true);
    const operation = mode === 'create' || !usuario
      ? this.api.createUsuario(request)
      : this.api.updateUsuario(usuario.idUsuario, request);
    operation.pipe(finalize(() => this.isLoading.set(false))).subscribe({
      next: () => {
        this.notifications.success(mode === 'create' ? 'Usuario creado correctamente.' : 'Usuario actualizado correctamente.');
        this.loadInitialData();
      },
      error: () => this.notifications.error(mode === 'create' ? 'No se pudo crear el usuario.' : 'No se pudo actualizar el usuario.'),
    });
  }

  private isActive(usuario: UsuarioResponse): boolean {
    const label = (usuario.estado?.nombre ?? '').toLowerCase();
    return usuario.estado?.id === 1 || label.includes('habilitado') || label.includes('activo');
  }

  private isBlocked(usuario: UsuarioResponse): boolean {
    const label = (usuario.estado?.nombre ?? '').toLowerCase();
    return usuario.estado?.id === 3 || label.includes('bloqueado');
  }
}

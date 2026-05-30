import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UsuarioResponse } from '../../core/auth/models/auth.models';
import { DomainApiService } from '../../core/services/domain-api.service';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { StatusChipComponent } from '../../shared/components/status-chip/status-chip.component';
import { MaterialModule } from '../../shared/material/material.module';

@Component({
  selector: 'app-usuarios',
  imports: [ReactiveFormsModule, MaterialModule, PageHeaderComponent, EmptyStateComponent, StatusChipComponent],
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.scss',
})
export class UsuariosComponent {
  private readonly api = inject(DomainApiService);
  private readonly fb = inject(FormBuilder);
  readonly usuarios = signal<UsuarioResponse[]>([]);
  readonly displayedColumns = ['nombre', 'correo', 'perfil', 'estado'];

  readonly form = this.fb.nonNullable.group({
    nombres: ['', Validators.required],
    apellidoPaterno: ['', Validators.required],
    nroDocumento: ['', [Validators.required, Validators.maxLength(20)]],
    correo: ['', [Validators.required, Validators.email]],
    celular: [''],
    idPerfil: [2, Validators.required],
    idEstado: [1, Validators.required],
  });

  constructor() {
    this.api.getUsuarios().subscribe({ next: (users) => this.usuarios.set(users) });
  }
}

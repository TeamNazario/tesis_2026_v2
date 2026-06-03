import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AuthService } from '../../../../core/auth/services/auth.service';
import { UsuarioRequest, UsuarioResponse } from '../../../../core/auth/models/auth.models';
import { CatalogoItem } from '../../../../core/models/v1.models';
import { MaterialModule } from '../../../../shared/material/material.module';

interface UsuarioFormDialogData {
  mode: 'create' | 'edit';
  usuario?: UsuarioResponse;
  perfiles: CatalogoItem[];
  tiposDocumento: CatalogoItem[];
  estadosUsuario: CatalogoItem[];
}

@Component({
  selector: 'app-usuario-form-dialog',
  imports: [ReactiveFormsModule, MaterialModule],
  templateUrl: './usuario-form-dialog.component.html',
  styleUrl: './usuario-form-dialog.component.scss',
})
export class UsuarioFormDialogComponent {
  private readonly fb = inject(FormBuilder);
  private readonly dialogRef = inject(MatDialogRef<UsuarioFormDialogComponent>);
  private readonly authService = inject(AuthService);
  readonly data = inject<UsuarioFormDialogData>(MAT_DIALOG_DATA);
  readonly isCreateMode = this.data.mode === 'create';

  readonly form = this.fb.nonNullable.group({
    idPerfil: [this.data.usuario?.perfil?.id ?? this.data.perfiles[0]?.id ?? 0, [Validators.required, Validators.min(1)]],
    idTipoDoc: [this.data.usuario?.tipoDocumento?.id ?? this.data.tiposDocumento[0]?.id ?? 0, [Validators.required, Validators.min(1)]],
    nroDocumento: [this.data.usuario?.nroDocumento ?? '', [Validators.required, Validators.maxLength(20)]],
    nombres: [this.data.usuario?.nombres ?? '', [Validators.required, Validators.maxLength(100)]],
    apellidoPaterno: [this.data.usuario?.apellidoPaterno ?? '', [Validators.required, Validators.maxLength(100)]],
    apellidoMaterno: [this.data.usuario?.apellidoMaterno ?? '', [Validators.maxLength(100)]],
    correo: [this.data.usuario?.correo ?? '', [Validators.required, Validators.email, Validators.maxLength(150)]],
    celular: [this.data.usuario?.celular ?? '', [Validators.maxLength(20)]],
    password: ['', this.isCreateMode ? [Validators.required, Validators.minLength(8), Validators.maxLength(72)] : [Validators.minLength(8), Validators.maxLength(72)]],
    idEstado: [this.data.usuario?.estado?.id ?? this.data.estadosUsuario[0]?.id ?? 1, [Validators.required, Validators.min(1)]],
  });

  close(): void {
    this.dialogRef.close();
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const raw = this.form.getRawValue();
    const usuarioLogeado = this.authService.currentUser()?.correo?.trim() || null;
    const request: UsuarioRequest = {
      idPerfil: raw.idPerfil,
      idTipoDoc: raw.idTipoDoc,
      nroDocumento: raw.nroDocumento.trim(),
      nombres: raw.nombres.trim(),
      apellidoPaterno: raw.apellidoPaterno.trim(),
      apellidoMaterno: raw.apellidoMaterno.trim() || undefined,
      correo: raw.correo.trim(),
      celular: raw.celular.trim() || undefined,
      passwordHash: raw.password.trim() || undefined,
      intentosFallidos: this.data.usuario?.intentosFallidos ?? 0,
      idEstado: raw.idEstado,
      usuarioRegistro: this.isCreateMode ? usuarioLogeado : this.data.usuario?.usuarioRegistro ?? usuarioLogeado,
      usuarioActualiza: this.isCreateMode ? null : usuarioLogeado,
    };
    this.dialogRef.close(request);
  }
}

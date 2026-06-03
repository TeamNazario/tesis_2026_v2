import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { forkJoin } from 'rxjs';
import {
  ContactoClienteCreateRequest,
  ContactoClienteResponseVm,
  ContactoClienteUpdateRequest,
} from '../../../../core/models/contacto-cliente.models';
import { CatalogoItem } from '../../../../core/models/v1.models';
import { CatalogoV1Service } from '../../../../core/services/catalogo-v1.service';
import { MaterialModule } from '../../../../shared/material/material.module';

interface ContactoFormDialogData {
  mode: 'create' | 'edit';
  clienteId: number;
  contacto?: ContactoClienteResponseVm;
}

@Component({
  selector: 'app-contacto-form-dialog',
  imports: [ReactiveFormsModule, MaterialModule],
  templateUrl: './contacto-form-dialog.component.html',
  styleUrl: './contacto-form-dialog.component.scss',
})
export class ContactoFormDialogComponent {
  readonly data = inject<ContactoFormDialogData>(MAT_DIALOG_DATA);
  private readonly fb = inject(FormBuilder);
  private readonly dialogRef = inject(MatDialogRef<ContactoFormDialogComponent>);
  private readonly catalogos = inject(CatalogoV1Service);

  readonly tiposDocumento = signal<CatalogoItem[]>([]);
  readonly estadosClienteContacto = signal<CatalogoItem[]>([]);

  readonly form = this.fb.nonNullable.group({
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    apellidoPaterno: ['', [Validators.required, Validators.maxLength(100)]],
    apellidoMaterno: ['', [Validators.maxLength(100)]],
    tipoDocumentoId: [1, [Validators.required]],
    nroDocumento: ['', [Validators.required, Validators.maxLength(20)]],
    celular: ['', [Validators.pattern(/^\+?\d{6,15}$/)]],
    correo: ['', [Validators.email]],
    idEstadoClienteContacto: [1, [Validators.required]],
  });

  constructor() {
    this.loadCatalogos();

    if (this.data.contacto) {
      this.form.patchValue({
        nombre: this.data.contacto.nombres,
        apellidoPaterno: this.data.contacto.apellidos.split(' ')[0] ?? '',
        apellidoMaterno: this.data.contacto.apellidos.split(' ').slice(1).join(' '),
        tipoDocumentoId: this.data.contacto.tipoDocumentoId ?? 1,
        nroDocumento: this.data.contacto.nroDocumento ?? '',
        celular: this.data.contacto.telefono ?? this.data.contacto.whatsapp ?? '',
        correo: this.data.contacto.correo ?? '',
        idEstadoClienteContacto: this.data.contacto.estado?.id ?? 1,
      });
    }
  }

  get title(): string {
    return this.data.mode === 'create' ? 'Agregar contacto' : 'Actualizar contacto';
  }

  submit(): void {
    this.form.markAllAsTouched();
    const raw = this.form.getRawValue();
    if (!this.validateDocumento(raw.tipoDocumentoId, raw.nroDocumento)) {
      return;
    }
    if (!raw.celular && !raw.correo) {
      this.form.controls.celular.setErrors({ requiredChannel: true });
      return;
    }
    if (this.form.invalid) {
      return;
    }

    const payload: ContactoClienteCreateRequest | ContactoClienteUpdateRequest = {
      clienteId: this.data.clienteId,
      tipoDocumentoId: raw.tipoDocumentoId,
      nroDocumento: raw.nroDocumento,
      nombre: raw.nombre,
      apellidoPaterno: raw.apellidoPaterno,
      apellidoMaterno: raw.apellidoMaterno,
      celular: raw.celular,
      correo: raw.correo,
      idEstadoClienteContacto: raw.idEstadoClienteContacto,
    };
    this.dialogRef.close(payload);
  }

  cancel(): void {
    this.dialogRef.close();
  }

  private loadCatalogos(): void {
    forkJoin({
      tiposDocumento: this.catalogos.tiposDocumento(),
      estadosClienteContacto: this.catalogos.estadosClienteContacto(),
    }).subscribe(({ tiposDocumento, estadosClienteContacto }) => {
      this.tiposDocumento.set(tiposDocumento);
      this.estadosClienteContacto.set(estadosClienteContacto);
      if (!this.form.controls.tipoDocumentoId.value && tiposDocumento[0]) {
        this.form.controls.tipoDocumentoId.setValue(tiposDocumento[0].id);
      }
      if (!this.form.controls.idEstadoClienteContacto.value && estadosClienteContacto[0]) {
        this.form.controls.idEstadoClienteContacto.setValue(estadosClienteContacto[0].id);
      }
    });
  }

  private validateDocumento(tipoDocumentoId: number, nroDocumento: string): boolean {
    const tipoDocumento = this.tiposDocumento()
      .find((item) => item.id === tipoDocumentoId)
      ?.descripcion.toUpperCase();
    const normalized = nroDocumento.trim();

    if (tipoDocumento?.includes('DNI') && !/^\d{8}$/.test(normalized)) {
      this.form.controls.nroDocumento.setErrors({ dniLength: true });
      return false;
    }
    if (tipoDocumento?.includes('RUC') && !/^\d{11}$/.test(normalized)) {
      this.form.controls.nroDocumento.setErrors({ rucLength: true });
      return false;
    }
    return true;
  }
}

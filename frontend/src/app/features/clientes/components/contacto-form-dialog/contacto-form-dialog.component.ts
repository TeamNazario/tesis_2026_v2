import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {
  ContactoClienteCreateRequest,
  ContactoClienteResponseVm,
  ContactoClienteUpdateRequest,
} from '../../../../core/models/contacto-cliente.models';
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

  readonly form = this.fb.nonNullable.group({
    nombres: ['', [Validators.required, Validators.maxLength(100)]],
    apellidos: ['', [Validators.required, Validators.maxLength(200)]],
    tipoDocumentoId: [1, [Validators.required]],
    nroDocumento: ['', [Validators.required, Validators.maxLength(20)]],
    telefono: ['', [Validators.pattern(/^\+?\d{6,15}$/)]],
    whatsapp: ['', [Validators.pattern(/^\+?\d{6,15}$/)]],
    correo: ['', [Validators.email]],
    principal: [false],
    recibeCotizaciones: [true],
    recibeNotificaciones: [true],
    estadoId: [1, [Validators.required]],
    observaciones: [''],
  });

  constructor() {
    if (this.data.contacto) {
      this.form.patchValue({
        nombres: this.data.contacto.nombres,
        apellidos: this.data.contacto.apellidos,
        tipoDocumentoId: this.data.contacto.tipoDocumentoId ?? 1,
        nroDocumento: this.data.contacto.nroDocumento ?? '',
        telefono: this.data.contacto.telefono ?? '',
        whatsapp: this.data.contacto.whatsapp ?? '',
        correo: this.data.contacto.correo ?? '',
        principal: this.data.contacto.principal,
        recibeCotizaciones: this.data.contacto.recibeCotizaciones,
        recibeNotificaciones: this.data.contacto.recibeNotificaciones,
        estadoId: this.data.contacto.estado?.id ?? 1,
      });
    }
  }

  get title(): string {
    return this.data.mode === 'create' ? 'Agregar contacto' : 'Actualizar contacto';
  }

  submit(): void {
    this.form.markAllAsTouched();
    const raw = this.form.getRawValue();
    if (!raw.telefono && !raw.whatsapp && !raw.correo) {
      this.form.controls.telefono.setErrors({ requiredChannel: true });
      return;
    }
    if (this.form.invalid) {
      return;
    }

    const payload: ContactoClienteCreateRequest | ContactoClienteUpdateRequest = {
      clienteId: this.data.clienteId,
      tipoDocumentoId: raw.tipoDocumentoId,
      nroDocumento: raw.nroDocumento,
      nombres: raw.nombres,
      apellidos: raw.apellidos,
      telefono: raw.telefono,
      whatsapp: raw.whatsapp,
      correo: raw.correo,
      principal: raw.principal,
      recibeCotizaciones: raw.recibeCotizaciones,
      recibeNotificaciones: raw.recibeNotificaciones,
      estadoId: raw.estadoId,
      observaciones: raw.observaciones,
    };
    this.dialogRef.close(payload);
  }

  cancel(): void {
    this.dialogRef.close();
  }
}

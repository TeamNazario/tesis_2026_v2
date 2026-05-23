import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ProductCreateRequest, ProductResponse, ProductUpdateRequest } from '../../../../core/models/product.models';
import { MaterialModule } from '../../../../shared/material/material.module';

interface ProductFormDialogData {
  mode: 'create' | 'edit';
  product?: ProductResponse;
}

@Component({
  selector: 'app-producto-form-dialog',
  imports: [ReactiveFormsModule, MaterialModule],
  templateUrl: './producto-form-dialog.component.html',
  styleUrl: './producto-form-dialog.component.scss',
})
export class ProductoFormDialogComponent {
  readonly data = inject<ProductFormDialogData>(MAT_DIALOG_DATA);
  private readonly fb = inject(FormBuilder);
  private readonly dialogRef = inject(MatDialogRef<ProductoFormDialogComponent>);

  readonly form = this.fb.nonNullable.group({
    nombre: ['', [Validators.required, Validators.maxLength(150)]],
    descripcion: ['', [Validators.maxLength(500)]],
    presentacion: ['', [Validators.required, Validators.maxLength(30)]],
    unidadMedida: ['', [Validators.required, Validators.maxLength(20)]],
    precioBase: [0, [Validators.required, Validators.min(0)]],
    stockFisico: [0, [Validators.required, Validators.min(0)]],
    stockReservado: [0, [Validators.required, Validators.min(0)]],
    stockSeguridad: [0, [Validators.required, Validators.min(0)]],
    concentracionUreaAus32: [32.5, [Validators.required, Validators.min(0)]],
    estadoId: [1, [Validators.required]],
  });

  constructor() {
    if (this.data.product) {
      this.form.patchValue({
        nombre: this.data.product.nombre,
        descripcion: this.data.product.descripcion ?? '',
        presentacion: this.data.product.presentacion ?? '',
        unidadMedida: this.data.product.unidadMedida,
        precioBase: this.data.product.precioBase,
        stockFisico: this.data.product.stockFisico,
        stockReservado: this.data.product.stockReservado,
        stockSeguridad: this.data.product.stockSeguridad,
        concentracionUreaAus32: this.data.product.concentracionUreaAus32 ?? 32.5,
        estadoId: this.data.product.estado?.id ?? 1,
      });
    }
  }

  get dialogTitle(): string {
    return this.data.mode === 'create' ? 'Nuevo producto' : 'Actualizar producto';
  }

  save(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid) {
      return;
    }

    const value = this.form.getRawValue();
    if (value.stockReservado > value.stockFisico) {
      this.form.controls.stockReservado.setErrors({ stockRange: true });
      return;
    }

    const payload: ProductCreateRequest | ProductUpdateRequest = {
      nombre: value.nombre,
      descripcion: value.descripcion,
      presentacion: value.presentacion,
      unidadMedida: value.unidadMedida,
      precioBase: value.precioBase,
      stockFisico: value.stockFisico,
      stockReservado: value.stockReservado,
      stockSeguridad: value.stockSeguridad,
      concentracionUreaAus32: value.concentracionUreaAus32,
      estadoId: value.estadoId,
    };

    this.dialogRef.close(payload);
  }

  cancel(): void {
    this.dialogRef.close();
  }
}

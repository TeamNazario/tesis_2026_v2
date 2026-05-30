import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CatalogoItem } from '../../../../core/models/v1.models';
import { CatalogoV1Service } from '../../../../core/services/catalogo-v1.service';
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
  private readonly catalogos = inject(CatalogoV1Service);
  readonly estadosProducto = signal<CatalogoItem[]>([]);

  readonly form = this.fb.nonNullable.group({
    nombre: ['', [Validators.required, Validators.maxLength(150)]],
    descripcion: ['', [Validators.maxLength(500)]],
    presentacion: ['', [Validators.required, Validators.maxLength(30)]],
    unidadMedida: ['', [Validators.required, Validators.maxLength(20)]],
    peso: [0, [Validators.required, Validators.min(0)]],
    volumen: [0, [Validators.required, Validators.min(0)]],
    stockFisico: [0, [Validators.required, Validators.min(0)]],
    stockReservado: [0, [Validators.required, Validators.min(0)]],
    stockMinimo: [0, [Validators.required, Validators.min(0)]],
    cantMinVenta: [1, [Validators.required, Validators.min(1)]],
    idEstadoProducto: [1, [Validators.required]],
  });

  constructor() {
    this.loadEstadosProducto();

    if (this.data.product) {
      this.form.patchValue({
        nombre: this.data.product.nombre,
        descripcion: this.data.product.descripcion ?? '',
        presentacion: this.data.product.presentacion ?? '',
        unidadMedida: this.data.product.unidadMedida,
        peso: this.data.product.peso,
        volumen: this.data.product.volumen,
        stockFisico: this.data.product.stockFisico,
        stockReservado: this.data.product.stockReservado,
        stockMinimo: this.data.product.stockMinimo,
        cantMinVenta: this.data.product.cantMinVenta,
        idEstadoProducto: this.data.product.estado?.id ?? 1,
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
      peso: value.peso,
      volumen: value.volumen,
      stockFisico: value.stockFisico,
      stockReservado: value.stockReservado,
      stockMinimo: value.stockMinimo,
      cantMinVenta: value.cantMinVenta,
      idEstadoProducto: value.idEstadoProducto,
    };

    this.dialogRef.close(payload);
  }

  cancel(): void {
    this.dialogRef.close();
  }

  private loadEstadosProducto(): void {
    this.catalogos.estadosProducto().subscribe((items) => {
      this.estadosProducto.set(items);
      if (!this.form.controls.idEstadoProducto.value && items[0]) {
        this.form.controls.idEstadoProducto.setValue(items[0].id);
      }
    });
  }
}

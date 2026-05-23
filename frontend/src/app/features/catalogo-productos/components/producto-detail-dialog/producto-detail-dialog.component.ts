import { DecimalPipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ProductResponse } from '../../../../core/models/product.models';
import { MaterialModule } from '../../../../shared/material/material.module';

@Component({
  selector: 'app-producto-detail-dialog',
  imports: [MaterialModule, DecimalPipe],
  templateUrl: './producto-detail-dialog.component.html',
  styleUrl: './producto-detail-dialog.component.scss',
})
export class ProductoDetailDialogComponent {
  readonly data = inject<ProductResponse>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject(MatDialogRef<ProductoDetailDialogComponent>);

  close(): void {
    this.dialogRef.close();
  }
}

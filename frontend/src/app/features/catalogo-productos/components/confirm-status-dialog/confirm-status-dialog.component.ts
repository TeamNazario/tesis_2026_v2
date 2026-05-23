import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MaterialModule } from '../../../../shared/material/material.module';

interface ConfirmStatusDialogData {
  productName: string;
  nextStatusLabel: string;
}

@Component({
  selector: 'app-confirm-status-dialog',
  imports: [MaterialModule],
  template: `
    <h2 mat-dialog-title>Confirmar cambio de estado</h2>
    <mat-dialog-content>
      <p>
        Se cambiará el estado de <strong>{{ data.productName }}</strong> a <strong>{{ data.nextStatusLabel }}</strong>.
      </p>
      <p>¿Deseas continuar?</p>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="close(false)">Cancelar</button>
      <button mat-flat-button (click)="close(true)">Confirmar</button>
    </mat-dialog-actions>
  `,
})
export class ConfirmStatusDialogComponent {
  readonly data = inject<ConfirmStatusDialogData>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject(MatDialogRef<ConfirmStatusDialogComponent>);

  close(confirmed: boolean): void {
    this.dialogRef.close(confirmed);
  }
}

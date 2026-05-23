import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MaterialModule } from '../../../../shared/material/material.module';

interface ConfirmStatusData {
  title: string;
  message: string;
}

@Component({
  selector: 'app-confirm-status-dialog',
  imports: [MaterialModule],
  template: `
    <h2 mat-dialog-title>{{ data.title }}</h2>
    <mat-dialog-content>
      <p>{{ data.message }}</p>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="close(false)">Cancelar</button>
      <button mat-flat-button (click)="close(true)">Confirmar</button>
    </mat-dialog-actions>
  `,
})
export class ConfirmStatusDialogComponent {
  readonly data = inject<ConfirmStatusData>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject(MatDialogRef<ConfirmStatusDialogComponent>);

  close(value: boolean): void {
    this.dialogRef.close(value);
  }
}

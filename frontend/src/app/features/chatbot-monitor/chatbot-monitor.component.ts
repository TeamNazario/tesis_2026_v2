import { Component, signal } from '@angular/core';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { StatusChipComponent } from '../../shared/components/status-chip/status-chip.component';
import { MaterialModule } from '../../shared/material/material.module';

@Component({
  selector: 'app-chatbot-monitor',
  imports: [MaterialModule, PageHeaderComponent, EmptyStateComponent, StatusChipComponent],
  template: `
    <app-page-header title="Monitor del chatbot" description="Trazabilidad de intenciones, validacion SUNAT, PDF y tiempos de atencion." />
    <div class="panel">
      @if (logs().length) {
        <table mat-table [dataSource]="logs()">
          <ng-container matColumnDef="session">
            <th mat-header-cell *matHeaderCellDef>Sesion</th>
            <td mat-cell *matCellDef="let item">{{ item.sessionIdWhatsapp }}</td>
          </ng-container>
          <ng-container matColumnDef="ruc">
            <th mat-header-cell *matHeaderCellDef>RUC</th>
            <td mat-cell *matCellDef="let item">{{ item.rucConsultado || '-' }}</td>
          </ng-container>
          <ng-container matColumnDef="intencion">
            <th mat-header-cell *matHeaderCellDef>Intencion</th>
            <td mat-cell *matCellDef="let item">{{ item.intencionDetectada || 'No clasificada' }}</td>
          </ng-container>
          <ng-container matColumnDef="tiempo">
            <th mat-header-cell *matHeaderCellDef>Tiempo</th>
            <td mat-cell *matCellDef="let item">{{ item.tiempoAtencionSegundos || 0 }} s</td>
          </ng-container>
          <ng-container matColumnDef="pdf">
            <th mat-header-cell *matHeaderCellDef>PDF</th>
            <td mat-cell *matCellDef="let item">
              <app-status-chip [label]="item.pdfGeneradoExitosamente ? 'Generado' : 'Pendiente'" />
            </td>
          </ng-container>
          <tr mat-header-row *matHeaderRowDef="columns"></tr>
          <tr mat-row *matRowDef="let row; columns: columns"></tr>
        </table>
      } @else {
        <app-empty-state title="Sin interacciones" description="Los logs de eficiencia apareceran desde /logs-eficiencia-chatbot." />
      }
    </div>
  `,
  styles: `
    .panel {
      overflow-x: auto;
      padding: 1rem;
      border-radius: 1rem;
      background: #fff;
    }
    table {
      width: 100%;
    }
  `,
})
export class ChatbotMonitorComponent {
  readonly logs = signal<
    { sessionIdWhatsapp: string; rucConsultado?: string; intencionDetectada?: string; tiempoAtencionSegundos?: number; pdfGeneradoExitosamente?: boolean }[]
  >([]);
  readonly columns = ['session', 'ruc', 'intencion', 'tiempo', 'pdf'];
}

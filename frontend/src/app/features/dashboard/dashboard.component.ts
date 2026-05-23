import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { DomainApiService } from '../../core/services/domain-api.service';
import { DashboardKpis } from '../../core/models/domain.models';
import { MaterialModule } from '../../shared/material/material.module';
import { EmptyStateComponent } from '../../shared/components/empty-state/empty-state.component';
import { KpiCardComponent } from '../../shared/components/kpi-card/kpi-card.component';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';

@Component({
  selector: 'app-dashboard',
  imports: [ReactiveFormsModule, MaterialModule, PageHeaderComponent, KpiCardComponent, EmptyStateComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  private readonly api = inject(DomainApiService);
  private readonly fb = inject(FormBuilder);
  readonly kpis = signal<DashboardKpis | null>(null);
  readonly error = signal('');

  readonly filters = this.fb.group({
    fechaInicio: [''],
    fechaFin: [''],
    estado: [''],
  });

  constructor() {
    this.load();
  }

  load(): void {
    this.error.set('');
    this.api.getDashboardKpis().subscribe({
      next: (kpis) => this.kpis.set(kpis),
      error: () => this.error.set('No se pudieron cargar los indicadores del dashboard.'),
    });
  }
}

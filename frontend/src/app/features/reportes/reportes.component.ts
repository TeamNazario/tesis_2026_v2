import { Component } from '@angular/core';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { MaterialModule } from '../../shared/material/material.module';

@Component({
  selector: 'app-reportes',
  imports: [MaterialModule, PageHeaderComponent],
  template: `
    <app-page-header title="Reportes" description="Exportaciones preparadas para analisis estadistico de cotizaciones, inventario y eficiencia." />
    <section class="reports">
      <article>
        <mat-icon>request_quote</mat-icon>
        <h2>Cotizaciones</h2>
        <p>Resumen operativo basado en las cotizaciones vigentes del sistema.</p>
      </article>
      <article>
        <mat-icon>business</mat-icon>
        <h2>Clientes</h2>
        <p>Seguimiento de cartera, contactos y estados comerciales.</p>
      </article>
      <article>
        <mat-icon>inventory_2</mat-icon>
        <h2>Inventario</h2>
        <p>Disponibilidad calculada desde productos y stock vigente.</p>
      </article>
    </section>
  `,
  styles: `
    .reports {
      display: grid;
      grid-template-columns: repeat(3, minmax(0, 1fr));
      gap: 1rem;
    }
    article {
      padding: 1.3rem;
      border-radius: 1rem;
      background: #fff;
    }
    mat-icon {
      color: #006e25;
    }
    h2 {
      font-family: Manrope, sans-serif;
      margin: .8rem 0 .4rem;
    }
    p {
      color: var(--bio-on-surface-variant);
    }
    @media (max-width: 900px) {
      .reports {
        grid-template-columns: 1fr;
      }
    }
  `,
})
export class ReportesComponent {}

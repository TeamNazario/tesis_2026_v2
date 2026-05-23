import { Component } from '@angular/core';
import { PageHeaderComponent } from '../../shared/components/page-header/page-header.component';
import { MaterialModule } from '../../shared/material/material.module';

@Component({
  selector: 'app-configuracion',
  imports: [MaterialModule, PageHeaderComponent],
  template: `
    <app-page-header title="Configuracion" description="Parametros visuales y operativos preparados para futuras reglas de negocio." />
    <section class="panel">
      <h2>Reglas reflejadas en UI</h2>
      <mat-list>
        <mat-list-item>RUC obligatorio para brindar precios o generar cotizaciones.</mat-list-item>
        <mat-list-item>Zona de despacho requerida antes de precio.</mat-list-item>
        <mat-list-item>Vigencia de cotizacion: 24 horas.</mat-list-item>
        <mat-list-item>Stock disponible = stock fisico - stock reservado.</mat-list-item>
      </mat-list>
    </section>
  `,
  styles: `
    .panel {
      padding: 1.3rem;
      border-radius: 1rem;
      background: #fff;
    }
    h2 {
      margin: 0 0 1rem;
      font-family: Manrope, sans-serif;
    }
  `,
})
export class ConfiguracionComponent {}

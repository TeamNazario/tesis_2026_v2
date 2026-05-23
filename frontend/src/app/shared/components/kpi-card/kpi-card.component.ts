import { Component, input } from '@angular/core';
import { MaterialModule } from '../../material/material.module';

@Component({
  selector: 'app-kpi-card',
  imports: [MaterialModule],
  template: `
    <article class="kpi-card">
      <div class="icon"><mat-icon>{{ icon() }}</mat-icon></div>
      <span>{{ label() }}</span>
      <strong>{{ value() }}</strong>
      <small>{{ detail() }}</small>
    </article>
  `,
  styles: `
    .kpi-card {
      min-height: 150px;
      padding: 1.25rem;
      border-radius: 1rem;
      background: linear-gradient(145deg, #ffffff, #eff6e9);
      display: grid;
      gap: .45rem;
    }
    .icon {
      width: 2.6rem;
      height: 2.6rem;
      border-radius: .8rem;
      display: grid;
      place-items: center;
      color: #006e25;
      background: #dff4df;
    }
    span, small {
      color: var(--bio-on-surface-variant);
    }
    strong {
      font-family: Manrope, sans-serif;
      font-size: 2rem;
      color: var(--bio-on-surface);
    }
  `,
})
export class KpiCardComponent {
  label = input.required<string>();
  value = input.required<string | number>();
  detail = input('');
  icon = input('analytics');
}

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
      border: 1px solid var(--bf-line);
      border-radius: var(--bf-radius);
      background: linear-gradient(145deg, #ffffff, #f4fbff);
      box-shadow: var(--bf-shadow-soft);
      display: grid;
      gap: .45rem;
    }
    .icon {
      width: 2.6rem;
      height: 2.6rem;
      border-radius: var(--bf-radius);
      display: grid;
      place-items: center;
      color: var(--bf-blue-strong);
      background: var(--bf-blue-soft);
    }
    span, small {
      color: var(--bio-on-surface-variant);
    }
    strong {
      font-family: var(--bf-font-title);
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

import { Component, input } from '@angular/core';
import { MaterialModule } from '../../material/material.module';

@Component({
  selector: 'app-empty-state',
  imports: [MaterialModule],
  template: `
    <section class="empty-state">
      <mat-icon>{{ icon() }}</mat-icon>
      <h3>{{ title() }}</h3>
      <p>{{ description() }}</p>
    </section>
  `,
  styles: `
    .empty-state {
      padding: 2rem;
      border: 1px dashed var(--bf-line);
      border-radius: var(--bf-radius);
      background: var(--bio-surface-lowest);
      text-align: center;
      color: var(--bio-on-surface-variant);
    }
    mat-icon {
      width: 3rem;
      height: 3rem;
      font-size: 3rem;
      color: var(--bf-blue);
    }
    h3 {
      margin: .75rem 0 .25rem;
      font-family: var(--bf-font-title);
      color: var(--bio-on-surface);
    }
    p {
      margin: 0;
    }
  `,
})
export class EmptyStateComponent {
  icon = input('inbox');
  title = input('Sin datos disponibles');
  description = input('Cuando el backend entregue registros, apareceran aqui.');
}

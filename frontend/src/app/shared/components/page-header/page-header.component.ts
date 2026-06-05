import { Component, input } from '@angular/core';

@Component({
  selector: 'app-page-header',
  imports: [],
  template: `
    <header class="page-header">
      <div>
        <span class="eyebrow">{{ eyebrow() }}</span>
        <h1>{{ title() }}</h1>
        <p>{{ description() }}</p>
      </div>
      <ng-content />
    </header>
  `,
  styles: `
    .page-header {
      display: flex;
      align-items: flex-end;
      justify-content: space-between;
      gap: 1.5rem;
      margin-bottom: 1.5rem;
    }
    .eyebrow {
      color: var(--bf-blue);
      font-weight: 800;
      text-transform: uppercase;
      font-size: .75rem;
    }
    h1 {
      margin: .2rem 0;
      font-family: var(--bf-font-title);
      font-size: clamp(1.8rem, 4vw, 2.7rem);
      color: var(--bio-on-surface);
      letter-spacing: 0;
    }
    p {
      margin: 0;
      max-width: 760px;
      color: var(--bio-on-surface-variant);
    }
    @media (max-width: 720px) {
      .page-header {
        align-items: flex-start;
        flex-direction: column;
      }
    }
  `,
})
export class PageHeaderComponent {
  eyebrow = input('BIOFLUID D32');
  title = input.required<string>();
  description = input('');
}

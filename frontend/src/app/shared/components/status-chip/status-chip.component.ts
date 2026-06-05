import { Component, computed, input } from '@angular/core';

@Component({
  selector: 'app-status-chip',
  imports: [],
  template: `<span class="chip" [class]="tone()">{{ label() }}</span>`,
  styles: `
    .chip {
      display: inline-flex;
      align-items: center;
      min-height: 1.8rem;
      padding: .2rem .7rem;
      border-radius: 999px;
      font-size: .78rem;
      font-weight: 800;
      background: #f2f7fa;
      color: var(--bf-muted);
    }
    .ok {
      background: var(--bf-success-soft);
      color: var(--bf-green-strong);
    }
    .warn {
      background: var(--bf-warn-soft);
      color: var(--bf-warn);
    }
    .danger {
      background: var(--bf-danger-soft);
      color: var(--bf-danger);
    }
  `,
})
export class StatusChipComponent {
  label = input.required<string>();
  tone = computed(() => {
    const value = this.label().toUpperCase();
    if (value.includes('APROBAD') || value.includes('CONFIRM') || value.includes('ACTIVO') || value.includes('OK')) return 'ok';
    if (value.includes('VENC') || value.includes('RECHAZ') || value.includes('BAJO') || value.includes('ERROR')) return 'danger';
    if (value.includes('ANUL')) return 'warn';
    return value.includes('PEND') || value.includes('PRELIM') ? 'warn' : '';
  });
}

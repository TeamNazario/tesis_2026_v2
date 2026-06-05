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
      background: #eff6e9;
      color: #3e4a3c;
    }
    .ok {
      background: #dff4df;
      color: #006e25;
    }
    .warn {
      background: #fff2cc;
      color: #6b5200;
    }
    .danger {
      background: #ffe1e8;
      color: #ab2d57;
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

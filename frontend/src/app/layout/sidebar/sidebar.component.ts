import { Component, computed, inject, output } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { PermissionService } from '../../core/auth/services/permission.service';
import { MaterialModule } from '../../shared/material/material.module';

interface NavItem {
  label: string;
  icon: string;
  route: string;
}

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, RouterLinkActive, MaterialModule],
  template: `
    <aside class="sidebar">
      <div class="brand">
        <div class="mark">B32</div>
        <div>
          <strong>BIOFLUID D32 E.I.R.L.</strong>
        </div>
      </div>

      <nav>
        @for (item of navItems(); track item.route) {
          <a [routerLink]="item.route" routerLinkActive="active" (click)="navigate.emit()">
            <mat-icon>{{ item.icon }}</mat-icon>
            <span>{{ item.label }}</span>
          </a>
        }
      </nav>
    </aside>
  `,
  styleUrl: './sidebar.component.scss',
})
export class SidebarComponent {
  private readonly permissions = inject(PermissionService);
  navigate = output<void>();
  private readonly allItems: NavItem[] = [
    { label: 'Dashboard', icon: 'space_dashboard', route: '/dashboard' },
    { label: 'Clientes', icon: 'business', route: '/clientes' },
    { label: 'Usuarios', icon: 'group', route: '/usuarios' },
    { label: 'Productos', icon: 'science', route: '/catalogo-productos' },
    { label: 'Precios por Tipo de Cliente', icon: 'price_change', route: '/precios-tipo-cliente' },
    { label: 'Cotizaciones', icon: 'request_quote', route: '/cotizaciones' },
    { label: 'Reportes', icon: 'bar_chart', route: '/reportes' },
  ];

  readonly navItems = computed(() =>
    this.allItems.filter((item) => item.route !== '/usuarios' || this.permissions.canAccessUsers()),
  );
}

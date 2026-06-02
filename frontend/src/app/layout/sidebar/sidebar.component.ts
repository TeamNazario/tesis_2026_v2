import { Component, output } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
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
          <strong>BIOFLUID D32</strong>
          <span>Precision Flow</span>
        </div>
      </div>

      <nav>
        @for (item of navItems; track item.route) {
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
  navigate = output<void>();
  navItems: NavItem[] = [
    { label: 'Dashboard', icon: 'space_dashboard', route: '/dashboard' },
    { label: 'Clientes', icon: 'business', route: '/clientes' },
    { label: 'Usuarios', icon: 'group', route: '/usuarios' },
    { label: 'Inventario', icon: 'inventory_2', route: '/inventario' },
    { label: 'Productos', icon: 'science', route: '/catalogo-productos' },
    { label: 'Cotizaciones', icon: 'request_quote', route: '/cotizaciones' },
    { label: 'Reportes', icon: 'bar_chart', route: '/reportes' },
    { label: 'Configuracion', icon: 'tune', route: '/configuracion' },
  ];
}

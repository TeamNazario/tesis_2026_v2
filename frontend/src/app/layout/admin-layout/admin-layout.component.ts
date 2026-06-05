import { BreakpointObserver } from '@angular/cdk/layout';
import { Component, ViewChild, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatSidenav } from '@angular/material/sidenav';
import { MaterialModule } from '../../shared/material/material.module';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { TopbarComponent } from '../topbar/topbar.component';

@Component({
  selector: 'app-admin-layout',
  imports: [RouterOutlet, MaterialModule, SidebarComponent, TopbarComponent],
  template: `
    <mat-sidenav-container class="shell">
      <mat-sidenav #drawer [mode]="isMobile() ? 'over' : 'side'" [opened]="!isMobile()" class="drawer">
        <app-sidebar (navigate)="isMobile() && drawer.close()" />
      </mat-sidenav>
      <mat-sidenav-content>
        <app-topbar (menuClick)="drawer.toggle()" />
        <main class="content">
          <router-outlet />
        </main>
      </mat-sidenav-content>
    </mat-sidenav-container>
  `,
  styles: `
    .shell {
      min-height: 100vh;
      background:
        linear-gradient(135deg, rgba(0, 119, 182, .07), transparent 32%),
        var(--bf-bg);
    }
    .drawer {
      width: 280px;
      border-right: 0;
      box-shadow: 14px 0 40px rgba(23, 32, 42, .08);
    }
    .content {
      width: min(1440px, calc(100% - 2rem));
      margin: 0 auto;
      padding: 2rem 0 3rem;
    }
  `,
})
export class AdminLayoutComponent {
  @ViewChild('drawer') drawer?: MatSidenav;
  private readonly breakpoint = inject(BreakpointObserver);
  readonly isMobile = signal(false);

  constructor() {
    this.breakpoint.observe('(max-width: 900px)').subscribe((state) => this.isMobile.set(state.matches));
  }
}

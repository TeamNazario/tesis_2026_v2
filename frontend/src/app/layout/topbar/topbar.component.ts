import { Component, inject, output } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/auth/services/auth.service';
import { LoadingService } from '../../core/services/loading.service';
import { MaterialModule } from '../../shared/material/material.module';

@Component({
  selector: 'app-topbar',
  imports: [MaterialModule],
  template: `
    <mat-toolbar class="topbar">
      <button mat-icon-button class="menu-button" (click)="menuClick.emit()" aria-label="Abrir menu">
        <mat-icon>menu</mat-icon>
      </button>
      <div class="context">
        <span>Sistema administrativo</span>
        <strong>Urea automotriz AUS-32</strong>
      </div>
      <span class="spacer"></span>
      @if (loading.isLoading()) {
        <mat-spinner diameter="24" />
      }
      <button mat-button [matMenuTriggerFor]="profileMenu">
        <mat-icon>account_circle</mat-icon>
        {{ auth.currentUser()?.nombres || 'Usuario' }}
      </button>
      <mat-menu #profileMenu="matMenu">
        <button mat-menu-item (click)="logout()">
          <mat-icon>logout</mat-icon>
          Cerrar sesion
        </button>
      </mat-menu>
    </mat-toolbar>
  `,
  styles: `
    .topbar {
      position: sticky;
      top: 0;
      z-index: 10;
      background: rgba(245, 249, 251, .92);
      backdrop-filter: blur(16px);
      color: var(--bio-on-surface);
      border-bottom: 1px solid var(--bf-line);
    }
    .context {
      display: grid;
      line-height: 1.1;
    }
    .context span {
      font-size: .75rem;
      color: var(--bio-on-surface-variant);
    }
    .context strong {
      font-family: var(--bf-font-title);
      font-size: 1rem;
    }
    .spacer {
      flex: 1;
    }
    .menu-button {
      display: none;
    }
    @media (max-width: 900px) {
      .menu-button {
        display: inline-flex;
      }
    }
  `,
})
export class TopbarComponent {
  menuClick = output<void>();
  readonly auth = inject(AuthService);
  readonly loading = inject(LoadingService);
  private readonly router = inject(Router);

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}

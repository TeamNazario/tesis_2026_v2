import { Routes } from '@angular/router';
import { authGuard } from './core/auth/guards/auth.guard';
import { AdminLayoutComponent } from './layout/admin-layout/admin-layout.component';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: '',
    component: AdminLayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      {
        path: 'dashboard',
        loadComponent: () => import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent),
      },
      {
        path: 'clientes',
        loadComponent: () => import('./features/clientes/clientes.component').then((m) => m.ClientesComponent),
      },
      {
        path: 'usuarios',
        loadComponent: () => import('./features/usuarios/usuarios.component').then((m) => m.UsuariosComponent),
      },
      {
        path: 'catalogo-productos',
        loadComponent: () =>
          import('./features/catalogo-productos/catalogo-productos.component').then((m) => m.CatalogoProductosComponent),
      },
      {
        path: 'precios-tipo-cliente',
        loadChildren: () =>
          import('./features/precios-tipo-cliente/precios-tipo-cliente.routes').then((m) => m.PRECIO_TIPO_CLIENTE_ROUTES),
      },
      {
        path: 'cotizaciones',
        loadChildren: () => import('./features/cotizaciones/cotizaciones.routes').then((m) => m.COTIZACIONES_ROUTES),
      },
      {
        path: 'reportes',
        loadComponent: () => import('./features/reportes/reportes.component').then((m) => m.ReportesComponent),
      },
      {
        path: 'configuracion',
        loadComponent: () =>
          import('./features/configuracion/configuracion.component').then((m) => m.ConfiguracionComponent),
      },
    ],
  },
  { path: '**', redirectTo: 'dashboard' },
];

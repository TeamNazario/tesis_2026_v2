import { Routes } from '@angular/router';

export const COTIZACIONES_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/cotizacion-list/cotizacion-list.component').then((m) => m.CotizacionListComponent),
  },
  {
    path: 'nueva',
    loadComponent: () => import('./pages/cotizacion-form/cotizacion-form.component').then((m) => m.CotizacionFormComponent),
  },
  {
    path: ':id',
    loadComponent: () => import('./pages/cotizacion-detail/cotizacion-detail.component').then((m) => m.CotizacionDetailComponent),
  },
];

import { Routes } from '@angular/router';
import { roleGuard } from '../../core/auth/guards/role.guard';

export const PRECIO_TIPO_CLIENTE_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./pages/precio-tipo-cliente-list/precio-tipo-cliente-list.component').then((m) => m.PrecioTipoClienteListComponent),
  },
  {
    path: 'nuevo',
    canActivate: [roleGuard(['SISTEMAS', 'JEFE DE VENTAS', 'ADMINISTRATIVO'])],
    loadComponent: () =>
      import('./pages/precio-tipo-cliente-form/precio-tipo-cliente-form.component').then((m) => m.PrecioTipoClienteFormComponent),
    data: { mode: 'create' },
  },
  {
    path: ':id/editar',
    canActivate: [roleGuard(['SISTEMAS', 'JEFE DE VENTAS', 'ADMINISTRATIVO'])],
    loadComponent: () =>
      import('./pages/precio-tipo-cliente-form/precio-tipo-cliente-form.component').then((m) => m.PrecioTipoClienteFormComponent),
    data: { mode: 'edit' },
  },
  {
    path: ':id',
    loadComponent: () =>
      import('./pages/precio-tipo-cliente-detail/precio-tipo-cliente-detail.component').then(
        (m) => m.PrecioTipoClienteDetailComponent,
      ),
  },
];

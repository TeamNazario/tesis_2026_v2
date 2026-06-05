import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { PermissionService } from '../services/permission.service';

export const roleGuard = (roles: string[]): CanActivateFn => () => {
  const permissions = inject(PermissionService);
  const router = inject(Router);
  return permissions.hasAnyRole(roles) ? true : router.createUrlTree(['/dashboard']);
};

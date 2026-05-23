import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { NotificationService } from '../../services/notification.service';
import { TokenService } from '../services/token.service';

export const errorInterceptor: HttpInterceptorFn = (request, next) => {
  const router = inject(Router);
  const tokenService = inject(TokenService);
  const notifications = inject(NotificationService);

  return next(request).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        tokenService.clear();
        router.navigate(['/login']);
        notifications.error(error.error?.message ?? 'Sesion expirada o credenciales invalidas.');
      } else if (error.status === 403) {
        notifications.error('No tienes permisos para acceder a este recurso.');
      } else if (error.status >= 500) {
        notifications.error('El servidor no pudo procesar la solicitud.');
      }
      return throwError(() => error);
    }),
  );
};

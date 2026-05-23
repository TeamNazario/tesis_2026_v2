import { Injectable, inject, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { API_ENDPOINTS } from '../../constants/api-endpoints';
import { ApiService } from '../../services/api.service';
import { AuthResponse, LoginRequest, UsuarioResponse } from '../models/auth.models';
import { TokenService } from './token.service';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly api = inject(ApiService);
  private readonly tokenService = inject(TokenService);
  readonly currentUser = signal<UsuarioResponse | null>(this.tokenService.getUser());

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.api.post<AuthResponse>(API_ENDPOINTS.auth.login, request).pipe(
      tap((response) => {
        this.tokenService.saveSession(response);
        this.currentUser.set(response.usuario);
      }),
    );
  }

  me(): Observable<UsuarioResponse> {
    return this.api.get<UsuarioResponse>(API_ENDPOINTS.auth.me).pipe(tap((user) => this.currentUser.set(user)));
  }

  logout(): void {
    this.tokenService.clear();
    this.currentUser.set(null);
  }

  isAuthenticated(): boolean {
    return this.tokenService.isAuthenticated();
  }

  hasRole(roles: string[]): boolean {
    const role = this.currentUser()?.perfil?.nombre?.toUpperCase();
    return !roles.length || roles.some((allowed) => role?.includes(allowed.toUpperCase()));
  }
}

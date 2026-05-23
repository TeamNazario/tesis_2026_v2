import { Injectable } from '@angular/core';
import { AuthResponse, UsuarioResponse } from '../models/auth.models';

const TOKEN_KEY = 'biofluid_access_token';
const USER_KEY = 'biofluid_user';
const EXPIRES_AT_KEY = 'biofluid_expires_at';

@Injectable({ providedIn: 'root' })
export class TokenService {
  saveSession(response: AuthResponse): void {
    localStorage.setItem(TOKEN_KEY, response.accessToken);
    localStorage.setItem(USER_KEY, JSON.stringify(response.usuario));
    localStorage.setItem(EXPIRES_AT_KEY, String(Date.now() + response.expiresInSeconds * 1000));
  }

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  getUser(): UsuarioResponse | null {
    const raw = localStorage.getItem(USER_KEY);
    return raw ? (JSON.parse(raw) as UsuarioResponse) : null;
  }

  isAuthenticated(): boolean {
    const expiresAt = Number(localStorage.getItem(EXPIRES_AT_KEY) ?? 0);
    return Boolean(this.getToken()) && expiresAt > Date.now();
  }

  clear(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    localStorage.removeItem(EXPIRES_AT_KEY);
  }
}

import { ReferenceResponse } from '../../http/reference-response.model';

export interface LoginRequest {
  correo: string;
  password: string;
}

export interface UsuarioResponse {
  idUsuario: number;
  perfil: ReferenceResponse;
  tipoDocumento: ReferenceResponse;
  nroDocumento: string;
  nombres: string;
  apellidoPaterno: string;
  apellidoMaterno?: string;
  correo: string;
  celular?: string;
  intentosFallidos: number;
  estado: ReferenceResponse;
  usuarioRegistro?: string;
  fechaRegistro?: string;
  usuarioActualiza?: string;
  fechaActualiza?: string;
}

export interface AuthResponse {
  tokenType: string;
  accessToken: string;
  expiresInSeconds: number;
  usuario: UsuarioResponse;
}

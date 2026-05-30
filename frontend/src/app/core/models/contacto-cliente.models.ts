import { ReferenceResponse } from '../http/reference-response.model';

export interface ContactoClienteResponseVm {
  id: number;
  clienteId: number;
  nombres: string;
  apellidos: string;
  nombreCompleto: string;
  cargo?: string;
  area?: string;
  telefono?: string;
  whatsapp?: string;
  correo?: string;
  principal: boolean;
  recibeCotizaciones: boolean;
  recibeNotificaciones: boolean;
  estado?: ReferenceResponse;
  fechaCreacion?: string;
  fechaActualizacion?: string;
  tipoDocumentoId?: number;
  tipoDocumento?: string;
  nroDocumento?: string;
  idEstadoClienteContacto?: number;
  estadoClienteContacto?: string;
}

export interface ContactoClienteCreateRequest {
  clienteId: number;
  tipoDocumentoId: number;
  nroDocumento: string;
  nombre: string;
  apellidoPaterno: string;
  apellidoMaterno?: string;
  celular?: string;
  correo?: string;
  idEstadoClienteContacto: number;
}

export interface ContactoClienteUpdateRequest extends ContactoClienteCreateRequest {}

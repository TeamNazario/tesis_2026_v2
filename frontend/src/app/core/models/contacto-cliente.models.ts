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
  nroDocumento?: string;
}

export interface ContactoClienteCreateRequest {
  clienteId: number;
  tipoDocumentoId: number;
  nroDocumento: string;
  nombres: string;
  apellidos: string;
  apellidoMaterno?: string;
  telefono?: string;
  whatsapp?: string;
  correo?: string;
  principal: boolean;
  recibeCotizaciones: boolean;
  recibeNotificaciones: boolean;
  observaciones?: string;
  estadoId: number;
}

export interface ContactoClienteUpdateRequest extends ContactoClienteCreateRequest {}

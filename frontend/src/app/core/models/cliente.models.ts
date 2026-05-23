import { ReferenceResponse } from '../http/reference-response.model';

export interface ClienteResponseVm {
  id: number;
  ruc: string;
  razonSocial: string;
  nombreComercial?: string;
  tipoCliente?: string;
  direccionFiscal?: string;
  zonaDespacho?: string;
  departamento?: string;
  provincia?: string;
  distrito?: string;
  telefonoPrincipal?: string;
  correoPrincipal?: string;
  estado?: ReferenceResponse;
  fechaCreacion?: string;
  fechaActualizacion?: string;
  cantidadContactos: number;
  cantidadCotizaciones: number;
  observaciones?: string;
  condicionSunat?: string;
  estadoSunat?: string;
  ubigeo?: string;
}

export interface ClienteCreateRequest {
  ruc: string;
  razonSocial: string;
  nombreComercial?: string;
  tipoCliente?: string;
  direccionFiscal?: string;
  zonaDespacho?: string;
  departamento?: string;
  provincia?: string;
  distrito?: string;
  telefonoPrincipal?: string;
  correoPrincipal?: string;
  observaciones?: string;
  estadoId: number;
  condicionSunat: string;
  estadoSunat: string;
  ubigeo?: string;
}

export interface ClienteUpdateRequest extends Omit<ClienteCreateRequest, 'ruc'> {
  ruc: string;
}

export interface ClienteFilter {
  search: string;
  estado: 'ALL' | 'ACTIVE' | 'INACTIVE';
  tipoCliente: string;
  zona: string;
  departamento: string;
  provincia: string;
  distrito: string;
  conCotizaciones: 'ALL' | 'YES' | 'NO';
  conContactos: 'ALL' | 'YES' | 'NO';
  page: number;
  size: number;
  sort: 'ruc' | 'razonSocial' | 'departamento' | 'fechaActualizacion';
  direction: 'asc' | 'desc';
}

import { ReferenceResponse } from '../http/reference-response.model';

export interface ProductResponse {
  id: number;
  codigo: string;
  nombre: string;
  descripcion?: string;
  presentacion?: string;
  unidadMedida: string;
  precioBase: number;
  stockFisico: number;
  stockReservado: number;
  stockDisponible: number;
  stockSeguridad: number;
  estado?: ReferenceResponse;
  concentracionUreaAus32?: number;
  fechaCreacion?: string;
  fechaActualizacion?: string;
}

export interface ProductCreateRequest {
  nombre: string;
  descripcion?: string;
  presentacion?: string;
  unidadMedida: string;
  precioBase: number;
  stockFisico: number;
  stockReservado: number;
  stockSeguridad: number;
  concentracionUreaAus32: number;
  estadoId: number;
}

export interface ProductUpdateRequest extends ProductCreateRequest {}

export interface ProductFilter {
  search: string;
  estado: 'ALL' | 'ACTIVE' | 'INACTIVE' | 'BLOCKED';
  presentacion: string;
  unidadMedida: string;
  stockBajo: boolean;
  minPrecio: number | null;
  maxPrecio: number | null;
  page: number;
  size: number;
  sort: 'nombre' | 'precioBase' | 'stockDisponible' | 'fechaActualizacion';
  direction: 'asc' | 'desc';
}

export interface ProductStatusChangeRequest {
  estadoId: number;
}

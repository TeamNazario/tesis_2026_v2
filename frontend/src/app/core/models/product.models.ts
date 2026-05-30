import { ReferenceResponse } from '../http/reference-response.model';

export interface ProductResponse {
  id: number;
  codigo: string;
  nombre: string;
  descripcion?: string;
  presentacion?: string;
  unidadMedida: string;
  peso: number;
  volumen: number;
  stockFisico: number;
  stockReservado: number;
  stockDisponible: number;
  stockMinimo: number;
  cantMinVenta: number;
  estado?: ReferenceResponse;
  fechaCreacion?: string;
  fechaActualizacion?: string;
}

export interface ProductCreateRequest {
  nombre: string;
  descripcion?: string;
  presentacion?: string;
  unidadMedida: string;
  peso: number;
  volumen: number;
  stockFisico: number;
  stockReservado: number;
  stockMinimo: number;
  cantMinVenta: number;
  idEstadoProducto: number;
}

export interface ProductUpdateRequest extends ProductCreateRequest {}

export interface ProductFilter {
  search: string;
  estado: 'ALL' | 'ACTIVE' | 'INACTIVE' | 'BLOCKED';
  presentacion: string;
  unidadMedida: string;
  stockBajo: boolean;
  page: number;
  size: number;
  sort: 'nombre' | 'stockDisponible' | 'fechaActualizacion';
  direction: 'asc' | 'desc';
}

export interface ProductStatusChangeRequest {
  idEstadoProducto: number;
}

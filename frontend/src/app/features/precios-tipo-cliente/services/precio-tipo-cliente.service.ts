import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../../../core/constants/api-endpoints';
import { ApiService } from '../../../core/services/api.service';
import {
  PrecioTipoClienteCreateRequest,
  PrecioTipoClienteFilter,
  PrecioTipoClienteResponse,
  PrecioTipoClienteUpdateRequest,
} from '../models/precio-tipo-cliente.model';

@Injectable({ providedIn: 'root' })
export class PrecioTipoClienteService {
  private readonly api = inject(ApiService);

  getPrecios(filters?: PrecioTipoClienteFilter): Observable<PrecioTipoClienteResponse[]> {
    const serverFilters = this.toServerFilters(filters);
    if (this.hasServerFilters(serverFilters)) {
      return this.buscarPrecios(serverFilters);
    }
    return this.api.get<PrecioTipoClienteResponse[]>(API_ENDPOINTS.v1.preciosTipoCliente);
  }

  getPrecioById(id: number): Observable<PrecioTipoClienteResponse> {
    return this.api.get<PrecioTipoClienteResponse>(`${API_ENDPOINTS.v1.preciosTipoCliente}/${id}`);
  }

  getPreciosByProducto(idProducto: number): Observable<PrecioTipoClienteResponse[]> {
    return this.api.get<PrecioTipoClienteResponse[]>(`${API_ENDPOINTS.v1.preciosTipoCliente}/producto/${idProducto}`);
  }

  buscarPrecios(filters: PrecioTipoClienteFilter): Observable<PrecioTipoClienteResponse[]> {
    return this.api.get<PrecioTipoClienteResponse[]>(
      `${API_ENDPOINTS.v1.preciosTipoCliente}/buscar`,
      this.toServerFilters(filters),
    );
  }

  createPrecio(request: PrecioTipoClienteCreateRequest): Observable<PrecioTipoClienteResponse> {
    return this.api.post<PrecioTipoClienteResponse>(API_ENDPOINTS.v1.preciosTipoCliente, request);
  }

  updatePrecio(id: number, request: PrecioTipoClienteUpdateRequest): Observable<PrecioTipoClienteResponse> {
    return this.api.put<PrecioTipoClienteResponse>(`${API_ENDPOINTS.v1.preciosTipoCliente}/${id}`, request);
  }

  hasDuplicateCombination(
    precios: PrecioTipoClienteResponse[],
    idProducto: number,
    idTipoCliente: number,
    excludeId?: number,
  ): boolean {
    return precios.some(
      (item) =>
        item.idProducto === idProducto &&
        item.idTipoCliente === idTipoCliente &&
        (excludeId === undefined || item.idPrecio !== excludeId),
    );
  }

  private hasServerFilters(filters: PrecioTipoClienteFilter): boolean {
    return filters.idProducto !== null && filters.idProducto !== undefined
      || filters.idTipoCliente !== null && filters.idTipoCliente !== undefined
      || filters.idEstadoProducto !== null && filters.idEstadoProducto !== undefined;
  }

  private toServerFilters(filters?: PrecioTipoClienteFilter): Record<string, string | number | boolean | null | undefined> {
    return {
      idProducto: filters?.idProducto ?? null,
      idTipoCliente: filters?.idTipoCliente ?? null,
      idEstadoProducto: filters?.idEstadoProducto ?? null,
    };
  }
}

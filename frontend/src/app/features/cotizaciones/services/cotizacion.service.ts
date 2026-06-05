import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../../../core/constants/api-endpoints';
import { ApiService, QueryParams } from '../../../core/services/api.service';
import {
  CotizacionCalcularItemRequest,
  CotizacionCalcularItemResponse,
  CotizacionCalcularResumenRequest,
  CotizacionCalcularResumenResponse,
  CotizacionCreateRequest,
  CotizacionEstadoUpdateRequest,
  CotizacionFilter,
  CotizacionPdfResponse,
  CotizacionPrecioProductoResponse,
  CotizacionResponse,
} from '../models/cotizacion.model';

@Injectable({ providedIn: 'root' })
export class CotizacionService {
  private readonly api = inject(ApiService);
  private readonly basePath = API_ENDPOINTS.v1.cotizaciones;

  getCotizaciones(filters?: CotizacionFilter): Observable<CotizacionResponse[]> {
    return this.api.get<CotizacionResponse[]>(this.basePath, this.toQueryParams(filters));
  }

  getCotizacionById(id: number): Observable<CotizacionResponse> {
    return this.api.get<CotizacionResponse>(`${this.basePath}/${id}`);
  }

  createCotizacion(request: CotizacionCreateRequest): Observable<CotizacionResponse> {
    return this.api.post<CotizacionResponse>(this.basePath, request);
  }

  cambiarEstado(id: number, request: CotizacionEstadoUpdateRequest): Observable<CotizacionResponse> {
    return this.api.patch<CotizacionResponse>(`${this.basePath}/${id}/estado`, request);
  }

  obtenerPrecioProducto(idCliente: number, idProducto: number, moneda: string): Observable<CotizacionPrecioProductoResponse> {
    return this.api.get<CotizacionPrecioProductoResponse>(`${this.basePath}/precio-producto`, {
      idCliente,
      idProducto,
      moneda,
    });
  }

  calcularItem(request: CotizacionCalcularItemRequest): Observable<CotizacionCalcularItemResponse> {
    return this.api.post<CotizacionCalcularItemResponse>(`${this.basePath}/calcular-item`, request);
  }

  calcularResumen(request: CotizacionCalcularResumenRequest): Observable<CotizacionCalcularResumenResponse> {
    return this.api.post<CotizacionCalcularResumenResponse>(`${this.basePath}/calcular-resumen`, request);
  }

  generarPdf(id: number): Observable<CotizacionPdfResponse> {
    return this.api.post<CotizacionPdfResponse>(`${this.basePath}/${id}/generar-pdf`, {});
  }

  procesarVencidas(): Observable<number> {
    return this.api.post<number>(`${this.basePath}/procesar-vencidas`, {});
  }

  descargarPdf(id: number): Observable<Blob> {
    return this.api.download(`${this.basePath}/${id}/pdf`);
  }

  private toQueryParams(filters?: CotizacionFilter): QueryParams {
    return {
      search: filters?.search,
      idCliente: filters?.idCliente,
      idVendedor: filters?.idVendedor,
      idEstadoCotizacion: filters?.idEstadoCotizacion,
      fechaInicio: filters?.fechaInicio,
      fechaFin: filters?.fechaFin,
      // The current Spring Boot endpoint filters these fields server-side only.
      // Pagination, sort and moneda stay in the UI until the backend exposes them.
    };
  }
}

import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../../../core/constants/api-endpoints';
import { ApiService, QueryParams } from '../../../core/services/api.service';
import {
  CotizacionPorEstadoResponse,
  CotizacionPorVendedorResponse,
  CotizacionTendenciaResponse,
  DashboardFilter,
  DashboardResumenResponse,
  ProductoStockBajoResponse,
  StockProductoDashboardResponse,
  TopClienteResponse,
  TopProductoResponse,
} from '../models/dashboard.model';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private readonly api = inject(ApiService);
  private readonly basePath = API_ENDPOINTS.v1.dashboard;

  resumen(filters?: DashboardFilter): Observable<DashboardResumenResponse> {
    return this.api.get<DashboardResumenResponse>(`${this.basePath}/resumen`, this.toQueryParams(filters));
  }

  cotizacionesTendencia(filters?: DashboardFilter): Observable<CotizacionTendenciaResponse[]> {
    return this.api.get<CotizacionTendenciaResponse[]>(`${this.basePath}/cotizaciones-tendencia`, this.toQueryParams(filters));
  }

  cotizacionesPorEstado(filters?: DashboardFilter): Observable<CotizacionPorEstadoResponse[]> {
    return this.api.get<CotizacionPorEstadoResponse[]>(`${this.basePath}/cotizaciones-por-estado`, this.toQueryParams(filters));
  }

  topProductos(filters?: DashboardFilter, limit = 8): Observable<TopProductoResponse[]> {
    return this.api.get<TopProductoResponse[]>(`${this.basePath}/top-productos`, {
      ...this.toQueryParams(filters),
      limit,
    });
  }

  topClientes(filters?: DashboardFilter, limit = 8): Observable<TopClienteResponse[]> {
    return this.api.get<TopClienteResponse[]>(`${this.basePath}/top-clientes`, {
      ...this.toQueryParams(filters),
      limit,
    });
  }

  cotizacionesPorVendedor(filters?: DashboardFilter, limit = 8): Observable<CotizacionPorVendedorResponse[]> {
    return this.api.get<CotizacionPorVendedorResponse[]>(`${this.basePath}/cotizaciones-por-vendedor`, {
      ...this.toQueryParams(filters),
      limit,
    });
  }

  stockProductos(filters?: DashboardFilter, limit = 8): Observable<StockProductoDashboardResponse[]> {
    return this.api.get<StockProductoDashboardResponse[]>(`${this.basePath}/stock-productos`, {
      ...this.toQueryParams(filters),
      limit,
    });
  }

  productosStockBajo(filters?: DashboardFilter, limit = 8): Observable<ProductoStockBajoResponse[]> {
    return this.api.get<ProductoStockBajoResponse[]>(`${this.basePath}/productos-stock-bajo`, {
      ...this.toQueryParams(filters),
      limit,
    });
  }

  private toQueryParams(filters?: DashboardFilter): QueryParams {
    return {
      fechaInicio: filters?.fechaInicio,
      fechaFin: filters?.fechaFin,
      idEstadoCotizacion: filters?.idEstadoCotizacion,
      idVendedor: filters?.idVendedor,
      idCliente: filters?.idCliente,
      idProducto: filters?.idProducto,
      idTipoCliente: filters?.idTipoCliente,
      moneda: filters?.moneda,
    };
  }
}

import { Injectable, inject } from '@angular/core';
import { Observable, forkJoin, map } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import { UsuarioResponse } from '../auth/models/auth.models';
import { ReferenceResponse } from '../http/reference-response.model';
import { ApiService, QueryParams } from './api.service';
import {
  ClienteRequest,
  ClienteResponse,
  CotizacionResponse,
  DashboardKpis,
  LogEficienciaChatbotResponse,
  LogInventarioResponse,
  ProductoResponse,
} from '../models/domain.models';

@Injectable({ providedIn: 'root' })
export class DomainApiService {
  private readonly api = inject(ApiService);

  getClientes(params?: QueryParams): Observable<ClienteResponse[]> {
    return this.api.get<ClienteResponse[]>(API_ENDPOINTS.v1.clientes, params);
  }

  createCliente(request: ClienteRequest): Observable<ClienteResponse> {
    return this.api.post<ClienteResponse>(API_ENDPOINTS.v1.clientes, request);
  }

  updateCliente(id: number, request: ClienteRequest): Observable<ClienteResponse> {
    return this.api.put<ClienteResponse>(`${API_ENDPOINTS.v1.clientes}/${id}`, request);
  }

  getUsuarios(): Observable<UsuarioResponse[]> {
    return this.api.get<UsuarioResponse[]>(API_ENDPOINTS.usuarios);
  }

  getProductos(params?: QueryParams): Observable<ProductoResponse[]> {
    return this.api.get<ProductoResponse[]>(API_ENDPOINTS.v1.productos, params);
  }

  getCotizaciones(params?: QueryParams): Observable<CotizacionResponse[]> {
    return this.api.get<CotizacionResponse[]>(API_ENDPOINTS.v1.cotizaciones, params);
  }

  getLogsInventario(params?: QueryParams): Observable<LogInventarioResponse[]> {
    return this.api.get<LogInventarioResponse[]>(API_ENDPOINTS.logsInventario, params);
  }

  getLogsEficienciaChatbot(params?: QueryParams): Observable<LogEficienciaChatbotResponse[]> {
    return this.api.get<LogEficienciaChatbotResponse[]>(API_ENDPOINTS.logsEficienciaChatbot, params);
  }

  getPerfiles(): Observable<ReferenceResponse[]> {
    return this.api.get<ReferenceResponse[]>(API_ENDPOINTS.perfiles);
  }

  getEstados(): Observable<ReferenceResponse[]> {
    return this.api.get<ReferenceResponse[]>(API_ENDPOINTS.estados);
  }

  getDashboardKpis(): Observable<DashboardKpis> {
    // TODO backend: reemplazar por endpoint agregado /dashboard/kpis cuando exista.
    return forkJoin({
      cotizaciones: this.getCotizaciones(),
      productos: this.getProductos(),
      logsChatbot: this.getLogsEficienciaChatbot(),
    }).pipe(
      map(({ cotizaciones, productos, logsChatbot }) => {
        const now = new Date();
        const today = now.toDateString();
        const weekAgo = new Date(now);
        weekAgo.setDate(now.getDate() - 7);
        const month = now.getMonth();
        const confirmed = cotizaciones.filter((item) => item.estadoCotizacion === 'CONFIRMADA');
        const vencidas = cotizaciones.filter((item) => new Date(item.fechaVencimiento) < now);
        const chatbot: CotizacionResponse[] = [];
        const tiempos = logsChatbot
          .map((item) => item.tiempoAtencionSegundos ?? 0)
          .filter((value) => value > 0);
        return {
          cotizacionesHoy: cotizaciones.filter((item) => new Date(item.fechaEmision).toDateString() === today).length,
          cotizacionesSemana: cotizaciones.filter((item) => new Date(item.fechaEmision) >= weekAgo).length,
          cotizacionesMes: cotizaciones.filter((item) => new Date(item.fechaEmision).getMonth() === month).length,
          tiempoPromedioRespuestaSegundos: tiempos.length
            ? Math.round(tiempos.reduce((sum, value) => sum + value, 0) / tiempos.length)
            : 0,
          cotizacionesChatbot: chatbot.length,
          cotizacionesManuales: cotizaciones.length - chatbot.length,
          cotizacionesVencidas: vencidas.length,
          cotizacionesConfirmadas: confirmed.length,
          stockFisico: productos.reduce((sum, item) => sum + (item.stockFisico ?? 0), 0),
          stockReservado: productos.reduce((sum, item) => sum + (item.stockReservado ?? 0), 0),
          stockDisponible: productos.reduce((sum, item) => sum + (item.stockDisponible ?? 0), 0),
          alertasStockBajo: productos.filter((item) => item.stockDisponible <= item.stockMinimo).length,
          pdfsGenerados: logsChatbot.filter((item) => item.pdfGeneradoExitosamente).length,
        };
      }),
    );
  }

  downloadCotizacionPdf(id: number): Observable<Blob> {
    // TODO backend: confirmar endpoint real para PDF.
    return this.api.download(`${API_ENDPOINTS.v1.cotizaciones}/${id}/pdf`);
  }
}

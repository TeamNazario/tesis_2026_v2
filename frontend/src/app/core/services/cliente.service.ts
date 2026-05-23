import { Injectable, inject } from '@angular/core';
import { Observable, map, switchMap } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import { ReferenceResponse } from '../http/reference-response.model';
import { ClienteFilter, ClienteCreateRequest, ClienteResponseVm, ClienteUpdateRequest } from '../models/cliente.models';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class ClienteService {
  private readonly api = inject(ApiService);

  getClientes(): Observable<ClienteResponseVm[]> {
    return this.api.get<ApiClienteResponse[]>(API_ENDPOINTS.clientes).pipe(map((items) => items.map((item) => this.toVm(item))));
  }

  getClienteById(id: number): Observable<ClienteResponseVm> {
    return this.api.get<ApiClienteResponse>(`${API_ENDPOINTS.clientes}/${id}`).pipe(map((item) => this.toVm(item)));
  }

  getClienteByRuc(ruc: string): Observable<ClienteResponseVm> {
    return this.api.get<ApiClienteResponse>(`${API_ENDPOINTS.clientes}/ruc/${ruc}`).pipe(map((item) => this.toVm(item)));
  }

  searchClientes(term: string, filter: ClienteFilter): Observable<ClienteResponseVm[]> {
    return this.getClientes().pipe(map((items) => this.applyLocalSearch(items, term, filter)));
  }

  createCliente(request: ClienteCreateRequest): Observable<ClienteResponseVm> {
    return this.api.post<ApiClienteResponse>(API_ENDPOINTS.clientes, this.toApiRequest(request)).pipe(map((item) => this.toVm(item)));
  }

  updateCliente(id: number, request: ClienteUpdateRequest): Observable<ClienteResponseVm> {
    return this.api
      .put<ApiClienteResponse>(`${API_ENDPOINTS.clientes}/${id}`, this.toApiRequest(request))
      .pipe(map((item) => this.toVm(item)));
  }

  changeClienteStatus(id: number, estadoId: number): Observable<ClienteResponseVm> {
    return this.getClienteById(id).pipe(
      map((cliente) => ({
        ruc: cliente.ruc,
        razonSocial: cliente.razonSocial,
        nombreComercial: cliente.nombreComercial,
        tipoCliente: cliente.tipoCliente,
        direccionFiscal: cliente.direccionFiscal,
        zonaDespacho: cliente.zonaDespacho,
        departamento: cliente.departamento,
        provincia: cliente.provincia,
        distrito: cliente.distrito,
        telefonoPrincipal: cliente.telefonoPrincipal,
        correoPrincipal: cliente.correoPrincipal,
        observaciones: cliente.observaciones,
        estadoId,
        condicionSunat: cliente.condicionSunat ?? 'HABIDO',
        estadoSunat: cliente.estadoSunat ?? 'ACTIVO',
        ubigeo: cliente.ubigeo,
      })),
      switchMap((payload) => this.updateCliente(id, payload)),
    );
  }

  getClienteCotizaciones(_id: number): Observable<never> {
    // TODO backend: exponer endpoint de historial de cotizaciones por cliente.
    throw new Error('TODO: Endpoint de cotizaciones por cliente no disponible en backend.');
  }

  validateRuc(_ruc: string): Observable<never> {
    // TODO backend: exponer endpoint de validacion de RUC.
    throw new Error('TODO: Endpoint de validacion RUC no disponible en backend.');
  }

  deleteCliente(id: number): Observable<void> {
    return this.api.delete<void>(`${API_ENDPOINTS.clientes}/${id}`);
  }

  applyLocalSearch(items: ClienteResponseVm[], term: string, filter: ClienteFilter): ClienteResponseVm[] {
    const normalizedTerm = term.trim().toLowerCase();
    return items.filter((item) => {
      const searchable = [
        item.ruc,
        item.razonSocial,
        item.nombreComercial ?? '',
        item.telefonoPrincipal ?? '',
        item.correoPrincipal ?? '',
        item.departamento ?? '',
      ]
        .join(' ')
        .toLowerCase();

      if (normalizedTerm && !searchable.includes(normalizedTerm)) {
        return false;
      }
      if (filter.estado === 'ACTIVE' && !this.isActive(item)) {
        return false;
      }
      if (filter.estado === 'INACTIVE' && this.isActive(item)) {
        return false;
      }
      if (filter.tipoCliente && (item.tipoCliente ?? '') !== filter.tipoCliente) {
        return false;
      }
      if (filter.zona && (item.zonaDespacho ?? '') !== filter.zona) {
        return false;
      }
      if (filter.departamento && (item.departamento ?? '') !== filter.departamento) {
        return false;
      }
      if (filter.provincia && (item.provincia ?? '') !== filter.provincia) {
        return false;
      }
      if (filter.distrito && (item.distrito ?? '') !== filter.distrito) {
        return false;
      }
      if (filter.conContactos === 'YES' && item.cantidadContactos <= 0) {
        return false;
      }
      if (filter.conContactos === 'NO' && item.cantidadContactos > 0) {
        return false;
      }
      if (filter.conCotizaciones === 'YES' && item.cantidadCotizaciones <= 0) {
        return false;
      }
      if (filter.conCotizaciones === 'NO' && item.cantidadCotizaciones > 0) {
        return false;
      }
      return true;
    });
  }

  isActive(cliente: ClienteResponseVm): boolean {
    const name = (cliente.estado?.nombre ?? '').toLowerCase();
    return cliente.estado?.id === 1 || name.includes('activo') || name.includes('habilitado');
  }

  getStatusLabel(cliente: ClienteResponseVm): string {
    const normalized = (cliente.estado?.nombre ?? '').trim();
    if (normalized) {
      return normalized;
    }
    const byId = new Map<number, string>([
      [1, 'Activo'],
      [2, 'Inactivo'],
      [3, 'Pendiente de validacion'],
    ]);
    return byId.get(cliente.estado?.id ?? 0) ?? 'Sin estado';
  }

  private toVm(item: ApiClienteResponse): ClienteResponseVm {
    return {
      id: item.idCliente,
      ruc: item.ruc,
      razonSocial: item.razonSocial,
      nombreComercial: item.nombreComercial,
      tipoCliente: this.resolveTipoCliente(item),
      direccionFiscal: item.direccion,
      zonaDespacho: undefined,
      departamento: item.departamento,
      provincia: item.provincia,
      distrito: item.distrito,
      telefonoPrincipal: undefined,
      correoPrincipal: undefined,
      estado: item.estado,
      fechaCreacion: item.fechaRegistro,
      fechaActualizacion: item.fechaRegistro,
      cantidadContactos: 0,
      cantidadCotizaciones: 0,
      condicionSunat: item.condicionSunat,
      estadoSunat: item.estadoSunat,
      ubigeo: item.ubigeo,
      observaciones: undefined,
    };
  }

  private toApiRequest(request: ClienteCreateRequest | ClienteUpdateRequest): ApiClienteRequest {
    return {
      ruc: request.ruc,
      razonSocial: request.razonSocial,
      nombreComercial: request.nombreComercial ?? '',
      condicionSunat: request.condicionSunat,
      estadoSunat: request.estadoSunat,
      direccion: request.direccionFiscal ?? '',
      departamento: request.departamento ?? '',
      provincia: request.provincia ?? '',
      distrito: request.distrito ?? '',
      ubigeo: request.ubigeo ?? '',
      idEstado: request.estadoId,
      usuarioRegistro: 'frontend',
    };
  }

  private resolveTipoCliente(item: ApiClienteResponse): string {
    if (item.ruc.startsWith('20')) {
      return 'Empresa';
    }
    if (item.ruc.startsWith('10')) {
      return 'Persona Natural';
    }
    return 'Comercial';
  }
}

interface ApiClienteResponse {
  idCliente: number;
  ruc: string;
  razonSocial: string;
  nombreComercial?: string;
  condicionSunat?: string;
  estadoSunat?: string;
  direccion?: string;
  departamento?: string;
  provincia?: string;
  distrito?: string;
  ubigeo?: string;
  vendedorAsignado?: ReferenceResponse;
  estado?: ReferenceResponse;
  usuarioRegistro?: string;
  fechaRegistro?: string;
}

interface ApiClienteRequest {
  ruc: string;
  razonSocial: string;
  nombreComercial: string;
  condicionSunat: string;
  estadoSunat: string;
  direccion: string;
  departamento: string;
  provincia: string;
  distrito: string;
  ubigeo: string;
  idEstado: number;
  usuarioRegistro: string;
}

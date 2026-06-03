import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { ReferenceResponse } from '../http/reference-response.model';
import { ClienteFilter, ClienteCreateRequest, ClienteResponseVm, ClienteUpdateRequest } from '../models/cliente.models';
import { ClienteV1, ClienteV1CreateRequest, ClienteV1UpdateRequest } from '../models/v1.models';
import { ClienteV1Service } from './cliente-v1.service';

@Injectable({ providedIn: 'root' })
export class ClienteService {
  private readonly clienteV1 = inject(ClienteV1Service);

  getClientes(): Observable<ClienteResponseVm[]> {
    return this.clienteV1.findAll().pipe(map((items) => items.map((item) => this.toVm(item))));
  }

  getClienteById(id: number): Observable<ClienteResponseVm> {
    return this.clienteV1.findById(id).pipe(map((item) => this.toVm(item)));
  }

  getClienteByRuc(ruc: string): Observable<ClienteResponseVm> {
    return this.clienteV1.buscar(ruc, undefined).pipe(map((items) => this.toVm(items[0])));
  }

  searchClientes(term: string, filter: ClienteFilter): Observable<ClienteResponseVm[]> {
    return this.getClientes().pipe(map((items) => this.applyLocalSearch(items, term, filter)));
  }

  createCliente(request: ClienteCreateRequest): Observable<ClienteResponseVm> {
    return this.clienteV1.create(this.toCreateRequest(request)).pipe(map((item) => this.toVm(item)));
  }

  updateCliente(id: number, request: ClienteUpdateRequest): Observable<ClienteResponseVm> {
    return this.clienteV1.update(id, this.toUpdateRequest(request)).pipe(map((item) => this.toVm(item)));
  }

  changeClienteStatus(id: number, idEstadoClienteContacto: number): Observable<ClienteResponseVm> {
    return this.clienteV1.patchEstado(id, idEstadoClienteContacto).pipe(map((item) => this.toVm(item)));
  }

  applyLocalSearch(items: ClienteResponseVm[], term: string, filter: ClienteFilter): ClienteResponseVm[] {
    const normalizedTerm = term.trim().toLowerCase();
    return items.filter((item) => {
      const searchable = [
        item.ruc,
        item.razonSocial,
        item.tipoCliente ?? '',
        item.vendedorAsignado ?? '',
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
      if (filter.estado === 'INACTIVE' && !this.isInactive(item)) {
        return false;
      }
      if (filter.estado === 'BLOCKED' && !this.isBlocked(item)) {
        return false;
      }
      if (filter.tipoCliente && (item.tipoCliente ?? '') !== filter.tipoCliente) {
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
    const statusId = this.getStatusId(cliente);
    if (statusId === 1) {
      return true;
    }
    if (statusId === 2 || statusId === 3) {
      return false;
    }
    const label = this.normalizeStatus(cliente.estado?.nombre);
    if (label) {
      if (label.includes('inactivo') || label.includes('deshabilitado') || label.includes('bloqueado')) {
        return false;
      }
      return label.includes('activo') || label.includes('habilitado');
    }
    return statusId === 1;
  }

  isInactive(cliente: ClienteResponseVm): boolean {
    const statusId = this.getStatusId(cliente);
    if (statusId === 2) {
      return true;
    }
    const label = this.normalizeStatus(cliente.estado?.nombre);
    return label.includes('inactivo') || label.includes('deshabilitado');
  }

  isBlocked(cliente: ClienteResponseVm): boolean {
    const statusId = this.getStatusId(cliente);
    if (statusId === 3) {
      return true;
    }
    return this.normalizeStatus(cliente.estado?.nombre).includes('bloqueado');
  }

  getStatusClass(cliente: ClienteResponseVm): string {
    if (this.isBlocked(cliente)) {
      return 'blocked';
    }
    if (this.isInactive(cliente)) {
      return 'inactive';
    }
    if (this.isActive(cliente)) {
      return 'active';
    }
    return 'unknown';
  }

  getStatusLabel(cliente: ClienteResponseVm): string {
    const statusId = this.getStatusId(cliente);
    if (statusId === 1) {
      return 'Activo';
    }
    if (statusId === 2) {
      return 'Inactivo';
    }
    if (statusId === 3) {
      return 'Bloqueado';
    }
    return cliente.estado?.nombre?.trim() || 'Sin estado';
  }

  private toVm(item: ClienteV1): ClienteResponseVm {
    return {
      id: item.idCliente,
      ruc: item.ruc,
      razonSocial: item.razonSocial,
      nombreComercial: undefined,
      tipoCliente: item.tipoCliente,
      idTipoCliente: item.idTipoCliente,
      vendedorAsignado: item.vendedorAsignado,
      idVendedorAsignado: item.idVendedorAsignado,
      direccionFiscal: item.direccion,
      departamento: item.departamento,
      provincia: item.provincia,
      distrito: item.distrito,
      telefonoPrincipal: undefined,
      correoPrincipal: undefined,
      estado: this.toReference(item.idEstadoClienteContacto, item.estadoClienteContacto),
      fechaCreacion: item.fecRegistro,
      fechaActualizacion: item.fecActualiza ?? item.fecRegistro,
      cantidadContactos: 0,
      cantidadCotizaciones: 0,
      condicionSunat: item.condicionSunat,
      estadoSunat: item.estadoSunat,
      ubigeo: item.ubigeo,
      observaciones: undefined,
      usuRegistro: item.usuRegistro,
      fecRegistro: item.fecRegistro,
      usuActualiza: item.usuActualiza,
      fecActualiza: item.fecActualiza,
    };
  }

  private toCreateRequest(request: ClienteCreateRequest): ClienteV1CreateRequest {
    return {
      ruc: request.ruc,
      razonSocial: request.razonSocial,
      condicionSunat: request.condicionSunat,
      estadoSunat: request.estadoSunat,
      direccion: request.direccionFiscal,
      departamento: request.departamento,
      provincia: request.provincia,
      distrito: request.distrito,
      ubigeo: request.ubigeo,
      idVendedorAsignado: request.idVendedorAsignado,
      idTipoCliente: request.idTipoCliente,
      idEstadoClienteContacto: request.idEstadoClienteContacto,
    };
  }

  private toUpdateRequest(request: ClienteUpdateRequest): ClienteV1UpdateRequest {
    return {
      razonSocial: request.razonSocial,
      condicionSunat: request.condicionSunat,
      estadoSunat: request.estadoSunat,
      direccion: request.direccionFiscal,
      departamento: request.departamento,
      provincia: request.provincia,
      distrito: request.distrito,
      ubigeo: request.ubigeo,
      idVendedorAsignado: request.idVendedorAsignado,
      idTipoCliente: request.idTipoCliente,
      idEstadoClienteContacto: request.idEstadoClienteContacto,
    };
  }

  private toReference(id?: number, nombre?: string): ReferenceResponse | undefined {
    if (id == null && !nombre) {
      return undefined;
    }
    return { id: id ?? 0, nombre: nombre?.trim() || this.getStatusLabelById(id) || 'Sin estado' };
  }

  private getStatusId(cliente: ClienteResponseVm): number | undefined {
    return cliente.estado?.id && cliente.estado.id > 0 ? cliente.estado.id : undefined;
  }

  private getStatusLabelById(id?: number): string | undefined {
    if (id === 1) {
      return 'Activo';
    }
    if (id === 2) {
      return 'Inactivo';
    }
    if (id === 3) {
      return 'Bloqueado';
    }
    return undefined;
  }

  private normalizeStatus(value?: string): string {
    return (value ?? '')
      .trim()
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '');
  }
}

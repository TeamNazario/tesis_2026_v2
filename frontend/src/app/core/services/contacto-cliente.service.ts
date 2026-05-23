import { Injectable, inject } from '@angular/core';
import { Observable, map, switchMap } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import { ReferenceResponse } from '../http/reference-response.model';
import {
  ContactoClienteCreateRequest,
  ContactoClienteResponseVm,
  ContactoClienteUpdateRequest,
} from '../models/contacto-cliente.models';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class ContactoClienteService {
  private readonly api = inject(ApiService);

  getContactosByCliente(clienteId: number): Observable<ContactoClienteResponseVm[]> {
    return this.api
      .get<ApiContactoClienteResponse[]>(`${API_ENDPOINTS.contactosCliente}/cliente/${clienteId}`)
      .pipe(map((items) => items.map((item) => this.toVm(item))));
  }

  getContactoById(contactoId: number): Observable<ContactoClienteResponseVm> {
    return this.api.get<ApiContactoClienteResponse>(`${API_ENDPOINTS.contactosCliente}/${contactoId}`).pipe(map((item) => this.toVm(item)));
  }

  createContacto(request: ContactoClienteCreateRequest): Observable<ContactoClienteResponseVm> {
    return this.api
      .post<ApiContactoClienteResponse>(API_ENDPOINTS.contactosCliente, this.toApiRequest(request))
      .pipe(map((item) => this.toVm(item)));
  }

  updateContacto(contactoId: number, request: ContactoClienteUpdateRequest): Observable<ContactoClienteResponseVm> {
    return this.api
      .put<ApiContactoClienteResponse>(`${API_ENDPOINTS.contactosCliente}/${contactoId}`, this.toApiRequest(request))
      .pipe(map((item) => this.toVm(item)));
  }

  changeContactoStatus(contactoId: number, estadoId: number): Observable<ContactoClienteResponseVm> {
    return this.getContactoById(contactoId).pipe(
      map((contacto) => ({
        clienteId: contacto.clienteId,
        tipoDocumentoId: contacto.tipoDocumentoId ?? 1,
        nroDocumento: contacto.nroDocumento ?? '',
        nombres: contacto.nombres,
        apellidos: contacto.apellidos,
        correo: contacto.correo,
        telefono: contacto.telefono,
        whatsapp: contacto.whatsapp,
        principal: contacto.principal,
        recibeCotizaciones: contacto.recibeCotizaciones,
        recibeNotificaciones: contacto.recibeNotificaciones,
        estadoId,
      })),
      switchMap((payload) => this.updateContacto(contactoId, payload)),
    );
  }

  setContactoPrincipal(contactoId: number): Observable<ContactoClienteResponseVm> {
    return this.getContactoById(contactoId).pipe(
      map((contacto) => ({
        clienteId: contacto.clienteId,
        tipoDocumentoId: contacto.tipoDocumentoId ?? 1,
        nroDocumento: contacto.nroDocumento ?? '',
        nombres: contacto.nombres,
        apellidos: contacto.apellidos,
        correo: contacto.correo,
        telefono: contacto.telefono,
        whatsapp: contacto.whatsapp,
        principal: true,
        recibeCotizaciones: contacto.recibeCotizaciones,
        recibeNotificaciones: contacto.recibeNotificaciones,
        estadoId: contacto.estado?.id ?? 1,
      })),
      switchMap((payload) => this.updateContacto(contactoId, payload)),
    );
  }

  deleteContacto(contactoId: number): Observable<void> {
    return this.api.delete<void>(`${API_ENDPOINTS.contactosCliente}/${contactoId}`);
  }

  private toVm(item: ApiContactoClienteResponse): ContactoClienteResponseVm {
    const fullName = [item.nombre, item.apellidoPaterno, item.apellidoMaterno ?? ''].join(' ').replace(/\s+/g, ' ').trim();
    return {
      id: item.idContacto,
      clienteId: item.cliente?.id ?? 0,
      nombres: item.nombre,
      apellidos: [item.apellidoPaterno, item.apellidoMaterno ?? ''].join(' ').trim(),
      nombreCompleto: fullName,
      telefono: item.celular,
      whatsapp: item.celular,
      correo: item.correo,
      principal: false,
      recibeCotizaciones: true,
      recibeNotificaciones: true,
      estado: item.estado,
      tipoDocumentoId: item.tipoDocumento?.id,
      nroDocumento: item.nroDocumento,
    };
  }

  private toApiRequest(request: ContactoClienteCreateRequest | ContactoClienteUpdateRequest): ApiContactoClienteRequest {
    const [apellidoPaterno, apellidoMaterno = ''] = (request.apellidos || '').split(' ');
    return {
      idCliente: request.clienteId,
      idTipoDoc: request.tipoDocumentoId,
      nroDocumento: request.nroDocumento,
      nombre: request.nombres,
      apellidoPaterno: apellidoPaterno || request.apellidos || 'N/A',
      apellidoMaterno,
      correo: request.correo ?? '',
      celular: request.telefono ?? request.whatsapp ?? '',
      idEstado: request.estadoId,
    };
  }
}

interface ApiContactoClienteResponse {
  idContacto: number;
  cliente?: ReferenceResponse;
  tipoDocumento?: ReferenceResponse;
  nroDocumento: string;
  nombre: string;
  apellidoPaterno: string;
  apellidoMaterno?: string;
  correo?: string;
  celular?: string;
  estado?: ReferenceResponse;
}

interface ApiContactoClienteRequest {
  idCliente: number;
  idTipoDoc: number;
  nroDocumento: string;
  nombre: string;
  apellidoPaterno: string;
  apellidoMaterno: string;
  correo: string;
  celular: string;
  idEstado: number;
}

import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import { ApiService } from './api.service';
import {
  ClienteV1,
  ClienteV1CreateRequest,
  ClienteV1UpdateRequest,
  ContactoClienteV1,
  ContactoClienteV1CreateRequest,
  ContactoClienteV1UpdateRequest,
} from '../models/v1.models';

@Injectable({ providedIn: 'root' })
export class ClienteV1Service {
  private readonly api = inject(ApiService);

  findAll(): Observable<ClienteV1[]> { return this.api.get<ClienteV1[]>(API_ENDPOINTS.v1.clientes); }
  findById(id: number): Observable<ClienteV1> { return this.api.get<ClienteV1>(`${API_ENDPOINTS.v1.clientes}/${id}`); }
  buscar(ruc?: string, razonSocial?: string): Observable<ClienteV1[]> { return this.api.get<ClienteV1[]>(`${API_ENDPOINTS.v1.clientes}/buscar`, { ruc, razonSocial }); }
  create(body: ClienteV1CreateRequest): Observable<ClienteV1> { return this.api.post<ClienteV1>(API_ENDPOINTS.v1.clientes, body); }
  update(id: number, body: ClienteV1UpdateRequest): Observable<ClienteV1> { return this.api.put<ClienteV1>(`${API_ENDPOINTS.v1.clientes}/${id}`, body); }
  patchEstado(id: number, idEstadoClienteContacto: number): Observable<ClienteV1> { return this.api.patch<ClienteV1>(`${API_ENDPOINTS.v1.clientes}/${id}/estado`, { idEstadoClienteContacto }); }

  listContactos(clienteId: number): Observable<ContactoClienteV1[]> {
    return this.api.get<ContactoClienteV1[]>(`${API_ENDPOINTS.v1.clientes}/${clienteId}/contactos`);
  }

  createContacto(clienteId: number, body: ContactoClienteV1CreateRequest): Observable<ContactoClienteV1> {
    return this.api.post<ContactoClienteV1>(`${API_ENDPOINTS.v1.clientes}/${clienteId}/contactos`, body);
  }

  updateContacto(clienteId: number, contactoId: number, body: ContactoClienteV1UpdateRequest): Observable<ContactoClienteV1> {
    return this.api.put<ContactoClienteV1>(`${API_ENDPOINTS.v1.clientes}/${clienteId}/contactos/${contactoId}`, body);
  }

  patchContactoEstado(clienteId: number, contactoId: number, idEstadoClienteContacto: number): Observable<ContactoClienteV1> {
    return this.api.patch<ContactoClienteV1>(`${API_ENDPOINTS.v1.clientes}/${clienteId}/contactos/${contactoId}/estado`, {
      idEstadoClienteContacto,
    });
  }
}

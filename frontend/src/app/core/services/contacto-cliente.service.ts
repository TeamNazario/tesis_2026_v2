import { Injectable, inject } from '@angular/core';
import { Observable, map } from 'rxjs';
import { ReferenceResponse } from '../http/reference-response.model';
import {
  ContactoClienteCreateRequest,
  ContactoClienteResponseVm,
  ContactoClienteUpdateRequest,
} from '../models/contacto-cliente.models';
import { ContactoClienteV1, ContactoClienteV1CreateRequest } from '../models/v1.models';
import { ClienteV1Service } from './cliente-v1.service';

@Injectable({ providedIn: 'root' })
export class ContactoClienteService {
  private readonly clienteV1 = inject(ClienteV1Service);

  getContactosByCliente(clienteId: number): Observable<ContactoClienteResponseVm[]> {
    return this.clienteV1.listContactos(clienteId).pipe(map((items) => items.map((item) => this.toVm(item))));
  }

  createContacto(request: ContactoClienteCreateRequest): Observable<ContactoClienteResponseVm> {
    return this.clienteV1
      .createContacto(request.clienteId, this.toApiRequest(request))
      .pipe(map((item) => this.toVm(item)));
  }

  updateContacto(contactoId: number, request: ContactoClienteUpdateRequest): Observable<ContactoClienteResponseVm> {
    return this.clienteV1
      .updateContacto(request.clienteId, contactoId, this.toApiRequest(request))
      .pipe(map((item) => this.toVm(item)));
  }

  changeContactoStatus(clienteId: number, contactoId: number, idEstadoClienteContacto: number): Observable<ContactoClienteResponseVm> {
    return this.clienteV1
      .patchContactoEstado(clienteId, contactoId, idEstadoClienteContacto)
      .pipe(map((item) => this.toVm(item)));
  }

  setContactoPrincipal(_contactoId: number): Observable<never> {
    throw new Error('TODO: Endpoint de contacto principal no disponible en backend v1.');
  }

  deleteContacto(_contactoId: number): Observable<never> {
    throw new Error('TODO: Endpoint de eliminacion de contacto no disponible en backend v1.');
  }

  private toVm(item: ContactoClienteV1): ContactoClienteResponseVm {
    const apellidos = [item.apellidoPaterno, item.apellidoMaterno ?? ''].join(' ').replace(/\s+/g, ' ').trim();
    const fullName = [item.nombre, apellidos].join(' ').replace(/\s+/g, ' ').trim();
    return {
      id: item.idContacto,
      clienteId: item.idCliente,
      nombres: item.nombre,
      apellidos,
      nombreCompleto: fullName,
      telefono: item.celular,
      whatsapp: item.celular,
      correo: item.correo,
      principal: false,
      recibeCotizaciones: true,
      recibeNotificaciones: true,
      estado: this.toReference(item.idEstadoClienteContacto, item.estadoClienteContacto),
      tipoDocumentoId: item.idTipoDoc,
      tipoDocumento: item.tipoDocumento,
      nroDocumento: item.nroDocumento,
      idEstadoClienteContacto: item.idEstadoClienteContacto,
      estadoClienteContacto: item.estadoClienteContacto,
    };
  }

  private toApiRequest(request: ContactoClienteCreateRequest | ContactoClienteUpdateRequest): ContactoClienteV1CreateRequest {
    return {
      idTipoDoc: request.tipoDocumentoId,
      nroDocumento: request.nroDocumento,
      nombre: request.nombre,
      apellidoPaterno: request.apellidoPaterno,
      apellidoMaterno: request.apellidoMaterno,
      correo: request.correo,
      celular: request.celular,
      idEstadoClienteContacto: request.idEstadoClienteContacto,
    };
  }

  private toReference(id?: number, nombre?: string): ReferenceResponse | undefined {
    if (!id && !nombre) {
      return undefined;
    }
    return { id: id ?? 0, nombre: nombre ?? 'Sin estado' };
  }
}

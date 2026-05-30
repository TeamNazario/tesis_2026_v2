import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import {
  PrecioTipoClienteV1,
  PrecioTipoClienteV1CreateRequest,
  PrecioTipoClienteV1UpdateRequest,
} from '../models/v1.models';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class PrecioTipoClienteV1Service {
  private readonly api = inject(ApiService);

  findAll(): Observable<PrecioTipoClienteV1[]> { return this.api.get<PrecioTipoClienteV1[]>(API_ENDPOINTS.v1.preciosTipoCliente); }
  findById(id: number): Observable<PrecioTipoClienteV1> { return this.api.get<PrecioTipoClienteV1>(`${API_ENDPOINTS.v1.preciosTipoCliente}/${id}`); }
  findByProductoId(idProducto: number): Observable<PrecioTipoClienteV1[]> {
    return this.api.get<PrecioTipoClienteV1[]>(`${API_ENDPOINTS.v1.preciosTipoCliente}/producto/${idProducto}`);
  }
  create(body: PrecioTipoClienteV1CreateRequest): Observable<PrecioTipoClienteV1> { return this.api.post<PrecioTipoClienteV1>(API_ENDPOINTS.v1.preciosTipoCliente, body); }
  update(id: number, body: PrecioTipoClienteV1UpdateRequest): Observable<PrecioTipoClienteV1> { return this.api.put<PrecioTipoClienteV1>(`${API_ENDPOINTS.v1.preciosTipoCliente}/${id}`, body); }
}

import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import { CatalogoItem } from '../models/v1.models';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class CatalogoV1Service {
  private readonly api = inject(ApiService);

  tiposDocumento(): Observable<CatalogoItem[]> { return this.api.get<CatalogoItem[]>(`${API_ENDPOINTS.v1.catalogos}/tipos-documento`); }
  estadosUsuario(): Observable<CatalogoItem[]> { return this.api.get<CatalogoItem[]>(`${API_ENDPOINTS.v1.catalogos}/estados-usuario`); }
  estadosClienteContacto(): Observable<CatalogoItem[]> { return this.api.get<CatalogoItem[]>(`${API_ENDPOINTS.v1.catalogos}/estados-cliente-contacto`); }
  tiposCliente(): Observable<CatalogoItem[]> { return this.api.get<CatalogoItem[]>(`${API_ENDPOINTS.v1.catalogos}/tipos-cliente`); }
  estadosProducto(): Observable<CatalogoItem[]> { return this.api.get<CatalogoItem[]>(`${API_ENDPOINTS.v1.catalogos}/estados-producto`); }
  estadosCotizacion(): Observable<CatalogoItem[]> { return this.api.get<CatalogoItem[]>(`${API_ENDPOINTS.v1.catalogos}/estados-cotizacion`); }
  perfiles(): Observable<CatalogoItem[]> { return this.api.get<CatalogoItem[]>(`${API_ENDPOINTS.v1.catalogos}/perfiles`); }
}

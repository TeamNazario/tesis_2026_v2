import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../constants/api-endpoints';
import { RucConsultaResponse } from '../models/documento.models';
import { ApiService } from './api.service';

@Injectable({ providedIn: 'root' })
export class DocumentoService {
  private readonly api = inject(ApiService);

  consultarRuc(numero: string): Observable<RucConsultaResponse> {
    return this.api.get<RucConsultaResponse>(`${API_ENDPOINTS.documentos}/ruc/${numero}`);
  }
}


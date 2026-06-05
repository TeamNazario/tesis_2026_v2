import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_ENDPOINTS } from '../../../core/constants/api-endpoints';
import { ApiService, QueryParams } from '../../../core/services/api.service';
import { ReporteCodigo, ReporteFiltro } from '../models/reporte.model';

@Injectable({ providedIn: 'root' })
export class ReporteService {
  private readonly api = inject(ApiService);
  private readonly basePath = API_ENDPOINTS.v1.reportes;

  exportar(codigo: ReporteCodigo, filtros: ReporteFiltro): Observable<Blob> {
    return this.api.download(`${this.basePath}/${codigo}/excel`, this.toParams(filtros));
  }

  private toParams(filtros: ReporteFiltro): QueryParams {
    return {
      fechaInicio: filtros.fechaInicio,
      fechaFin: filtros.fechaFin,
      idEstadoCotizacion: filtros.idEstadoCotizacion,
      idCliente: filtros.idCliente,
      idVendedor: filtros.idVendedor,
      idProducto: filtros.idProducto,
      idTipoCliente: filtros.idTipoCliente,
      idEstadoProducto: filtros.idEstadoProducto,
      moneda: filtros.moneda,
      departamento: filtros.departamento,
      provincia: filtros.provincia,
      distrito: filtros.distrito,
      stockBajo: filtros.stockBajo,
      search: filtros.search,
    };
  }
}

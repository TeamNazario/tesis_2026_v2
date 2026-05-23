import { ReferenceResponse } from '../http/reference-response.model';

export interface ClienteResponse {
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

export interface ClienteRequest {
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
  idVendedorAsignado?: number;
  idEstado: number;
  usuarioRegistro?: string;
  usuarioActualiza?: string;
}

export interface ProductoResponse {
  idProducto: number;
  nombreProducto: string;
  descripcionTecnica?: string;
  unidadMedida: string;
  precioBaseUnitario: number;
  concentracionUreaAus32?: number;
  stockFisico: number;
  stockReservado: number;
  stockDisponible: number;
  stockMinimoSeguridad: number;
  estado?: ReferenceResponse;
}

export interface CotizacionResponse {
  idCotizacion: number;
  uuidPublico: string;
  cliente: ReferenceResponse;
  zona: ReferenceResponse;
  vendedor?: ReferenceResponse;
  fechaEmision: string;
  fechaVencimiento: string;
  subtotal: number;
  igv: number;
  montoTotal: number;
  origenCotizacion: string;
  estadoCotizacion: string;
  pdfPath?: string;
  detalles?: CotizacionDetalleResponse[];
}

export interface CotizacionDetalleResponse {
  idDetalle?: number;
  producto?: ReferenceResponse;
  cantidad?: number;
  precioUnitario?: number;
  subtotal?: number;
}

export interface LogInventarioResponse {
  idLogInventario: number;
  producto: ReferenceResponse;
  idUsuario?: number;
  tipoMovimiento: string;
  cantidad: number;
  stockFisicoMomento: number;
  stockReservadoMomento: number;
  fechaEvento: string;
}

export interface LogEficienciaChatbotResponse {
  idLogEficiencia: number;
  sessionIdWhatsapp: string;
  rucConsultado?: string;
  intencionDetectada?: string;
  timestampPrimerMensaje: string;
  timestampFinProcesamiento?: string;
  tiempoAtencionSegundos?: number;
  apiSunatRespondio?: boolean;
  pdfGeneradoExitosamente?: boolean;
  payloadAuditoria?: string;
}

export interface DashboardKpis {
  cotizacionesHoy: number;
  cotizacionesSemana: number;
  cotizacionesMes: number;
  tiempoPromedioRespuestaSegundos: number;
  cotizacionesChatbot: number;
  cotizacionesManuales: number;
  cotizacionesVencidas: number;
  cotizacionesConfirmadas: number;
  stockFisico: number;
  stockReservado: number;
  stockDisponible: number;
  alertasStockBajo: number;
  pdfsGenerados: number;
}

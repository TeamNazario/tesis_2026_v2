import { ReferenceResponse } from '../http/reference-response.model';

export interface ClienteResponse {
  idCliente: number;
  ruc: string;
  razonSocial: string;
  condicionSunat?: string;
  estadoSunat?: string;
  direccion?: string;
  departamento?: string;
  provincia?: string;
  distrito?: string;
  ubigeo?: string;
  idVendedorAsignado?: number;
  vendedorAsignado?: string;
  idTipoCliente?: number;
  tipoCliente?: string;
  idEstadoClienteContacto?: number;
  estadoClienteContacto?: string;
  usuRegistro?: string;
  fecRegistro?: string;
  usuActualiza?: string;
  fecActualiza?: string;
}

export interface ClienteRequest {
  ruc: string;
  razonSocial: string;
  condicionSunat?: string;
  estadoSunat?: string;
  direccion?: string;
  departamento?: string;
  provincia?: string;
  distrito?: string;
  ubigeo?: string;
  idVendedorAsignado: number;
  idTipoCliente: number;
  idEstadoClienteContacto: number;
}

export interface ProductoResponse {
  idProducto: number;
  nombreProducto: string;
  unidadMedida: string;
  peso?: number;
  volumen?: number;
  stockFisico: number;
  stockReservado: number;
  stockDisponible: number;
  stockMinimo: number;
  cantMinVenta: number;
  idEstadoProducto?: number;
  estadoProducto?: string;
}

export interface CotizacionResponse {
  idCotizacion: number;
  idCliente: number;
  cliente: string;
  idVendedor: number;
  vendedor: string;
  fechaEmision: string;
  fechaVencimiento: string;
  moneda: string;
  subtotal: number;
  igv: number;
  importeTotal: number;
  direccionDespacho?: string;
  depProvDis?: string;
  flagCubierto?: boolean;
  observaciones?: string;
  idEstadoCotizacion: number;
  estadoCotizacion: string;
  pdfPath?: string;
  detalles?: CotizacionDetalleResponse[];
}

export interface CotizacionDetalleResponse {
  idDetalleCoti: number;
  idCotizacion: number;
  idProducto: number;
  producto: string;
  cantidad: number;
  precioUni: number;
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

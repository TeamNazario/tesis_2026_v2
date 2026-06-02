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
  descEstadoProducto?: string;
}

export interface CotizacionResponse {
  idCotizacion: number;
  idCliente: number;
  cliente: string;
  razonSocialCliente?: string;
  rucCliente?: string;
  idVendedor: number;
  vendedor: string;
  nombreVendedor?: string;
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
  descEstadoCotizacion?: string;
  pdfPath?: string;
  detalles?: CotizacionDetalleResponse[];
}

export interface CotizacionDetalleResponse {
  idDetalleCoti: number;
  idCotizacion: number;
  idProducto: number;
  producto: string;
  nombreProducto?: string;
  unidadMedida?: string;
  cantidad: number;
  precioUni: number;
  subtotalDetalle?: number;
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

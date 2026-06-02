export interface DetalleCotizacionResponse {
  idDetalleCoti: number;
  idCotizacion: number;
  idProducto: number;
  nombreProducto: string;
  unidadMedida: string;
  cantidad: number;
  precioUni: number;
  importe: number;
}

export interface CotizacionResponse {
  idCotizacion: number;
  idCliente: number;
  rucCliente: string;
  razonSocialCliente: string;
  direccionCliente?: string;
  idVendedor: number;
  nombreVendedor: string;
  fechaEmision: string;
  fechaVencimiento: string;
  moneda: string;
  subtotal: number;
  igv: number;
  importeTotal: number;
  direccionDespacho?: string;
  depProvDis?: string;
  flagCubierto?: number;
  observaciones?: string;
  idEstadoCotizacion: number;
  descEstadoCotizacion: string;
  pdfPath?: string;
  detalles: DetalleCotizacionResponse[];
}

export interface DetalleCotizacionCreateRequest {
  idProducto: number;
  cantidad: number;
  precioUni?: number;
}

export interface CotizacionCreateRequest {
  idCliente: number;
  idVendedor: number;
  fechaVencimiento: string;
  moneda: string;
  direccionDespacho?: string;
  depProvDis?: string;
  flagCubierto?: number;
  observaciones?: string;
  idEstadoCotizacion: number;
  detalles: DetalleCotizacionCreateRequest[];
}

export interface CotizacionFilter {
  search?: string | null;
  idCliente?: number | null;
  idVendedor?: number | null;
  idEstadoCotizacion?: number | null;
  fechaInicio?: string | null;
  fechaFin?: string | null;
}

export interface CotizacionEstadoUpdateRequest {
  idEstadoCotizacion: number;
}

export interface CotizacionPrecioProductoResponse {
  idProducto: number;
  nombreProducto: string;
  unidadMedida: string;
  idTipoCliente: number;
  descTipoCliente: string;
  precioUnitario: number;
  moneda: string;
}

export interface CotizacionCalcularItemRequest {
  idCliente: number;
  idProducto: number;
  cantidad: number;
  moneda: string;
}

export interface CotizacionCalcularItemResponse {
  idProducto: number;
  nombreProducto: string;
  unidadMedida: string;
  cantidad: number;
  precioUnitario: number;
  importe: number;
  moneda: string;
}

export interface CotizacionCalcularResumenRequest {
  idCliente: number;
  moneda: string;
  detalles: Array<{
    idProducto: number;
    cantidad: number;
  }>;
}

export interface CotizacionCalcularResumenResponse {
  items: CotizacionCalcularItemResponse[];
  subtotal: number;
  igv: number;
  importeTotal: number;
  moneda: string;
}

export interface CotizacionPdfResponse {
  idCotizacion: number;
  pdfPath: string;
  fileName: string;
  message: string;
}

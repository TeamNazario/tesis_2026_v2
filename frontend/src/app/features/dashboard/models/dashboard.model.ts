export interface DashboardFilter {
  fechaInicio?: string;
  fechaFin?: string;
  idEstadoCotizacion?: number;
  idVendedor?: number;
  idCliente?: number;
  idProducto?: number;
  idTipoCliente?: number;
  moneda?: string;
}

export interface DashboardResumenResponse {
  totalCotizaciones: number;
  cotizacionesGeneradas: number;
  cotizacionesAprobadas: number;
  cotizacionesVencidas: number;
  cotizacionesRechazadas: number;
  cotizacionesAnuladas: number;
  tasaConversion: number;
  importeTotalCotizado: number;
  importeTotalAprobado: number;
  ticketPromedio: number;
  clientesAtendidos: number;
  productosCotizados: number;
  stockTotalFisico: number;
  stockTotalReservado: number;
  stockTotalDisponible: number;
  productosStockBajo: number;
}

export interface CotizacionTendenciaResponse {
  fecha: string;
  totalCotizaciones: number;
  importeTotal: number;
}

export interface CotizacionPorEstadoResponse {
  estado: string;
  cantidad: number;
  importeTotal: number;
}

export interface TopProductoResponse {
  idProducto: number;
  nombreProducto: string;
  unidadMedida: string;
  cantidadCotizada: number;
  importeTotal: number;
}

export interface TopClienteResponse {
  idCliente: number;
  razonSocial: string;
  ruc: string;
  cantidadCotizaciones: number;
  importeTotal: number;
}

export interface CotizacionPorVendedorResponse {
  idVendedor: number;
  nombreVendedor: string;
  cantidadCotizaciones: number;
  cotizacionesAprobadas: number;
  importeTotal: number;
}

export interface StockProductoDashboardResponse {
  idProducto: number;
  nombreProducto: string;
  unidadMedida: string;
  stockFisico: number;
  stockReservado: number;
  stockDisponible: number;
  stockMinimo: number;
  stockBajo: boolean;
}

export interface ProductoStockBajoResponse {
  idProducto: number;
  nombreProducto: string;
  unidadMedida: string;
  stockDisponible: number;
  stockMinimo: number;
}

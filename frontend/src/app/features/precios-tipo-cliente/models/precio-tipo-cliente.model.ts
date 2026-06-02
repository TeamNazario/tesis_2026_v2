export interface PrecioTipoClienteResponse {
  idPrecio: number;
  precioUnitario: number;
  moneda: string;
  idTipoCliente: number;
  tipoCliente?: string;
  descTipoCliente?: string;
  idEstadoProducto: number;
  estadoProducto?: string;
  descEstadoProducto?: string;
  idProducto: number;
  producto?: string;
  nombreProducto?: string;
  unidadMedida?: string;
  usuRegistro?: string;
  fecRegistro?: string;
  usuActualiza?: string;
  fecActualiza?: string;
}

export interface PrecioTipoClienteCreateRequest {
  precioUnitario: number;
  moneda: string;
  idTipoCliente: number;
  idEstadoProducto: number;
  idProducto: number;
}

export interface PrecioTipoClienteUpdateRequest extends Partial<PrecioTipoClienteCreateRequest> {}

export interface PrecioTipoClienteFilter {
  search?: string;
  idProducto?: number | null;
  idTipoCliente?: number | null;
  idEstadoProducto?: number | null;
  moneda?: string | null;
}

export interface ProductoComboResponse {
  idProducto: number;
  nombreProducto: string;
  unidadMedida?: string;
  stockDisponible?: number;
}

export interface TipoClienteComboResponse {
  idTipoCliente: number;
  descTipoCliente: string;
}

export interface EstadoProductoResponse {
  idEstadoProducto: number;
  descEstadoProducto: string;
}

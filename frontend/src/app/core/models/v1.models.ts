export interface CatalogoItem {
  id: number;
  descripcion: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface ClienteV1 {
  idCliente: number;
  ruc: string;
  razonSocial: string;
  condicionSunat: string;
  estadoSunat: string;
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

export interface ClienteV1CreateRequest {
  ruc: string;
  razonSocial: string;
  condicionSunat: string;
  estadoSunat: string;
  direccion?: string;
  departamento?: string;
  provincia?: string;
  distrito?: string;
  ubigeo?: string;
  idVendedorAsignado: number;
  idTipoCliente: number;
  idEstadoClienteContacto: number;
}

export interface ClienteV1UpdateRequest {
  razonSocial: string;
  condicionSunat: string;
  estadoSunat: string;
  direccion?: string;
  departamento?: string;
  provincia?: string;
  distrito?: string;
  ubigeo?: string;
  idVendedorAsignado: number;
  idTipoCliente: number;
  idEstadoClienteContacto: number;
}

export interface ContactoClienteV1 {
  idContacto: number;
  idCliente: number;
  idTipoDoc: number;
  tipoDocumento: string;
  nroDocumento: string;
  nombre: string;
  apellidoPaterno: string;
  apellidoMaterno?: string;
  correo?: string;
  celular?: string;
  idEstadoClienteContacto?: number;
  estadoClienteContacto?: string;
}

export interface ContactoClienteV1CreateRequest {
  idTipoDoc: number;
  nroDocumento: string;
  nombre: string;
  apellidoPaterno: string;
  apellidoMaterno?: string;
  correo?: string;
  celular?: string;
  idEstadoClienteContacto: number;
}

export interface ContactoClienteV1UpdateRequest {
  idTipoDoc: number;
  nroDocumento: string;
  nombre: string;
  apellidoPaterno: string;
  apellidoMaterno?: string;
  correo?: string;
  celular?: string;
  idEstadoClienteContacto: number;
}

export interface ProductoV1 {
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

export interface ProductoV1CreateRequest {
  nombreProducto: string;
  unidadMedida: string;
  peso: number;
  volumen: number;
  stockFisico: number;
  stockReservado: number;
  stockDisponible: number;
  stockMinimo: number;
  cantMinVenta: number;
  idEstadoProducto: number;
}

export interface ProductoV1UpdateRequest extends Partial<ProductoV1CreateRequest> {}

export interface PrecioTipoClienteV1 {
  idPrecio: number;
  precioUnitario: number;
  moneda: string;
  idTipoCliente: number;
  tipoCliente: string;
  idEstadoProducto: number;
  estadoProducto: string;
  idProducto: number;
  producto: string;
}

export interface PrecioTipoClienteV1CreateRequest {
  precioUnitario: number;
  moneda: string;
  idTipoCliente: number;
  idEstadoProducto: number;
  idProducto: number;
}

export interface PrecioTipoClienteV1UpdateRequest extends Partial<PrecioTipoClienteV1CreateRequest> {}

export interface DetalleCotizacionV1 {
  idDetalleCoti: number;
  idCotizacion: number;
  idProducto: number;
  producto: string;
  cantidad: number;
  precioUni: number;
}

export interface CotizacionV1 {
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
  detalles?: DetalleCotizacionV1[];
}

export interface CatalogoItem {
  id: number;
  descripcion: string;
}

export interface TipoClienteComboResponse {
  idTipoCliente: number;
  descTipoCliente: string;
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
  descEstadoProducto?: string;
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

export interface DetalleCotizacionV1 {
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

export interface CotizacionV1 {
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
  detalles?: DetalleCotizacionV1[];
}

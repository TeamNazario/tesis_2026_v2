export type ReporteCodigo =
  | 'cotizaciones'
  | 'cotizaciones-detalle'
  | 'clientes'
  | 'productos'
  | 'stock'
  | 'precios-tipo-cliente'
  | 'vendedores'
  | 'productos-mas-cotizados'
  | 'clientes-top';

export type ReporteFiltroKey =
  | 'fechaInicio'
  | 'fechaFin'
  | 'idEstadoCotizacion'
  | 'idCliente'
  | 'idVendedor'
  | 'idProducto'
  | 'idTipoCliente'
  | 'idEstadoProducto'
  | 'moneda'
  | 'departamento'
  | 'provincia'
  | 'distrito'
  | 'stockBajo'
  | 'search';

export interface ReporteTipo {
  codigo: ReporteCodigo;
  nombre: string;
  descripcion: string;
  icono: string;
  categoria: string;
  filtros: ReporteFiltroKey[];
  fileName: string;
}

export interface ReporteFiltro {
  fechaInicio?: string;
  fechaFin?: string;
  idEstadoCotizacion?: number;
  idCliente?: number;
  idVendedor?: number;
  idProducto?: number;
  idTipoCliente?: number;
  idEstadoProducto?: number;
  moneda?: string;
  departamento?: string;
  provincia?: string;
  distrito?: string;
  stockBajo?: boolean;
  search?: string;
}

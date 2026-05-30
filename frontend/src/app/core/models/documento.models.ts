export interface RucConsultaResponse {
  ruc: string;
  razonSocial: string;
  nombreComercial?: string;
  estado: string;
  condicion: string;
  direccion: string;
  departamento: string;
  provincia: string;
  distrito: string;
  ubigeo: string;
  telefonos?: string[];
  capital?: string;
  activo?: boolean;
  habido?: boolean;
  aptoParaCotizacion?: boolean;
  mensajeValidacion?: string;
}

export interface DniConsultaResponse {
  dni: string;
  nombres: string;
  apellidoPaterno: string;
  apellidoMaterno?: string;
}

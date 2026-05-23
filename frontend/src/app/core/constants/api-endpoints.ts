export const API_ENDPOINTS = {
  auth: {
    login: '/auth/login',
    register: '/auth/register',
    me: '/auth/me',
  },
  clientes: '/clientes',
  contactosCliente: '/contactos-cliente',
  usuarios: '/usuarios',
  productos: '/productos',
  cotizaciones: '/cotizaciones',
  logsInventario: '/logs-inventario',
  logsEficienciaChatbot: '/logs-eficiencia-chatbot',
  perfiles: '/perfiles',
  estados: '/estados',
  tiposDocumento: '/tipos-documento',
  zonasDespacho: '/zonas-despacho',
  documentos: '/v1/documentos',
} as const;

package com.example.demo.service;

import com.example.demo.dto.ReporteClienteDTO;
import com.example.demo.dto.ReporteCotizacionDTO;
import com.example.demo.dto.ReporteCotizacionDetalleDTO;
import com.example.demo.dto.ReporteFiltroRequest;
import com.example.demo.dto.ReportePrecioTipoClienteDTO;
import com.example.demo.dto.ReporteProductoDTO;
import com.example.demo.dto.ReporteStockDTO;
import com.example.demo.dto.ReporteTopClienteDTO;
import com.example.demo.dto.ReporteTopProductoDTO;
import com.example.demo.dto.ReporteVendedorDTO;
import java.util.List;

public interface ReporteService {
    List<ReporteCotizacionDTO> cotizaciones(ReporteFiltroRequest filter);
    List<ReporteCotizacionDetalleDTO> cotizacionesDetalle(ReporteFiltroRequest filter);
    List<ReporteClienteDTO> clientes(ReporteFiltroRequest filter);
    List<ReporteProductoDTO> productos(ReporteFiltroRequest filter);
    List<ReportePrecioTipoClienteDTO> preciosTipoCliente(ReporteFiltroRequest filter);
    List<ReporteStockDTO> stock(ReporteFiltroRequest filter);
    List<ReporteVendedorDTO> vendedores(ReporteFiltroRequest filter);
    List<ReporteTopProductoDTO> productosMasCotizados(ReporteFiltroRequest filter);
    List<ReporteTopClienteDTO> clientesTop(ReporteFiltroRequest filter);
}

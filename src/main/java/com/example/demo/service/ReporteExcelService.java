package com.example.demo.service;

import com.example.demo.dto.ReporteExcelFile;
import com.example.demo.dto.ReporteFiltroRequest;

public interface ReporteExcelService {
    ReporteExcelFile cotizacionesExcel(ReporteFiltroRequest filter, String generatedBy);
    ReporteExcelFile cotizacionesDetalleExcel(ReporteFiltroRequest filter, String generatedBy);
    ReporteExcelFile clientesExcel(ReporteFiltroRequest filter, String generatedBy);
    ReporteExcelFile productosExcel(ReporteFiltroRequest filter, String generatedBy);
    ReporteExcelFile preciosTipoClienteExcel(ReporteFiltroRequest filter, String generatedBy);
    ReporteExcelFile stockExcel(ReporteFiltroRequest filter, String generatedBy);
    ReporteExcelFile vendedoresExcel(ReporteFiltroRequest filter, String generatedBy);
    ReporteExcelFile productosMasCotizadosExcel(ReporteFiltroRequest filter, String generatedBy);
    ReporteExcelFile clientesTopExcel(ReporteFiltroRequest filter, String generatedBy);
}

package com.example.demo.service;

import com.example.demo.dto.ReporteClienteDTO;
import com.example.demo.dto.ReporteCotizacionDTO;
import com.example.demo.dto.ReporteCotizacionDetalleDTO;
import com.example.demo.dto.ReporteExcelFile;
import com.example.demo.dto.ReporteFiltroRequest;
import com.example.demo.dto.ReportePrecioTipoClienteDTO;
import com.example.demo.dto.ReporteProductoDTO;
import com.example.demo.dto.ReporteStockDTO;
import com.example.demo.dto.ReporteTopClienteDTO;
import com.example.demo.dto.ReporteTopProductoDTO;
import com.example.demo.dto.ReporteVendedorDTO;
import com.example.demo.util.ExcelExportUtil;
import com.example.demo.util.FileNameUtil;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ReporteExcelServiceImpl implements ReporteExcelService {
    private final ReporteService reporteService;
    private final ExcelExportUtil excel;
    private final FileNameUtil fileNameUtil;
    private final AuditoriaService auditoriaService;

    public ReporteExcelServiceImpl(
            ReporteService reporteService,
            ExcelExportUtil excel,
            FileNameUtil fileNameUtil,
            AuditoriaService auditoriaService
    ) {
        this.reporteService = reporteService;
        this.excel = excel;
        this.fileNameUtil = fileNameUtil;
        this.auditoriaService = auditoriaService;
    }

    @Override
    public ReporteExcelFile cotizacionesExcel(ReporteFiltroRequest filter, String generatedBy) {
        List<ReporteCotizacionDTO> rows = reporteService.cotizaciones(filter);
        String fileName = fileNameUtil.reporte("reporte_cotizaciones", filter);
        byte[] content = excel.buildWorkbook("REPORTE DE COTIZACIONES", "Cotizaciones", generatedBy, filter, List.of(
                col("ID cotizacion", ReporteCotizacionDTO::idCotizacion),
                col("Numero", ReporteCotizacionDTO::numeroCotizacion),
                col("Fecha emision", ReporteCotizacionDTO::fechaEmision),
                col("Fecha vencimiento", ReporteCotizacionDTO::fechaVencimiento),
                col("Estado", ReporteCotizacionDTO::estado),
                col("Cliente", ReporteCotizacionDTO::cliente),
                col("RUC", ReporteCotizacionDTO::ruc),
                col("Vendedor", ReporteCotizacionDTO::vendedor),
                col("Moneda", ReporteCotizacionDTO::moneda),
                col("Subtotal", ReporteCotizacionDTO::subtotal),
                col("IGV", ReporteCotizacionDTO::igv),
                col("Importe total", ReporteCotizacionDTO::importeTotal),
                col("Direccion despacho", ReporteCotizacionDTO::direccionDespacho),
                col("Observaciones", ReporteCotizacionDTO::observaciones),
                col("PDF generado", ReporteCotizacionDTO::pdfGenerado),
                col("Cantidad productos", ReporteCotizacionDTO::cantidadProductos)
        ), rows);
        audit("cotizaciones", fileName, filter);
        return new ReporteExcelFile(fileName, content);
    }

    @Override
    public ReporteExcelFile cotizacionesDetalleExcel(ReporteFiltroRequest filter, String generatedBy) {
        List<ReporteCotizacionDetalleDTO> rows = reporteService.cotizacionesDetalle(filter);
        String fileName = fileNameUtil.reporte("reporte_cotizaciones_detalle", filter);
        byte[] content = excel.buildWorkbook("REPORTE DE DETALLE DE COTIZACIONES", "Detalle", generatedBy, filter, List.of(
                col("ID cotizacion", ReporteCotizacionDetalleDTO::idCotizacion),
                col("Fecha emision", ReporteCotizacionDetalleDTO::fechaEmision),
                col("Estado", ReporteCotizacionDetalleDTO::estadoCotizacion),
                col("Cliente", ReporteCotizacionDetalleDTO::cliente),
                col("RUC", ReporteCotizacionDetalleDTO::ruc),
                col("Vendedor", ReporteCotizacionDetalleDTO::vendedor),
                col("Producto", ReporteCotizacionDetalleDTO::producto),
                col("Unidad", ReporteCotizacionDetalleDTO::unidadMedida),
                col("Cantidad", ReporteCotizacionDetalleDTO::cantidad),
                col("Precio unitario", ReporteCotizacionDetalleDTO::precioUnitario),
                col("Importe", ReporteCotizacionDetalleDTO::importe),
                col("Moneda", ReporteCotizacionDetalleDTO::moneda)
        ), rows);
        audit("cotizaciones-detalle", fileName, filter);
        return new ReporteExcelFile(fileName, content);
    }

    @Override
    public ReporteExcelFile clientesExcel(ReporteFiltroRequest filter, String generatedBy) {
        List<ReporteClienteDTO> rows = reporteService.clientes(filter);
        String fileName = fileNameUtil.reporte("reporte_clientes", filter);
        byte[] content = excel.buildWorkbook("REPORTE DE CLIENTES", "Clientes", generatedBy, filter, List.of(
                col("ID cliente", ReporteClienteDTO::idCliente),
                col("RUC", ReporteClienteDTO::ruc),
                col("Razon social", ReporteClienteDTO::razonSocial),
                col("Condicion SUNAT", ReporteClienteDTO::condicionSunat),
                col("Estado SUNAT", ReporteClienteDTO::estadoSunat),
                col("Direccion", ReporteClienteDTO::direccion),
                col("Departamento", ReporteClienteDTO::departamento),
                col("Provincia", ReporteClienteDTO::provincia),
                col("Distrito", ReporteClienteDTO::distrito),
                col("Ubigeo", ReporteClienteDTO::ubigeo),
                col("Tipo cliente", ReporteClienteDTO::tipoCliente),
                col("Vendedor asignado", ReporteClienteDTO::vendedorAsignado),
                col("Estado cliente", ReporteClienteDTO::estadoCliente),
                col("Fecha registro", ReporteClienteDTO::fechaRegistro),
                col("Fecha actualizacion", ReporteClienteDTO::fechaActualizacion)
        ), rows);
        audit("clientes", fileName, filter);
        return new ReporteExcelFile(fileName, content);
    }

    @Override
    public ReporteExcelFile productosExcel(ReporteFiltroRequest filter, String generatedBy) {
        List<ReporteProductoDTO> rows = reporteService.productos(filter);
        String fileName = fileNameUtil.reporte("reporte_productos", filter);
        byte[] content = excel.buildWorkbook("REPORTE DE PRODUCTOS", "Productos", generatedBy, filter, List.of(
                col("ID producto", ReporteProductoDTO::idProducto),
                col("Producto", ReporteProductoDTO::nombreProducto),
                col("Unidad", ReporteProductoDTO::unidadMedida),
                col("Peso", ReporteProductoDTO::peso),
                col("Volumen", ReporteProductoDTO::volumen),
                col("Stock fisico", ReporteProductoDTO::stockFisico),
                col("Stock reservado", ReporteProductoDTO::stockReservado),
                col("Stock disponible", ReporteProductoDTO::stockDisponible),
                col("Stock minimo", ReporteProductoDTO::stockMinimo),
                col("Cantidad minima venta", ReporteProductoDTO::cantidadMinimaVenta),
                col("Estado", ReporteProductoDTO::estadoProducto),
                col("Fecha registro", ReporteProductoDTO::fechaRegistro),
                col("Fecha actualizacion", ReporteProductoDTO::fechaActualizacion)
        ), rows);
        audit("productos", fileName, filter);
        return new ReporteExcelFile(fileName, content);
    }

    @Override
    public ReporteExcelFile preciosTipoClienteExcel(ReporteFiltroRequest filter, String generatedBy) {
        List<ReportePrecioTipoClienteDTO> rows = reporteService.preciosTipoCliente(filter);
        String fileName = fileNameUtil.reporte("reporte_precios_tipo_cliente", filter);
        byte[] content = excel.buildWorkbook("REPORTE DE PRECIOS POR TIPO DE CLIENTE", "Precios", generatedBy, filter, List.of(
                col("ID precio", ReportePrecioTipoClienteDTO::idPrecio),
                col("Producto", ReportePrecioTipoClienteDTO::producto),
                col("Unidad", ReportePrecioTipoClienteDTO::unidadMedida),
                col("Tipo cliente", ReportePrecioTipoClienteDTO::tipoCliente),
                col("Precio unitario", ReportePrecioTipoClienteDTO::precioUnitario),
                col("Moneda", ReportePrecioTipoClienteDTO::moneda),
                col("Estado", ReportePrecioTipoClienteDTO::estado),
                col("Fecha registro", ReportePrecioTipoClienteDTO::fechaRegistro),
                col("Fecha actualizacion", ReportePrecioTipoClienteDTO::fechaActualizacion)
        ), rows);
        audit("precios-tipo-cliente", fileName, filter);
        return new ReporteExcelFile(fileName, content);
    }

    @Override
    public ReporteExcelFile stockExcel(ReporteFiltroRequest filter, String generatedBy) {
        List<ReporteStockDTO> rows = reporteService.stock(filter);
        String fileName = fileNameUtil.reporte("reporte_stock", filter);
        byte[] content = excel.buildWorkbook("REPORTE DE STOCK", "Stock", generatedBy, filter, List.of(
                col("ID producto", ReporteStockDTO::idProducto),
                col("Producto", ReporteStockDTO::producto),
                col("Unidad", ReporteStockDTO::unidadMedida),
                col("Stock fisico", ReporteStockDTO::stockFisico),
                col("Stock reservado", ReporteStockDTO::stockReservado),
                col("Stock disponible", ReporteStockDTO::stockDisponible),
                col("Stock minimo", ReporteStockDTO::stockMinimo),
                col("Cantidad minima venta", ReporteStockDTO::cantidadMinimaVenta),
                col("Stock bajo", ReporteStockDTO::stockBajo),
                col("Estado", ReporteStockDTO::estadoProducto)
        ), rows);
        audit("stock", fileName, filter);
        return new ReporteExcelFile(fileName, content);
    }

    @Override
    public ReporteExcelFile vendedoresExcel(ReporteFiltroRequest filter, String generatedBy) {
        List<ReporteVendedorDTO> rows = reporteService.vendedores(filter);
        String fileName = fileNameUtil.reporte("reporte_vendedores", filter);
        byte[] content = excel.buildWorkbook("REPORTE DE DESEMPENO POR VENDEDOR", "Vendedores", generatedBy, filter, List.of(
                col("ID vendedor", ReporteVendedorDTO::idVendedor),
                col("Vendedor", ReporteVendedorDTO::nombreVendedor),
                col("Cotizaciones generadas", ReporteVendedorDTO::cotizacionesGeneradas),
                col("Cotizaciones aprobadas", ReporteVendedorDTO::cotizacionesAprobadas),
                col("Cotizaciones vencidas", ReporteVendedorDTO::cotizacionesVencidas),
                col("Cotizaciones rechazadas", ReporteVendedorDTO::cotizacionesRechazadas),
                col("Importe cotizado", ReporteVendedorDTO::importeTotalCotizado),
                col("Importe aprobado", ReporteVendedorDTO::importeTotalAprobado),
                col("Tasa conversion %", ReporteVendedorDTO::tasaConversion)
        ), rows);
        audit("vendedores", fileName, filter);
        return new ReporteExcelFile(fileName, content);
    }

    @Override
    public ReporteExcelFile productosMasCotizadosExcel(ReporteFiltroRequest filter, String generatedBy) {
        List<ReporteTopProductoDTO> rows = reporteService.productosMasCotizados(filter);
        String fileName = fileNameUtil.reporte("reporte_productos_mas_cotizados", filter);
        byte[] content = excel.buildWorkbook("REPORTE DE PRODUCTOS MAS COTIZADOS", "Top productos", generatedBy, filter, List.of(
                col("Ranking", ReporteTopProductoDTO::ranking),
                col("ID producto", ReporteTopProductoDTO::idProducto),
                col("Producto", ReporteTopProductoDTO::producto),
                col("Unidad", ReporteTopProductoDTO::unidadMedida),
                col("Cantidad total", ReporteTopProductoDTO::cantidadTotalCotizada),
                col("Importe cotizado", ReporteTopProductoDTO::importeTotalCotizado),
                col("Numero cotizaciones", ReporteTopProductoDTO::numeroCotizaciones)
        ), rows);
        audit("productos-mas-cotizados", fileName, filter);
        return new ReporteExcelFile(fileName, content);
    }

    @Override
    public ReporteExcelFile clientesTopExcel(ReporteFiltroRequest filter, String generatedBy) {
        List<ReporteTopClienteDTO> rows = reporteService.clientesTop(filter);
        String fileName = fileNameUtil.reporte("reporte_clientes_top", filter);
        byte[] content = excel.buildWorkbook("REPORTE DE CLIENTES CON MAYOR COTIZACION", "Top clientes", generatedBy, filter, List.of(
                col("ID cliente", ReporteTopClienteDTO::idCliente),
                col("RUC", ReporteTopClienteDTO::ruc),
                col("Razon social", ReporteTopClienteDTO::razonSocial),
                col("Tipo cliente", ReporteTopClienteDTO::tipoCliente),
                col("Vendedor asignado", ReporteTopClienteDTO::vendedorAsignado),
                col("Cantidad cotizaciones", ReporteTopClienteDTO::cantidadCotizaciones),
                col("Importe cotizado", ReporteTopClienteDTO::importeTotalCotizado),
                col("Importe aprobado", ReporteTopClienteDTO::importeTotalAprobado),
                col("Ultima cotizacion", ReporteTopClienteDTO::ultimaFechaCotizacion)
        ), rows);
        audit("clientes-top", fileName, filter);
        return new ReporteExcelFile(fileName, content);
    }

    private <T> ExcelExportUtil.Column<T> col(String header, java.util.function.Function<T, Object> value) {
        return new ExcelExportUtil.Column<>(header, value);
    }

    private void audit(String reportCode, String fileName, ReporteFiltroRequest filter) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("reporte", reportCode);
        payload.put("archivo", fileName);
        payload.put("filtros", filter);
        auditoriaService.registrarAccion("REPORTES", reportCode, "EXPORT_REPORT", null, payload, "REPORTES", "Exportacion de reporte " + reportCode);
    }
}

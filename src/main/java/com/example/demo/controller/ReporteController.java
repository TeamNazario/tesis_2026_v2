package com.example.demo.controller;

import com.example.demo.dto.ReporteExcelFile;
import com.example.demo.dto.ReporteFiltroRequest;
import com.example.demo.security.AuthenticatedUser;
import com.example.demo.service.ReporteExcelService;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteController {
    private static final MediaType XLSX = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final ReporteExcelService excelService;

    public ReporteController(ReporteExcelService excelService) {
        this.excelService = excelService;
    }

    @GetMapping("/cotizaciones/excel")
    public ResponseEntity<byte[]> cotizacionesExcel(FilterParams params, @AuthenticationPrincipal AuthenticatedUser user) {
        return excel(excelService.cotizacionesExcel(params.toFilter(), actor(user)));
    }

    @GetMapping("/cotizaciones-detalle/excel")
    public ResponseEntity<byte[]> cotizacionesDetalleExcel(FilterParams params, @AuthenticationPrincipal AuthenticatedUser user) {
        return excel(excelService.cotizacionesDetalleExcel(params.toFilter(), actor(user)));
    }

    @GetMapping("/clientes/excel")
    public ResponseEntity<byte[]> clientesExcel(FilterParams params, @AuthenticationPrincipal AuthenticatedUser user) {
        return excel(excelService.clientesExcel(params.toFilter(), actor(user)));
    }

    @GetMapping("/productos/excel")
    public ResponseEntity<byte[]> productosExcel(FilterParams params, @AuthenticationPrincipal AuthenticatedUser user) {
        return excel(excelService.productosExcel(params.toFilter(), actor(user)));
    }

    @GetMapping("/precios-tipo-cliente/excel")
    public ResponseEntity<byte[]> preciosTipoClienteExcel(FilterParams params, @AuthenticationPrincipal AuthenticatedUser user) {
        return excel(excelService.preciosTipoClienteExcel(params.toFilter(), actor(user)));
    }

    @GetMapping("/stock/excel")
    public ResponseEntity<byte[]> stockExcel(FilterParams params, @AuthenticationPrincipal AuthenticatedUser user) {
        return excel(excelService.stockExcel(params.toFilter(), actor(user)));
    }

    @GetMapping("/vendedores/excel")
    public ResponseEntity<byte[]> vendedoresExcel(FilterParams params, @AuthenticationPrincipal AuthenticatedUser user) {
        return excel(excelService.vendedoresExcel(params.toFilter(), actor(user)));
    }

    @GetMapping("/productos-mas-cotizados/excel")
    public ResponseEntity<byte[]> productosMasCotizadosExcel(FilterParams params, @AuthenticationPrincipal AuthenticatedUser user) {
        return excel(excelService.productosMasCotizadosExcel(params.toFilter(), actor(user)));
    }

    @GetMapping("/clientes-top/excel")
    public ResponseEntity<byte[]> clientesTopExcel(FilterParams params, @AuthenticationPrincipal AuthenticatedUser user) {
        return excel(excelService.clientesTopExcel(params.toFilter(), actor(user)));
    }

    private ResponseEntity<byte[]> excel(ReporteExcelFile file) {
        return ResponseEntity.ok()
                .contentType(XLSX)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(file.fileName()).build().toString())
                .body(file.content());
    }

    private String actor(AuthenticatedUser user) {
        if (user != null && user.getUsuario() != null && user.getUsuario().correo != null) {
            return user.getUsuario().correo;
        }
        return "system@biofluid.local";
    }

    public record FilterParams(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Integer idEstadoCotizacion,
            @RequestParam(required = false) Integer idCliente,
            @RequestParam(required = false) Integer idVendedor,
            @RequestParam(required = false) Integer idProducto,
            @RequestParam(required = false) Integer idTipoCliente,
            @RequestParam(required = false) Integer idEstadoProducto,
            @RequestParam(required = false) String moneda,
            @RequestParam(required = false) String departamento,
            @RequestParam(required = false) String provincia,
            @RequestParam(required = false) String distrito,
            @RequestParam(required = false) Boolean stockBajo,
            @RequestParam(required = false) String search
    ) {
        ReporteFiltroRequest toFilter() {
            return new ReporteFiltroRequest(
                    fechaInicio,
                    fechaFin,
                    idEstadoCotizacion,
                    idCliente,
                    idVendedor,
                    idProducto,
                    idTipoCliente,
                    idEstadoProducto,
                    moneda,
                    departamento,
                    provincia,
                    distrito,
                    stockBajo,
                    search
            );
        }
    }
}

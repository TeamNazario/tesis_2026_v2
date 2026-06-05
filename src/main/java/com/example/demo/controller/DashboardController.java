package com.example.demo.controller;

import com.example.demo.dto.CotizacionPorEstadoResponse;
import com.example.demo.dto.CotizacionPorVendedorResponse;
import com.example.demo.dto.CotizacionTendenciaResponse;
import com.example.demo.dto.DashboardResumenResponse;
import com.example.demo.dto.ProductoStockBajoResponse;
import com.example.demo.dto.StockProductoDashboardResponse;
import com.example.demo.dto.TopClienteResponse;
import com.example.demo.dto.TopProductoResponse;
import com.example.demo.service.DashboardService;
import com.example.demo.service.DashboardService.DashboardFilter;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/resumen")
    public DashboardResumenResponse resumen(FilterParams params) {
        return service.resumen(params.toFilter());
    }

    @GetMapping("/cotizaciones-tendencia")
    public List<CotizacionTendenciaResponse> cotizacionesTendencia(FilterParams params) {
        return service.cotizacionesTendencia(params.toFilter());
    }

    @GetMapping("/cotizaciones-por-estado")
    public List<CotizacionPorEstadoResponse> cotizacionesPorEstado(FilterParams params) {
        return service.cotizacionesPorEstado(params.toFilter());
    }

    @GetMapping("/top-productos")
    public List<TopProductoResponse> topProductos(FilterParams params, @RequestParam(required = false) Integer limit) {
        return service.topProductos(params.toFilter(), limit == null ? 10 : limit);
    }

    @GetMapping("/top-clientes")
    public List<TopClienteResponse> topClientes(FilterParams params, @RequestParam(required = false) Integer limit) {
        return service.topClientes(params.toFilter(), limit == null ? 10 : limit);
    }

    @GetMapping("/cotizaciones-por-vendedor")
    public List<CotizacionPorVendedorResponse> cotizacionesPorVendedor(FilterParams params, @RequestParam(required = false) Integer limit) {
        return service.cotizacionesPorVendedor(params.toFilter(), limit == null ? 10 : limit);
    }

    @GetMapping("/stock-productos")
    public List<StockProductoDashboardResponse> stockProductos(FilterParams params, @RequestParam(required = false) Integer limit) {
        return service.stockProductos(params.toFilter(), limit == null ? 10 : limit);
    }

    @GetMapping("/productos-stock-bajo")
    public List<ProductoStockBajoResponse> productosStockBajo(FilterParams params, @RequestParam(required = false) Integer limit) {
        return service.productosStockBajo(params.toFilter(), limit == null ? 10 : limit);
    }

    public record FilterParams(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Integer idVendedor,
            @RequestParam(required = false) Integer idCliente,
            @RequestParam(required = false) Integer idProducto,
            @RequestParam(required = false) Integer idTipoCliente,
            @RequestParam(required = false) Integer idEstadoCotizacion,
            @RequestParam(required = false) String moneda
    ) {
        DashboardFilter toFilter() {
            return new DashboardFilter(
                    fechaInicio,
                    fechaFin,
                    idVendedor,
                    idCliente,
                    idProducto,
                    idTipoCliente,
                    idEstadoCotizacion,
                    moneda
            );
        }
    }
}

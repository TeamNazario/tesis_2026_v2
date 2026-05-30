package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CotizacionV1Response(
        Integer idCotizacion,
        Integer idCliente,
        String cliente,
        Integer idVendedor,
        String vendedor,
        LocalDateTime fechaEmision,
        LocalDateTime fechaVencimiento,
        String moneda,
        BigDecimal subtotal,
        BigDecimal igv,
        BigDecimal importeTotal,
        String direccionDespacho,
        String depProvDis,
        Boolean flagCubierto,
        String observaciones,
        Integer idEstadoCotizacion,
        String estadoCotizacion,
        String pdfPath,
        List<DetalleCotizacionV1Response> detalles
) {}

package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CotizacionV1Response(
        Integer idCotizacion,
        Integer idCliente,
        String rucCliente,
        String razonSocialCliente,
        String direccionCliente,
        Integer idVendedor,
        String nombreVendedor,
        LocalDateTime fechaEmision,
        LocalDateTime fechaVencimiento,
        String moneda,
        BigDecimal subtotal,
        BigDecimal igv,
        BigDecimal importeTotal,
        String direccionDespacho,
        String depProvDis,
        Integer flagCubierto,
        String observaciones,
        Integer idEstadoCotizacion,
        String descEstadoCotizacion,
        Boolean vencida,
        Boolean puedeAprobarse,
        Long tiempoRestanteSegundos,
        String pdfPath,
        List<DetalleCotizacionV1Response> detalles
) {}

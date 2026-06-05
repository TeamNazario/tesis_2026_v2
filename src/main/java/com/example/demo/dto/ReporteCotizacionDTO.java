package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReporteCotizacionDTO(
        Integer idCotizacion,
        String numeroCotizacion,
        LocalDateTime fechaEmision,
        LocalDateTime fechaVencimiento,
        String estado,
        String cliente,
        String ruc,
        String vendedor,
        String moneda,
        BigDecimal subtotal,
        BigDecimal igv,
        BigDecimal importeTotal,
        String direccionDespacho,
        String observaciones,
        String pdfGenerado,
        Long cantidadProductos
) {}

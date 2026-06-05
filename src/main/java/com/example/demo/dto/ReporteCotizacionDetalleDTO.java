package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReporteCotizacionDetalleDTO(
        Integer idCotizacion,
        LocalDateTime fechaEmision,
        String estadoCotizacion,
        String cliente,
        String ruc,
        String vendedor,
        String producto,
        String unidadMedida,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal importe,
        String moneda
) {}

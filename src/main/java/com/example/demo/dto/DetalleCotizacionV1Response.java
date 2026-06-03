package com.example.demo.dto;

import java.math.BigDecimal;

public record DetalleCotizacionV1Response(
        Integer idDetalleCoti,
        Integer idCotizacion,
        Integer idProducto,
        String nombreProducto,
        String unidadMedida,
        Integer cantidad,
        BigDecimal precioUni,
        BigDecimal importe
) {}

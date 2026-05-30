package com.example.demo.dto;

import java.math.BigDecimal;

public record DetalleCotizacionV1Response(
        Integer idDetalleCoti,
        Integer idCotizacion,
        Integer idProducto,
        String producto,
        Integer cantidad,
        BigDecimal precioUni
) {}

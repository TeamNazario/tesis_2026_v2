package com.example.demo.dto;

import java.math.BigDecimal;

public record CotizacionCalcularItemResponse(
        Integer idProducto,
        String nombreProducto,
        String unidadMedida,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal importe,
        String moneda
) {}

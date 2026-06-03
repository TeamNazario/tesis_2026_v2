package com.example.demo.dto;

import java.math.BigDecimal;

public record CotizacionPrecioProductoResponse(
        Integer idProducto,
        String nombreProducto,
        String unidadMedida,
        Integer idTipoCliente,
        String descTipoCliente,
        BigDecimal precioUnitario,
        String moneda
) {}

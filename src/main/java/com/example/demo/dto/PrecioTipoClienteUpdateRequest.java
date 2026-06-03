package com.example.demo.dto;

import java.math.BigDecimal;

public record PrecioTipoClienteUpdateRequest(
        BigDecimal precioUnitario,
        String moneda,
        Integer idTipoCliente,
        Integer idEstadoProducto,
        Integer idProducto
) {}

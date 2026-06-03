package com.example.demo.dto;

import java.math.BigDecimal;

public record PrecioTipoClienteResponse(
        Integer idPrecio,
        BigDecimal precioUnitario,
        String moneda,
        Integer idTipoCliente,
        String tipoCliente,
        Integer idEstadoProducto,
        String estadoProducto,
        Integer idProducto,
        String producto
) {}

package com.example.demo.dto;

public record ProductoV1Response(
        Integer idProducto,
        String nombreProducto,
        String unidadMedida,
        java.math.BigDecimal peso,
        java.math.BigDecimal volumen,
        Integer stockFisico,
        Integer stockReservado,
        Integer stockDisponible,
        Integer stockMinimo,
        Integer cantMinVenta,
        Integer idEstadoProducto,
        String estadoProducto
) {}

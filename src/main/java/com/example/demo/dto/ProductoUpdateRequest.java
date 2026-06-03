package com.example.demo.dto;

import java.math.BigDecimal;

public record ProductoUpdateRequest(
        String nombreProducto,
        String unidadMedida,
        BigDecimal peso,
        BigDecimal volumen,
        Integer stockFisico,
        Integer stockReservado,
        Integer stockDisponible,
        Integer stockMinimo,
        Integer cantMinVenta,
        Integer idEstadoProducto
) {}

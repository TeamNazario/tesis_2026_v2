package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReporteProductoDTO(
        Integer idProducto,
        String nombreProducto,
        String unidadMedida,
        BigDecimal peso,
        BigDecimal volumen,
        Integer stockFisico,
        Integer stockReservado,
        Integer stockDisponible,
        Integer stockMinimo,
        Integer cantidadMinimaVenta,
        String estadoProducto,
        LocalDateTime fechaRegistro,
        LocalDateTime fechaActualizacion
) {}

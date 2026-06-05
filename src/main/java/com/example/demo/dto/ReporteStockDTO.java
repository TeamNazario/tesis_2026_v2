package com.example.demo.dto;

public record ReporteStockDTO(
        Integer idProducto,
        String producto,
        String unidadMedida,
        Integer stockFisico,
        Integer stockReservado,
        Integer stockDisponible,
        Integer stockMinimo,
        Integer cantidadMinimaVenta,
        String stockBajo,
        String estadoProducto
) {}

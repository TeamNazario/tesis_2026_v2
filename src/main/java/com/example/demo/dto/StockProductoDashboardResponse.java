package com.example.demo.dto;

public record StockProductoDashboardResponse(
        Integer idProducto,
        String nombreProducto,
        String unidadMedida,
        Integer stockFisico,
        Integer stockReservado,
        Integer stockDisponible,
        Integer stockMinimo,
        Boolean stockBajo
) {}

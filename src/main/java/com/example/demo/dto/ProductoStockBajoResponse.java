package com.example.demo.dto;

public record ProductoStockBajoResponse(
        Integer idProducto,
        String nombreProducto,
        String unidadMedida,
        Integer stockDisponible,
        Integer stockMinimo
) {}

package com.example.demo.dto;

import java.math.BigDecimal;

public record ProductoResponse(
        Integer idProducto,
        String nombreProducto,
        String descripcionTecnica,
        String unidadMedida,
        BigDecimal precioBaseUnitario,
        BigDecimal concentracionUreaAus32,
        Integer stockFisico,
        Integer stockReservado,
        Integer stockDisponible,
        Integer stockMinimoSeguridad,
        ReferenceResponse estado
) {
}

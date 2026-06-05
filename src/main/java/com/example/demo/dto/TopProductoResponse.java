package com.example.demo.dto;

import java.math.BigDecimal;

public record TopProductoResponse(
        Integer idProducto,
        String nombreProducto,
        String unidadMedida,
        Long cantidadCotizada,
        BigDecimal importeTotal
) {}

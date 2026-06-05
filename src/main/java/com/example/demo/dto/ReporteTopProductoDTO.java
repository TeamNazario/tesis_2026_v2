package com.example.demo.dto;

import java.math.BigDecimal;

public record ReporteTopProductoDTO(
        Integer ranking,
        Integer idProducto,
        String producto,
        String unidadMedida,
        Long cantidadTotalCotizada,
        BigDecimal importeTotalCotizado,
        Long numeroCotizaciones
) {}

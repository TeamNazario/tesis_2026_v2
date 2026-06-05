package com.example.demo.dto;

import java.math.BigDecimal;

public record CotizacionPorVendedorResponse(
        Integer idVendedor,
        String nombreVendedor,
        Long cantidadCotizaciones,
        Long cotizacionesAprobadas,
        BigDecimal importeTotal
) {}

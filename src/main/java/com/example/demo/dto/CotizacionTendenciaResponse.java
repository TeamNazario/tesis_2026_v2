package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CotizacionTendenciaResponse(
        LocalDate fecha,
        Long cantidadCotizaciones,
        BigDecimal importeTotal
) {}

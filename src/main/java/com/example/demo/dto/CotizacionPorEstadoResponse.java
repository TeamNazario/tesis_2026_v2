package com.example.demo.dto;

import java.math.BigDecimal;

public record CotizacionPorEstadoResponse(
        String estado,
        Long cantidad,
        BigDecimal importeTotal
) {}

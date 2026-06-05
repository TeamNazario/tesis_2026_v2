package com.example.demo.dto;

import java.math.BigDecimal;

public record ReporteVendedorDTO(
        Integer idVendedor,
        String nombreVendedor,
        Long cotizacionesGeneradas,
        Long cotizacionesAprobadas,
        Long cotizacionesVencidas,
        Long cotizacionesRechazadas,
        BigDecimal importeTotalCotizado,
        BigDecimal importeTotalAprobado,
        BigDecimal tasaConversion
) {}

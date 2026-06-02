package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

public record CotizacionCalcularResumenResponse(
        List<CotizacionCalcularItemResponse> items,
        BigDecimal subtotal,
        BigDecimal igv,
        BigDecimal importeTotal,
        String moneda
) {}

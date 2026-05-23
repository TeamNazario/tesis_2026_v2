package com.example.demo.dto;

import java.math.BigDecimal;

public record CotizacionDetalleResponse(
        Integer idDetalle,
        ReferenceResponse producto,
        Integer cantidad,
        BigDecimal precioUnitarioAplicado,
        BigDecimal subtotalLinea
) {
}

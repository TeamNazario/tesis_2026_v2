package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CotizacionResponse(
        Integer idCotizacion,
        String uuidPublico,
        ReferenceResponse cliente,
        ReferenceResponse vendedor,
        LocalDateTime fechaEmision,
        LocalDateTime fechaVencimiento,
        BigDecimal subtotal,
        BigDecimal igv,
        BigDecimal montoTotal,
        String origenCotizacion,
        String estadoCotizacion,
        String pdfPath,
        List<CotizacionDetalleResponse> detalles
) {
}

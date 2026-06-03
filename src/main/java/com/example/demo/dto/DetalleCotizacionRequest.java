package com.example.demo.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record DetalleCotizacionRequest(
        @NotNull Integer idProducto,
        @NotNull @Positive Integer cantidad,
        @DecimalMin("0.01") BigDecimal precioUni
) {}

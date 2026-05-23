package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CotizacionDetalleUpsertRequest(
        @NotNull(message = "La cotizacion es obligatoria.")
        Integer idCotizacion,

        @NotNull(message = "El producto es obligatorio.")
        Integer idProducto,

        @NotNull(message = "La cantidad es obligatoria.")
        @Min(value = 1, message = "La cantidad debe ser mayor o igual a 1.")
        Integer cantidad,

        @NotNull(message = "El precio unitario aplicado es obligatorio.")
        @DecimalMin(value = "0.00", message = "El precio unitario aplicado debe ser mayor o igual a 0.")
        BigDecimal precioUnitarioAplicado
) {
}

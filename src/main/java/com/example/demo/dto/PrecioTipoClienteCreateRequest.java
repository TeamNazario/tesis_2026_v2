package com.example.demo.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record PrecioTipoClienteCreateRequest(
        @NotNull @DecimalMin("0.01") BigDecimal precioUnitario,
        @NotBlank String moneda,
        @NotNull Integer idTipoCliente,
        @NotNull Integer idEstadoProducto,
        @NotNull Integer idProducto
) {}

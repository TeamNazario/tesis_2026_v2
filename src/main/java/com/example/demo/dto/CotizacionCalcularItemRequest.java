package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CotizacionCalcularItemRequest(
        @NotNull Integer idCliente,
        @NotNull Integer idProducto,
        @NotNull @Positive Integer cantidad,
        @NotBlank String moneda
) {}

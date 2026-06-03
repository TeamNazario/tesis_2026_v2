package com.example.demo.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductoCreateRequest(
        @NotBlank String nombreProducto,
        @NotBlank String unidadMedida,
        @PositiveOrZero BigDecimal peso,
        @PositiveOrZero BigDecimal volumen,
        @NotNull @PositiveOrZero Integer stockFisico,
        @NotNull @PositiveOrZero Integer stockReservado,
        @PositiveOrZero Integer stockDisponible,
        @NotNull @PositiveOrZero Integer stockMinimo,
        @NotNull @PositiveOrZero Integer cantMinVenta,
        @NotNull Integer idEstadoProducto
) {}

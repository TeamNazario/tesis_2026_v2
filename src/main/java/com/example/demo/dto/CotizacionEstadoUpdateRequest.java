package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public record CotizacionEstadoUpdateRequest(
        @NotNull Integer idEstadoCotizacion
) {}

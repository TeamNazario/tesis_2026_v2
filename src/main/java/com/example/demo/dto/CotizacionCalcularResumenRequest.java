package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CotizacionCalcularResumenRequest(
        @NotNull Integer idCliente,
        @NotBlank String moneda,
        @NotEmpty List<@Valid CotizacionResumenDetalleRequest> detalles
) {}

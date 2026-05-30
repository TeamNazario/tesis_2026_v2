package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public record CotizacionCreateRequest(
        @NotNull Integer idCliente,
        @NotNull Integer idVendedor,
        LocalDateTime fechaEmision,
        @NotNull LocalDateTime fechaVencimiento,
        @NotBlank String moneda,
        String direccionDespacho,
        String depProvDis,
        Boolean flagCubierto,
        String observaciones,
        @NotNull Integer idEstadoCotizacion,
        String pdfPath,
        @NotEmpty List<@Valid DetalleCotizacionRequest> detalles
) {}

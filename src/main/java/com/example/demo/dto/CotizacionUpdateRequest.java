package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

public record CotizacionUpdateRequest(
        Integer idCliente,
        Integer idVendedor,
        LocalDateTime fechaVencimiento,
        String moneda,
        String direccionDespacho,
        String depProvDis,
        Integer flagCubierto,
        String observaciones,
        Integer idEstadoCotizacion,
        @NotEmpty List<@Valid DetalleCotizacionRequest> detalles
) {}

package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReportePrecioTipoClienteDTO(
        Integer idPrecio,
        String producto,
        String unidadMedida,
        String tipoCliente,
        BigDecimal precioUnitario,
        String moneda,
        String estado,
        LocalDateTime fechaRegistro,
        LocalDateTime fechaActualizacion
) {}

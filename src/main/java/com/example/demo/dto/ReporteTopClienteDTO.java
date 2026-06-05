package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReporteTopClienteDTO(
        Integer idCliente,
        String ruc,
        String razonSocial,
        String tipoCliente,
        String vendedorAsignado,
        Long cantidadCotizaciones,
        BigDecimal importeTotalCotizado,
        BigDecimal importeTotalAprobado,
        LocalDateTime ultimaFechaCotizacion
) {}

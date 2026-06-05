package com.example.demo.dto;

import java.math.BigDecimal;

public record TopClienteResponse(
        Integer idCliente,
        String razonSocial,
        String ruc,
        Long cantidadCotizaciones,
        BigDecimal importeTotal
) {}

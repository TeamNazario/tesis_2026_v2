package com.example.demo.dto;

import java.time.LocalDate;

public record ReporteFiltroRequest(
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Integer idEstadoCotizacion,
        Integer idCliente,
        Integer idVendedor,
        Integer idProducto,
        Integer idTipoCliente,
        Integer idEstadoProducto,
        String moneda,
        String departamento,
        String provincia,
        String distrito,
        Boolean stockBajo,
        String search
) {}

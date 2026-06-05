package com.example.demo.dto;

import java.time.LocalDateTime;

public record ReporteClienteDTO(
        Integer idCliente,
        String ruc,
        String razonSocial,
        String condicionSunat,
        String estadoSunat,
        String direccion,
        String departamento,
        String provincia,
        String distrito,
        String ubigeo,
        String tipoCliente,
        String vendedorAsignado,
        String estadoCliente,
        LocalDateTime fechaRegistro,
        LocalDateTime fechaActualizacion
) {}

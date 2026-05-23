package com.example.demo.dto;

import java.time.LocalDateTime;

public record ClienteResponse(
        Integer idCliente,
        String ruc,
        String razonSocial,
        String nombreComercial,
        String condicionSunat,
        String estadoSunat,
        String direccion,
        String departamento,
        String provincia,
        String distrito,
        String ubigeo,
        ReferenceResponse vendedorAsignado,
        ReferenceResponse estado,
        String usuarioRegistro,
        LocalDateTime fechaRegistro
) {
}

package com.example.demo.dto;

public record ClienteUpdateRequest(
        String razonSocial,
        String condicionSunat,
        String estadoSunat,
        String direccion,
        String departamento,
        String provincia,
        String distrito,
        String ubigeo,
        Integer idVendedorAsignado,
        Integer idTipoCliente,
        Integer idEstadoClienteContacto
) {}

package com.example.demo.dto;

import java.time.LocalDateTime;

public record ClienteV1Response(
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
        Integer idVendedorAsignado,
        String vendedorAsignado,
        Integer idTipoCliente,
        String tipoCliente,
        Integer idEstadoClienteContacto,
        String estadoClienteContacto,
        String usuRegistro,
        LocalDateTime fecRegistro,
        String usuActualiza,
        LocalDateTime fecActualiza
) {}

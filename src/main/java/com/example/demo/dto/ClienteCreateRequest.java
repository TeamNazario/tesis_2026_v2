package com.example.demo.dto;

import jakarta.validation.constraints.*;

public record ClienteCreateRequest(
        @NotBlank @Pattern(regexp = "\\d{11}") String ruc,
        @NotBlank @Size(max = 200) String razonSocial,
        @NotBlank @Size(max = 50) String condicionSunat,
        @NotBlank @Size(max = 50) String estadoSunat,
        String direccion,
        String departamento,
        String provincia,
        String distrito,
        @Size(max = 6) String ubigeo,
        @NotNull Integer idVendedorAsignado,
        @NotNull Integer idTipoCliente,
        @NotNull Integer idEstadoClienteContacto
) {}

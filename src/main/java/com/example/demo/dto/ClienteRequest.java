package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank(message = "El RUC es obligatorio.")
        @Pattern(regexp = "\\d{11}", message = "El RUC debe tener 11 digitos numericos.")
        String ruc,

        @NotBlank(message = "La razon social es obligatoria.")
        @Size(max = 200, message = "La razon social no debe superar 200 caracteres.")
        String razonSocial,

        @Size(max = 200, message = "El nombre comercial no debe superar 200 caracteres.")
        String nombreComercial,

        @NotBlank(message = "La condicion SUNAT es obligatoria.")
        @Size(max = 50, message = "La condicion SUNAT no debe superar 50 caracteres.")
        String condicionSunat,

        @NotBlank(message = "El estado SUNAT es obligatorio.")
        @Size(max = 50, message = "El estado SUNAT no debe superar 50 caracteres.")
        String estadoSunat,

        String direccion,

        @Size(max = 50, message = "El departamento no debe superar 50 caracteres.")
        String departamento,

        @Size(max = 50, message = "La provincia no debe superar 50 caracteres.")
        String provincia,

        @Size(max = 50, message = "El distrito no debe superar 50 caracteres.")
        String distrito,

        @Size(max = 6, message = "El ubigeo no debe superar 6 caracteres.")
        String ubigeo,

        Integer idVendedorAsignado,

        @NotNull(message = "El estado del cliente es obligatorio.")
        Integer idEstado,

        @Size(max = 50, message = "El usuario de registro no debe superar 50 caracteres.")
        String usuarioRegistro
) {
}

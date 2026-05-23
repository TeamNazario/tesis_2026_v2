package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotNull(message = "El perfil es obligatorio.")
        Integer idPerfil,

        @NotNull(message = "El tipo de documento es obligatorio.")
        Integer idTipoDoc,

        @NotNull(message = "El estado es obligatorio.")
        Integer idEstado,

        @NotBlank(message = "El numero de documento es obligatorio.")
        @Size(max = 20, message = "El numero de documento no debe superar 20 caracteres.")
        String nroDocumento,

        @NotBlank(message = "Los nombres son obligatorios.")
        @Size(max = 100, message = "Los nombres no deben superar 100 caracteres.")
        String nombres,

        @NotBlank(message = "El apellido paterno es obligatorio.")
        @Size(max = 100, message = "El apellido paterno no debe superar 100 caracteres.")
        String apellidoPaterno,

        @Size(max = 100, message = "El apellido materno no debe superar 100 caracteres.")
        String apellidoMaterno,

        @NotBlank(message = "El correo es obligatorio.")
        @Email(message = "El correo debe tener un formato valido.")
        @Size(max = 150, message = "El correo no debe superar 150 caracteres.")
        String correo,

        @Size(max = 20, message = "El celular no debe superar 20 caracteres.")
        String celular,

        @NotBlank(message = "La contrasena es obligatoria.")
        @Size(max = 255, message = "La contrasena no debe superar 255 caracteres.")
        String password
) {
}

package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotNull(message = "El perfil es obligatorio.")
        Integer idPerfil,

        @NotNull(message = "El tipo de documento es obligatorio.")
        Integer idTipoDoc,

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

        @NotBlank(message = "La contrasena hash es obligatoria.")
        @Size(max = 255, message = "La contrasena hash no debe superar 255 caracteres.")
        String passwordHash,

        @NotNull(message = "Los intentos fallidos son obligatorios.")
        @Min(value = 0, message = "Los intentos fallidos deben ser mayor o igual a 0.")
        Integer intentosFallidos,

        @NotNull(message = "El estado del usuario es obligatorio.")
        Integer idEstado,

        @Size(max = 50, message = "El usuario de registro no debe superar 50 caracteres.")
        String usuarioRegistro,

        @Size(max = 50, message = "El usuario de actualizacion no debe superar 50 caracteres.")
        String usuarioActualiza
) {
}

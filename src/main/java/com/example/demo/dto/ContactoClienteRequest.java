package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ContactoClienteRequest(
        @NotNull
        Integer idCliente,

        @NotNull
        Integer idTipoDoc,

        @NotBlank
        @Size(max = 20)
        String nroDocumento,

        @NotBlank
        @Size(max = 100)
        String nombre,

        @NotBlank
        @Size(max = 100)
        String apellidoPaterno,

        @Size(max = 100)
        String apellidoMaterno,

        @Email
        @Size(max = 150)
        String correo,

        @Size(max = 20)
        String celular,

        @NotNull
        Integer idEstado
) {
}

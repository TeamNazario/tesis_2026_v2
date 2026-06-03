package com.example.demo.dto;

import jakarta.validation.constraints.*;

public record ContactoClienteCreateRequest(
        @NotNull Integer idTipoDoc,
        @NotBlank @Size(max = 20) String nroDocumento,
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 100) String apellidoPaterno,
        @Size(max = 100) String apellidoMaterno,
        @Email String correo,
        String celular,
        @NotNull Integer idEstadoClienteContacto
) {}

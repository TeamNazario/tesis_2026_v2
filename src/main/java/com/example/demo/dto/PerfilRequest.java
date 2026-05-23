package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PerfilRequest(
        @NotBlank
        @Size(max = 100)
        String nombrePerfil,

        @Size(max = 255)
        String descPerfil,

        @NotNull
        Integer idEstado
) {
}

package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ZonaDespachoRequest(
        @NotBlank
        @Size(max = 50)
        String departamento,

        @NotBlank
        @Size(max = 50)
        String provincia,

        @NotNull
        Boolean coberturaEstandar,

        @NotNull
        @Min(0)
        Integer tiempoEntregaHoras
) {
}

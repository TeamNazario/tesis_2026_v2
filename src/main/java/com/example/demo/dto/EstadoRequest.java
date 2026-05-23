package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EstadoRequest(
        @NotBlank
        @Size(max = 50)
        String descEstado
) {
}

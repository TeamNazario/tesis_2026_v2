package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public record ContactoClienteEstadoPatchRequest(
        @NotNull Integer idEstadoClienteContacto
) {
}

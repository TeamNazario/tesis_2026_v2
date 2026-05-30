package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public record ClienteEstadoPatchRequest(
        @NotNull Integer idEstadoClienteContacto
) {
}

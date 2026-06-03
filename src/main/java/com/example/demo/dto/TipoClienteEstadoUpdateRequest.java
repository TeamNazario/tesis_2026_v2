package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;

public record TipoClienteEstadoUpdateRequest(
        @NotNull Integer idEstadoClienteContacto
) {}

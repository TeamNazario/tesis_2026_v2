package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TipoClienteUpdateRequest(
        @NotBlank @Size(max = 200) String descTipoCliente,
        @NotNull Integer idEstadoClienteContacto
) {}

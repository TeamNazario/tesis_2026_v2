package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record LogInventarioRequest(
        @NotNull
        Integer idProducto,

        Integer idUsuario,

        @NotBlank
        @Size(max = 19)
        String tipoMovimiento,

        @NotNull
        @Min(1)
        Integer cantidad,

        @NotNull
        @Min(0)
        Integer stockFisicoMomento,

        @NotNull
        @Min(0)
        Integer stockReservadoMomento,

        LocalDateTime fechaEvento
) {
}

package com.example.demo.dto;

import java.time.LocalDateTime;

public record LogInventarioResponse(
        Long idLogInventario,
        ReferenceResponse producto,
        Integer idUsuario,
        String tipoMovimiento,
        Integer cantidad,
        Integer stockFisicoMomento,
        Integer stockReservadoMomento,
        LocalDateTime fechaEvento
) {
}

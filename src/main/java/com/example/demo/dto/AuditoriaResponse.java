package com.example.demo.dto;

import java.time.LocalDateTime;

public record AuditoriaResponse(
        Long idLogAuditoria,
        String nombreTabla,
        String idRegistro,
        String accion,
        String campoModificado,
        String valorAnterior,
        String valorNuevo,
        String valoresAnterioresJson,
        String valoresNuevosJson,
        Long idUsuario,
        String correoUsuario,
        String nombreUsuario,
        String endpoint,
        String metodoHttp,
        String modulo,
        String ipOrigen,
        String userAgent,
        LocalDateTime fechaEvento,
        String observacion
) {}

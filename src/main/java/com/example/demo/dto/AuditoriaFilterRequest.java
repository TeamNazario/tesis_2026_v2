package com.example.demo.dto;

import java.time.LocalDate;

public record AuditoriaFilterRequest(
        String nombreTabla,
        String idRegistro,
        String accion,
        Long idUsuario,
        String correoUsuario,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String modulo
) {}

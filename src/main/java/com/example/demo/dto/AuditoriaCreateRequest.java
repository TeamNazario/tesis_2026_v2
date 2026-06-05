package com.example.demo.dto;

public record AuditoriaCreateRequest(
        String nombreTabla,
        String idRegistro,
        String accion,
        Object valoresAnteriores,
        Object valoresNuevos,
        String modulo,
        String observacion
) {}

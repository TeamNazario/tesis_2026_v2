package com.example.demo.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public record HistorialChatResponse(
        Long idHistorial,
        String llaveSesion,
        String proceso,
        String fechaInicio,
        String fechaFin,
        String conversacion,
        String estado,
        String usuRegistro
) {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final ZoneId LIMA = ZoneId.of("America/Lima");

    public static String formatear(LocalDateTime dt) {
        if (dt == null) return null;
        return dt.atZone(ZoneId.of("UTC")).withZoneSameInstant(LIMA).format(FMT);
    }
}

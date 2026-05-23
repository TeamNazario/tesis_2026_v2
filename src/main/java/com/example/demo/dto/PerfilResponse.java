package com.example.demo.dto;

public record PerfilResponse(
        Integer idPerfil,
        String nombrePerfil,
        String descPerfil,
        ReferenceResponse estado
) {
}

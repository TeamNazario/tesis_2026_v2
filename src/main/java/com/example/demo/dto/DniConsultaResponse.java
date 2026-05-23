package com.example.demo.dto;

public record DniConsultaResponse(
        String dni,
        String nombres,
        String apellidoPaterno,
        String apellidoMaterno,
        String codVerifica,
        String nombreCompleto
) {
}

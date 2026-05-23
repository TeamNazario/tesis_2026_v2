package com.example.demo.dto;

public record ZonaDespachoResponse(
        Integer idZona,
        String departamento,
        String provincia,
        Boolean coberturaEstandar,
        Integer tiempoEntregaHoras
) {
}

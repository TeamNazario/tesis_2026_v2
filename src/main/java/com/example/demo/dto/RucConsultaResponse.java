package com.example.demo.dto;

import java.util.List;

public record RucConsultaResponse(
        String ruc,
        String razonSocial,
        String nombreComercial,
        String estado,
        String condicion,
        String direccion,
        String departamento,
        String provincia,
        String distrito,
        String ubigeo,
        List<String> telefonos,
        String capital,
        boolean activo,
        boolean habido,
        boolean aptoParaCotizacion,
        String mensajeValidacion
) {
}

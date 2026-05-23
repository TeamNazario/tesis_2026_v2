package com.example.demo.dto.external;

import java.util.List;

public record ApisPeruRucResponse(
        String ruc,
        String razonSocial,
        String nombreComercial,
        List<String> telefonos,
        String estado,
        String condicion,
        String direccion,
        String departamento,
        String provincia,
        String distrito,
        String ubigeo,
        String capital
) {
}

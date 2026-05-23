package com.example.demo.dto;

public record ContactoClienteResponse(
        Integer idContacto,
        ReferenceResponse cliente,
        ReferenceResponse tipoDocumento,
        String nroDocumento,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String correo,
        String celular,
        ReferenceResponse estado
) {
}

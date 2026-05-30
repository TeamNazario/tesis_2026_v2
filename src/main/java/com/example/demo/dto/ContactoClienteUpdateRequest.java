package com.example.demo.dto;

public record ContactoClienteUpdateRequest(
        Integer idTipoDoc,
        String nroDocumento,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String correo,
        String celular,
        Integer idEstadoClienteContacto
) {}

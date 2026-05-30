package com.example.demo.dto;

public record ContactoClienteV1Response(
        Integer idContacto,
        Integer idCliente,
        Integer idTipoDoc,
        String tipoDocumento,
        String nroDocumento,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String correo,
        String celular,
        Integer idEstadoClienteContacto,
        String estadoClienteContacto
) {}

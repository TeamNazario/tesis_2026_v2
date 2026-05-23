package com.example.demo.dto;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Integer idUsuario,
        ReferenceResponse perfil,
        ReferenceResponse tipoDocumento,
        String nroDocumento,
        String nombres,
        String apellidoPaterno,
        String apellidoMaterno,
        String correo,
        String celular,
        Integer intentosFallidos,
        ReferenceResponse estado,
        String usuarioRegistro,
        LocalDateTime fechaRegistro,
        String usuarioActualiza,
        LocalDateTime fechaActualiza
) {
}

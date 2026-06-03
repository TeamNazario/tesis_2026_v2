package com.example.demo.dto;

import java.time.LocalDateTime;

public record TipoClienteResponse(
        Integer idTipoCliente,
        String descTipoCliente,
        Integer idEstadoClienteContacto,
        String desEstadoClienteContacto,
        String usuRegistro,
        LocalDateTime fecRegistro,
        String usuActualiza,
        LocalDateTime fecActualiza
) {}

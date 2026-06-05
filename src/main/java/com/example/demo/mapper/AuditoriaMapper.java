package com.example.demo.mapper;

import com.example.demo.dto.AuditoriaResponse;
import com.example.demo.model.LogAuditoria;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaMapper {
    public AuditoriaResponse toResponse(LogAuditoria log) {
        return new AuditoriaResponse(
                log.idLogAuditoria,
                log.nombreTabla,
                log.idRegistro,
                log.accion,
                log.campoModificado,
                log.valorAnterior,
                log.valorNuevo,
                log.valoresAnterioresJson,
                log.valoresNuevosJson,
                log.idUsuario,
                log.correoUsuario,
                log.nombreUsuario,
                log.endpoint,
                log.metodoHttp,
                log.modulo,
                log.ipOrigen,
                log.userAgent,
                log.fechaEvento,
                log.observacion
        );
    }
}

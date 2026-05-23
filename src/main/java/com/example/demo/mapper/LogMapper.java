package com.example.demo.mapper;

import com.example.demo.dto.LogEficienciaChatbotRequest;
import com.example.demo.dto.LogEficienciaChatbotResponse;
import com.example.demo.dto.LogInventarioRequest;
import com.example.demo.dto.LogInventarioResponse;
import com.example.demo.model.LogEficienciaChatbot;
import com.example.demo.model.LogInventario;
import com.example.demo.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class LogMapper {
    private final ReferenceMapper referenceMapper;

    public LogMapper(ReferenceMapper referenceMapper) {
        this.referenceMapper = referenceMapper;
    }

    public LogInventario toEntity(LogInventarioRequest request, Producto producto) {
        LogInventario log = new LogInventario();
        updateEntity(log, request, producto);
        return log;
    }

    public void updateEntity(LogInventario log, LogInventarioRequest request, Producto producto) {
        log.producto = producto;
        log.idUsuario = request.idUsuario();
        log.tipoMovimiento = request.tipoMovimiento();
        log.cantidad = request.cantidad();
        log.stockFisicoMomento = request.stockFisicoMomento();
        log.stockReservadoMomento = request.stockReservadoMomento();
        log.fechaEvento = request.fechaEvento();
    }

    public LogInventarioResponse toResponse(LogInventario log) {
        return new LogInventarioResponse(
                log.idLogInventario,
                referenceMapper.toReference(log.producto),
                log.idUsuario,
                log.tipoMovimiento,
                log.cantidad,
                log.stockFisicoMomento,
                log.stockReservadoMomento,
                log.fechaEvento
        );
    }

    public LogEficienciaChatbot toEntity(LogEficienciaChatbotRequest request) {
        LogEficienciaChatbot log = new LogEficienciaChatbot();
        updateEntity(log, request);
        return log;
    }

    public void updateEntity(LogEficienciaChatbot log, LogEficienciaChatbotRequest request) {
        log.sessionIdWhatsapp = request.sessionIdWhatsapp();
        log.rucConsultado = request.rucConsultado();
        log.intencionDetectada = request.intencionDetectada();
        log.timestampPrimerMensaje = request.timestampPrimerMensaje();
        log.timestampFinProcesamiento = request.timestampFinProcesamiento();
        log.apiSunatRespondio = request.apiSunatRespondio();
        log.pdfGeneradoExitosamente = request.pdfGeneradoExitosamente();
        log.payloadAuditoria = request.payloadAuditoria();
    }

    public LogEficienciaChatbotResponse toResponse(LogEficienciaChatbot log) {
        return new LogEficienciaChatbotResponse(
                log.idLogEficiencia,
                log.sessionIdWhatsapp,
                log.rucConsultado,
                log.intencionDetectada,
                log.timestampPrimerMensaje,
                log.timestampFinProcesamiento,
                log.tiempoAtencionSegundos,
                log.apiSunatRespondio,
                log.pdfGeneradoExitosamente,
                log.payloadAuditoria
        );
    }
}

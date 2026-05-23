package com.example.demo.dto;

import java.time.LocalDateTime;

public record LogEficienciaChatbotResponse(
        Long idLogEficiencia,
        String sessionIdWhatsapp,
        String rucConsultado,
        String intencionDetectada,
        LocalDateTime timestampPrimerMensaje,
        LocalDateTime timestampFinProcesamiento,
        Integer tiempoAtencionSegundos,
        Boolean apiSunatRespondio,
        Boolean pdfGeneradoExitosamente,
        String payloadAuditoria
) {
}

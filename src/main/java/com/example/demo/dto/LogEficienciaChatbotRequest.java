package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record LogEficienciaChatbotRequest(
        @NotBlank
        @Size(max = 100)
        String sessionIdWhatsapp,

        @Pattern(regexp = "\\d{11}")
        String rucConsultado,

        @Size(max = 50)
        String intencionDetectada,

        @NotNull
        LocalDateTime timestampPrimerMensaje,

        LocalDateTime timestampFinProcesamiento,

        @Min(0)
        Integer tiempoAtencionSegundos,

        @NotNull
        Boolean apiSunatRespondio,

        @NotNull
        Boolean pdfGeneradoExitosamente,

        String payloadAuditoria
) {
}

package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_eficiencia_chatbot")
public class LogEficienciaChatbot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log_eficiencia")
    public Long idLogEficiencia;

    @Column(name = "session_id_whatsapp", nullable = false, length = 100)
    public String sessionIdWhatsapp;

    @Column(name = "ruc_consultado", length = 11)
    public String rucConsultado;

    @Column(name = "intencion_detectada", length = 50)
    public String intencionDetectada;

    @Column(name = "timestamp_primer_mensaje", nullable = false)
    public LocalDateTime timestampPrimerMensaje;

    @Column(name = "timestamp_fin_procesamiento")
    public LocalDateTime timestampFinProcesamiento;

    @Column(name = "tiempo_atencion_segundos", insertable = false, updatable = false)
    public Integer tiempoAtencionSegundos;

    @Column(name = "api_sunat_respondió", nullable = false)
    public Boolean apiSunatRespondio;

    @Column(name = "pdf_generado_exitosamente", nullable = false)
    public Boolean pdfGeneradoExitosamente;

    @Lob
    @Column(name = "payload_auditoria", columnDefinition = "json")
    public String payloadAuditoria;
}

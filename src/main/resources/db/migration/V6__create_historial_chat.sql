-- V6: Tabla para historial de conversaciones del chatbot WhatsApp n8n
-- Registra inicio, fin, proceso y conversacion de cada flujo

CREATE TABLE IF NOT EXISTS historial_chat (
    id_historial    BIGINT AUTO_INCREMENT PRIMARY KEY,
    llave_sesion    VARCHAR(255)  NOT NULL COMMENT 'ID de sesion del chat (wa_id del cliente WhatsApp)',
    proceso         VARCHAR(50)   NOT NULL COMMENT 'Tipo de flujo: CONSULTA, CONSULTA_RUC, CALCULAR, COTIZACION, PDF, SISTEMA',
    fecha_inicio    DATETIME      NOT NULL COMMENT 'Fecha y hora de inicio en timezone Lima UTC-5 (DD/MM/AAAA HH:MM:SS)',
    fecha_fin       DATETIME      NULL     COMMENT 'Fecha y hora de fin en timezone Lima UTC-5, NULL si sigue activo',
    conversacion    LONGTEXT      NULL     COMMENT 'JSON con el historial de mensajes input/output del chat',
    estado          VARCHAR(20)   NOT NULL DEFAULT 'ACTIVO' COMMENT 'ACTIVO, COMPLETADO, ERROR',
    usu_registro    VARCHAR(100)  NULL     COMMENT 'Usuario o sistema que creo el registro',
    fec_registro    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creacion del registro'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_historial_llave_sesion ON historial_chat (llave_sesion);
CREATE INDEX idx_historial_proceso ON historial_chat (proceso);
CREATE INDEX idx_historial_fecha_inicio ON historial_chat (fecha_inicio);

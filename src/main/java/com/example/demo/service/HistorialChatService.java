package com.example.demo.service;

import com.example.demo.dto.HistorialChatFinalizarRequest;
import com.example.demo.dto.HistorialChatRequest;
import com.example.demo.dto.HistorialChatResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.HistorialChat;
import com.example.demo.repository.HistorialChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class HistorialChatService {

    private static final ZoneId LIMA = ZoneId.of("America/Lima");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final HistorialChatRepository repository;

    public HistorialChatService(HistorialChatRepository repository) {
        this.repository = repository;
    }

    private LocalDateTime ahoraLima() {
        return LocalDateTime.now(LIMA);
    }

    @Transactional
    public HistorialChatResponse iniciar(HistorialChatRequest request) {
        HistorialChat h = new HistorialChat();
        h.llaveSesion = request.llaveSesion();
        h.proceso = request.proceso();
        h.fechaInicio = ahoraLima();
        h.fechaFin = null;
        h.estado = "ACTIVO";
        h.usuRegistro = "n8n-chatbot";
        h.fecRegistro = ahoraLima();

        // Si se envía mensaje inicial, guardarlo como primer mensaje
        if (request.mensajeInicial() != null && !request.mensajeInicial().isBlank()) {
            h.conversacion = "[{\"rol\":\"usuario\",\"mensaje\":" +
                    escapeJson(request.mensajeInicial()) + "}]";
        }

        h = repository.save(h);
        return map(h);
    }

    @Transactional
    public HistorialChatResponse finalizar(Long id, HistorialChatFinalizarRequest request) {
        HistorialChat h = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HistorialChat", id));

        h.fechaFin = ahoraLima();
        h.estado = request.estado() != null ? request.estado() : "COMPLETADO";

        if (request.conversacion() != null) {
            h.conversacion = request.conversacion();
        } else if (request.mensajeFinal() != null) {
            // Append mensaje final al conversacion existente
            String conv = h.conversacion != null ? h.conversacion : "[]";
            String newMsg = "{\"rol\":\"asistente\",\"mensaje\":" + escapeJson(request.mensajeFinal()) + "}";
            if (conv.endsWith("]")) {
                h.conversacion = conv.substring(0, conv.length() - 1) +
                        (conv.length() > 2 ? "," : "") + newMsg + "]";
            }
        }

        h = repository.save(h);
        return map(h);
    }

    @Transactional(readOnly = true)
    public List<HistorialChatResponse> listar(
            String llaveSesion,
            String proceso,
            String fechaInicio,
            String fechaFin) {

        LocalDateTime desde = parseFecha(fechaInicio, false);
        LocalDateTime hasta = parseFecha(fechaFin, true);

        return repository.buscar(llaveSesion, proceso, desde, hasta)
                .stream()
                .map(this::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public HistorialChatResponse obtener(Long id) {
        return map(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HistorialChat", id)));
    }

    private HistorialChatResponse map(HistorialChat h) {
        return new HistorialChatResponse(
                h.idHistorial,
                h.llaveSesion,
                h.proceso,
                formatear(h.fechaInicio),
                formatear(h.fechaFin),
                h.conversacion,
                h.estado,
                h.usuRegistro
        );
    }

    private String formatear(LocalDateTime dt) {
        if (dt == null) return null;
        return dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    private LocalDateTime parseFecha(String fecha, boolean finDia) {
        if (fecha == null || fecha.isBlank()) return null;
        LocalDate d = LocalDate.parse(fecha, DATE_FMT);
        return finDia ? d.atTime(23, 59, 59) : d.atStartOfDay();
    }

    private String escapeJson(String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r") + "\"";
    }
}

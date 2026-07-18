package com.example.demo.controller;

import com.example.demo.dto.HistorialChatFinalizarRequest;
import com.example.demo.dto.HistorialChatRequest;
import com.example.demo.dto.HistorialChatResponse;
import com.example.demo.service.HistorialChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/historial-chat")
@Tag(name = "Historial Chat", description = "Registro del historial de conversaciones del chatbot WhatsApp n8n")
public class HistorialChatController {

    private final HistorialChatService service;

    public HistorialChatController(HistorialChatService service) {
        this.service = service;
    }

    @PostMapping("/iniciar")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Iniciar sesión de chat",
               description = "Crea un nuevo registro de historial al inicio de un flujo n8n. Registra la fecha/hora de inicio en timezone Lima UTC-5.")
    public HistorialChatResponse iniciar(@RequestBody HistorialChatRequest request) {
        return service.iniciar(request);
    }

    @PutMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar sesión de chat",
               description = "Cierra el registro de historial y registra automáticamente la fecha/hora de fin en timezone Lima UTC-5.")
    public HistorialChatResponse finalizar(
            @Parameter(description = "ID del historial a finalizar")
            @PathVariable Long id,
            @RequestBody HistorialChatFinalizarRequest request) {
        return service.finalizar(id, request);
    }

    @GetMapping
    @Operation(summary = "Listar historial de chat",
               description = "Retorna el historial filtrado por llaveSesion, proceso y rango de fechas (formato DD/MM/AAAA)")
    public List<HistorialChatResponse> listar(
            @Parameter(description = "Clave de sesion (wa_id del cliente)")
            @RequestParam(required = false) String llaveSesion,
            @Parameter(description = "Tipo de proceso: CONSULTA, CONSULTA_RUC, CALCULAR, COTIZACION, PDF")
            @RequestParam(required = false) String proceso,
            @Parameter(description = "Fecha inicio del filtro (DD/MM/AAAA)")
            @RequestParam(required = false) String fechaInicio,
            @Parameter(description = "Fecha fin del filtro (DD/MM/AAAA)")
            @RequestParam(required = false) String fechaFin) {
        return service.listar(llaveSesion, proceso, fechaInicio, fechaFin);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener historial por ID")
    public HistorialChatResponse obtener(
            @Parameter(description = "ID del historial")
            @PathVariable Long id) {
        return service.obtener(id);
    }
}

package com.example.demo.controller;

import com.example.demo.dto.AuditoriaFilterRequest;
import com.example.demo.dto.AuditoriaResponse;
import com.example.demo.service.AuditoriaService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auditoria")
public class AuditoriaController {
    private final AuditoriaService service;

    public AuditoriaController(AuditoriaService service) {
        this.service = service;
    }

    @GetMapping
    public Page<AuditoriaResponse> buscar(
            @RequestParam(required = false) String nombreTabla,
            @RequestParam(required = false) String idRegistro,
            @RequestParam(required = false) String accion,
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) String correoUsuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) String modulo,
            Pageable pageable
    ) {
        return service.buscarAuditoria(
                new AuditoriaFilterRequest(nombreTabla, idRegistro, accion, idUsuario, correoUsuario, fechaInicio, fechaFin, modulo),
                pageable
        );
    }

    @GetMapping("/{nombreTabla}/{idRegistro}")
    public List<AuditoriaResponse> historial(@PathVariable String nombreTabla, @PathVariable String idRegistro) {
        return service.obtenerHistorialRegistro(nombreTabla, idRegistro);
    }
}

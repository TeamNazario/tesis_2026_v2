package com.example.demo.controller;

import com.example.demo.dto.CotizacionCreateRequest;
import com.example.demo.dto.CotizacionEstadoUpdateRequest;
import com.example.demo.dto.CotizacionV1Response;
import com.example.demo.service.CotizacionV1Service;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cotizaciones")
public class CotizacionV1Controller {
    private final CotizacionV1Service service;

    public CotizacionV1Controller(CotizacionV1Service service) {
        this.service = service;
    }

    @GetMapping
    public List<CotizacionV1Response> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public CotizacionV1Response findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CotizacionV1Response create(@Valid @RequestBody CotizacionCreateRequest request) { return service.create(request); }

    @PatchMapping("/{id}/estado")
    public CotizacionV1Response patchEstado(
            @PathVariable Integer id,
            @Valid @RequestBody CotizacionEstadoUpdateRequest request
    ) {
        return service.patchEstado(id, request.idEstadoCotizacion());
    }
}

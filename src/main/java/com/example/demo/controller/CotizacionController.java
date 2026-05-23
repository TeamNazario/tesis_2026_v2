package com.example.demo.controller;

import com.example.demo.dto.CotizacionRequest;
import com.example.demo.dto.CotizacionResponse;
import com.example.demo.dto.CotizacionDetalleResponse;
import com.example.demo.service.CotizacionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cotizaciones")
public class CotizacionController {
    private final CotizacionService service;

    public CotizacionController(CotizacionService service) {
        this.service = service;
    }

    @GetMapping
    public List<CotizacionResponse> findAll() {
        return service.findAllDto();
    }

    @GetMapping("/{id}")
    public CotizacionResponse findById(@PathVariable Integer id) {
        return service.findDtoById(id);
    }

    @GetMapping("/uuid/{uuidPublico}")
    public CotizacionResponse findByUuid(@PathVariable String uuidPublico) {
        return service.findDtoByUuid(uuidPublico);
    }

    @GetMapping("/{id}/detalles")
    public List<CotizacionDetalleResponse> findDetalles(@PathVariable Integer id) {
        return service.findDetallesByCotizacion(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CotizacionResponse create(@Valid @RequestBody CotizacionRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public CotizacionResponse update(@PathVariable Integer id, @Valid @RequestBody CotizacionRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteById(id);
    }
}

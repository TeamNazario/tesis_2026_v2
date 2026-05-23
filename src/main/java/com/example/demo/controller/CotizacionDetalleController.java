package com.example.demo.controller;

import com.example.demo.dto.CotizacionDetalleResponse;
import com.example.demo.dto.CotizacionDetalleUpsertRequest;
import com.example.demo.service.CotizacionDetalleService;
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
@RequestMapping("/api/cotizacion-detalles")
public class CotizacionDetalleController {
    private final CotizacionDetalleService service;

    public CotizacionDetalleController(CotizacionDetalleService service) {
        this.service = service;
    }

    @GetMapping
    public List<CotizacionDetalleResponse> findAll() {
        return service.findAllDto();
    }

    @GetMapping("/{id}")
    public CotizacionDetalleResponse findById(@PathVariable Integer id) {
        return service.findDtoById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CotizacionDetalleResponse create(@Valid @RequestBody CotizacionDetalleUpsertRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public CotizacionDetalleResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody CotizacionDetalleUpsertRequest request
    ) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteById(id);
    }
}

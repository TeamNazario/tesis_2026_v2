package com.example.demo.controller;

import com.example.demo.dto.ZonaDespachoRequest;
import com.example.demo.dto.ZonaDespachoResponse;
import com.example.demo.service.ZonaDespachoService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/zonas-despacho")
public class ZonaDespachoController {
    private final ZonaDespachoService service;

    public ZonaDespachoController(ZonaDespachoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ZonaDespachoResponse> findAll() {
        return service.findAllDto();
    }

    @GetMapping("/{id}")
    public ZonaDespachoResponse findById(@PathVariable Integer id) {
        return service.findDtoById(id);
    }

    @GetMapping("/buscar")
    public List<ZonaDespachoResponse> findByUbicacion(
            @RequestParam String departamento,
            @RequestParam(required = false) String provincia
    ) {
        return service.findByUbicacion(departamento, provincia);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ZonaDespachoResponse create(@Valid @RequestBody ZonaDespachoRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ZonaDespachoResponse update(@PathVariable Integer id, @Valid @RequestBody ZonaDespachoRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteById(id);
    }
}

package com.example.demo.controller;

import com.example.demo.dto.EstadoRequest;
import com.example.demo.dto.EstadoResponse;
import com.example.demo.service.EstadoService;
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
@RequestMapping("/api/estados")
public class EstadoController {
    private final EstadoService service;

    public EstadoController(EstadoService service) {
        this.service = service;
    }

    @GetMapping
    public List<EstadoResponse> findAll() {
        return service.findAllDto();
    }

    @GetMapping("/{id}")
    public EstadoResponse findById(@PathVariable Integer id) {
        return service.findDtoById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstadoResponse create(@Valid @RequestBody EstadoRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public EstadoResponse update(@PathVariable Integer id, @Valid @RequestBody EstadoRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteById(id);
    }
}

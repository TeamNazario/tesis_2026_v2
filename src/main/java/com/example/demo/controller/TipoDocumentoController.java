package com.example.demo.controller;

import com.example.demo.dto.TipoDocumentoRequest;
import com.example.demo.dto.TipoDocumentoResponse;
import com.example.demo.service.TipoDocumentoService;
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
@RequestMapping("/api/tipos-documento")
public class TipoDocumentoController {
    private final TipoDocumentoService service;

    public TipoDocumentoController(TipoDocumentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<TipoDocumentoResponse> findAll() {
        return service.findAllDto();
    }

    @GetMapping("/{id}")
    public TipoDocumentoResponse findById(@PathVariable Integer id) {
        return service.findDtoById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TipoDocumentoResponse create(@Valid @RequestBody TipoDocumentoRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public TipoDocumentoResponse update(@PathVariable Integer id, @Valid @RequestBody TipoDocumentoRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteById(id);
    }
}

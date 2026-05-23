package com.example.demo.controller;

import com.example.demo.dto.PerfilRequest;
import com.example.demo.dto.PerfilResponse;
import com.example.demo.service.PerfilService;
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
@RequestMapping("/api/perfiles")
public class PerfilController {
    private final PerfilService service;

    public PerfilController(PerfilService service) {
        this.service = service;
    }

    @GetMapping
    public List<PerfilResponse> findAll() {
        return service.findAllDto();
    }

    @GetMapping("/{id}")
    public PerfilResponse findById(@PathVariable Integer id) {
        return service.findDtoById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PerfilResponse create(@Valid @RequestBody PerfilRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public PerfilResponse update(@PathVariable Integer id, @Valid @RequestBody PerfilRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteById(id);
    }
}

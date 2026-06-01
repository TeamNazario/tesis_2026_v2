package com.example.demo.controller;

import com.example.demo.dto.ProductoRequest;
import com.example.demo.dto.ProductoResponse;
import com.example.demo.service.ProductoService;
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
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductoResponse> findAll() {
        return service.findAllDto();
    }

    @GetMapping("/{id}")
    public ProductoResponse findById(@PathVariable Integer id) {
        return service.findDtoById(id);
    }

    @GetMapping("/activos")
    public List<ProductoResponse> findActivos() {
        return service.findActivos();
    }

    @GetMapping("/estado/{idEstadoProducto}")
    public List<ProductoResponse> findByEstado(@PathVariable Integer idEstadoProducto) {
        return service.findByEstado(idEstadoProducto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponse create(@Valid @RequestBody ProductoRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ProductoResponse update(@PathVariable Integer id, @Valid @RequestBody ProductoRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteById(id);
    }
}

package com.example.demo.controller;

import com.example.demo.dto.ProductoCreateRequest;
import com.example.demo.dto.ProductoEstadoUpdateRequest;
import com.example.demo.dto.ProductoUpdateRequest;
import com.example.demo.dto.ProductoV1Response;
import com.example.demo.security.AuthenticatedUser;
import com.example.demo.service.ProductoV1Service;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoV1Controller {
    private final ProductoV1Service service;

    public ProductoV1Controller(ProductoV1Service service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductoV1Response> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public ProductoV1Response findById(@PathVariable Integer id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoV1Response create(
            @Valid @RequestBody ProductoCreateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return service.create(request, resolveActor(user));
    }

    @PutMapping("/{id}")
    public ProductoV1Response update(
            @PathVariable Integer id,
            @RequestBody ProductoUpdateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return service.update(id, request, resolveActor(user));
    }

    @PatchMapping("/{id}/estado")
    public ProductoV1Response patchEstado(
            @PathVariable Integer id,
            @Valid @RequestBody ProductoEstadoUpdateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return service.patchEstado(id, request.idEstadoProducto(), resolveActor(user));
    }

    private String resolveActor(AuthenticatedUser user) {
        if (user != null && user.getUsuario() != null && user.getUsuario().correo != null) {
            return user.getUsuario().correo;
        }
        return "system";
    }
}

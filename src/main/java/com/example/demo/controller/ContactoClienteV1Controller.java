package com.example.demo.controller;

import com.example.demo.dto.ContactoClienteCreateRequest;
import com.example.demo.dto.ContactoClienteEstadoPatchRequest;
import com.example.demo.dto.ContactoClienteUpdateRequest;
import com.example.demo.dto.ContactoClienteV1Response;
import com.example.demo.security.AuthenticatedUser;
import com.example.demo.service.ContactoClienteV1Service;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clientes/{clienteId}/contactos")
public class ContactoClienteV1Controller {
    private final ContactoClienteV1Service service;

    public ContactoClienteV1Controller(ContactoClienteV1Service service) {
        this.service = service;
    }

    @GetMapping
    public List<ContactoClienteV1Response> list(@PathVariable Integer clienteId) {
        return service.listByCliente(clienteId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactoClienteV1Response create(
            @PathVariable Integer clienteId,
            @Valid @RequestBody ContactoClienteCreateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return service.create(clienteId, request, resolveActor(user));
    }

    @PutMapping("/{contactoId}")
    public ContactoClienteV1Response update(
            @PathVariable Integer clienteId,
            @PathVariable Integer contactoId,
            @RequestBody ContactoClienteUpdateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return service.update(clienteId, contactoId, request, resolveActor(user));
    }

    @PatchMapping("/{contactoId}/estado")
    public ContactoClienteV1Response patchEstado(
            @PathVariable Integer clienteId,
            @PathVariable Integer contactoId,
            @Valid @RequestBody ContactoClienteEstadoPatchRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return service.patchEstado(clienteId, contactoId, request.idEstadoClienteContacto(), resolveActor(user));
    }

    private String resolveActor(AuthenticatedUser user) {
        if (user != null && user.getUsuario() != null && user.getUsuario().correo != null) {
            return user.getUsuario().correo;
        }
        return "system";
    }
}

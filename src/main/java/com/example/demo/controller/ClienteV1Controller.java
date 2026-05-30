package com.example.demo.controller;

import com.example.demo.dto.ClienteCreateRequest;
import com.example.demo.dto.ClienteEstadoPatchRequest;
import com.example.demo.dto.ClienteUpdateRequest;
import com.example.demo.dto.ClienteV1Response;
import com.example.demo.security.AuthenticatedUser;
import com.example.demo.service.ClienteV1Service;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteV1Controller {
    private final ClienteV1Service service;

    public ClienteV1Controller(ClienteV1Service service) {
        this.service = service;
    }

    @GetMapping
    public List<ClienteV1Response> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public ClienteV1Response findById(@PathVariable Integer id) { return service.findById(id); }

    @GetMapping("/buscar")
    public List<ClienteV1Response> buscar(@RequestParam(required = false) String ruc, @RequestParam(required = false) String razonSocial) {
        return service.buscar(ruc, razonSocial);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteV1Response create(
            @Valid @RequestBody ClienteCreateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return service.create(request, resolveActor(user));
    }

    @PutMapping("/{id}")
    public ClienteV1Response update(
            @PathVariable Integer id,
            @RequestBody ClienteUpdateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return service.update(id, request, resolveActor(user));
    }

    @PatchMapping("/{id}")
    public ClienteV1Response patchDatos(
            @PathVariable Integer id,
            @RequestBody ClienteUpdateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return service.update(id, request, resolveActor(user));
    }

    @PatchMapping("/{id}/estado")
    public ClienteV1Response patchEstado(
            @PathVariable Integer id,
            @Valid @RequestBody ClienteEstadoPatchRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return service.patchEstado(id, request.idEstadoClienteContacto(), resolveActor(user));
    }

    private String resolveActor(AuthenticatedUser user) {
        if (user != null && user.getUsuario() != null && user.getUsuario().correo != null) {
            return user.getUsuario().correo;
        }
        return "system";
    }
}

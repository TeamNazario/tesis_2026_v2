package com.example.demo.controller;

import com.example.demo.dto.TipoClienteComboResponse;
import com.example.demo.dto.TipoClienteCreateRequest;
import com.example.demo.dto.TipoClienteEstadoUpdateRequest;
import com.example.demo.dto.TipoClienteResponse;
import com.example.demo.dto.TipoClienteUpdateRequest;
import com.example.demo.security.AuthenticatedUser;
import com.example.demo.service.TipoClienteService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tipos-cliente")
public class TipoClienteController {
    private final TipoClienteService service;

    public TipoClienteController(TipoClienteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<TipoClienteResponse>> listar(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer idEstadoClienteContacto,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(service.listar(search, idEstadoClienteContacto, pageable));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<TipoClienteComboResponse>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoClienteResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<TipoClienteResponse> crear(
            @Valid @RequestBody TipoClienteCreateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        TipoClienteResponse response = service.crear(request, resolveActor(user));
        return ResponseEntity.created(URI.create("/api/v1/tipos-cliente/" + response.idTipoCliente())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoClienteResponse> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody TipoClienteUpdateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return ResponseEntity.ok(service.actualizar(id, request, resolveActor(user)));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TipoClienteResponse> cambiarEstado(
            @PathVariable Integer id,
            @Valid @RequestBody TipoClienteEstadoUpdateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return ResponseEntity.ok(service.cambiarEstado(id, request, resolveActor(user)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer id,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        service.eliminar(id, resolveActor(user));
        return ResponseEntity.noContent().build();
    }

    private String resolveActor(AuthenticatedUser user) {
        if (user != null && user.getUsuario() != null && user.getUsuario().correo != null) {
            return user.getUsuario().correo;
        }
        return "system";
    }
}

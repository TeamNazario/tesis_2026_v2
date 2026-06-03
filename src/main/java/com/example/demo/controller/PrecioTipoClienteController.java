package com.example.demo.controller;

import com.example.demo.dto.PrecioTipoClienteCreateRequest;
import com.example.demo.dto.PrecioTipoClienteResponse;
import com.example.demo.dto.PrecioTipoClienteUpdateRequest;
import com.example.demo.security.AuthenticatedUser;
import com.example.demo.service.PrecioTipoClienteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/precios-tipo-cliente")
public class PrecioTipoClienteController {
    private final PrecioTipoClienteService service;

    public PrecioTipoClienteController(PrecioTipoClienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<PrecioTipoClienteResponse> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public PrecioTipoClienteResponse findById(@PathVariable Integer id) { return service.findById(id); }

    @GetMapping("/producto/{idProducto}")
    public List<PrecioTipoClienteResponse> findByProductoId(@PathVariable Integer idProducto) {
        return service.findByProductoId(idProducto);
    }

    @GetMapping("/buscar")
    public List<PrecioTipoClienteResponse> buscar(
            @RequestParam(required = false) Integer idProducto,
            @RequestParam(required = false) Integer idTipoCliente,
            @RequestParam(required = false) Integer idEstadoProducto
    ) {
        return service.buscar(idProducto, idTipoCliente, idEstadoProducto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrecioTipoClienteResponse create(
            @Valid @RequestBody PrecioTipoClienteCreateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return service.create(request, resolveActor(user));
    }

    @PutMapping("/{id}")
    public PrecioTipoClienteResponse update(
            @PathVariable Integer id,
            @RequestBody PrecioTipoClienteUpdateRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        return service.update(id, request, resolveActor(user));
    }

    private String resolveActor(AuthenticatedUser user) {
        if (user != null && user.getUsuario() != null && user.getUsuario().correo != null) {
            return user.getUsuario().correo;
        }
        return "system";
    }
}

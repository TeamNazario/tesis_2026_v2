package com.example.demo.controller;

import com.example.demo.dto.CatalogoItemResponse;
import com.example.demo.service.CatalogoV1Service;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalogos")
public class CatalogoV1Controller {
    private final CatalogoV1Service service;

    public CatalogoV1Controller(CatalogoV1Service service) {
        this.service = service;
    }

    @GetMapping("/tipos-documento")
    public List<CatalogoItemResponse> tiposDocumento() { return service.tiposDocumento(); }

    @GetMapping("/estados-usuario")
    public List<CatalogoItemResponse> estadosUsuario() { return service.estadosUsuario(); }

    @GetMapping("/estados-cliente-contacto")
    public List<CatalogoItemResponse> estadosClienteContacto() { return service.estadosClienteContacto(); }

    @GetMapping("/tipos-cliente")
    public List<CatalogoItemResponse> tiposCliente() { return service.tiposCliente(); }

    @GetMapping("/estados-producto")
    public List<CatalogoItemResponse> estadosProducto() { return service.estadosProducto(); }

    @GetMapping("/estados-cotizacion")
    public List<CatalogoItemResponse> estadosCotizacion() { return service.estadosCotizacion(); }

    @GetMapping("/perfiles")
    public List<CatalogoItemResponse> perfiles() { return service.perfiles(); }
}

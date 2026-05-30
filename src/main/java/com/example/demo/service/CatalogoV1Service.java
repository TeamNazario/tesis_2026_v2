package com.example.demo.service;

import com.example.demo.dto.CatalogoItemResponse;
import com.example.demo.repository.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CatalogoV1Service {
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final EstadoUsuarioRepository estadoUsuarioRepository;
    private final EstadoClienteContactoRepository estadoClienteContactoRepository;
    private final TipoClienteRepository tipoClienteRepository;
    private final EstadoProductoRepository estadoProductoRepository;
    private final EstadoCotizacionRepository estadoCotizacionRepository;
    private final PerfilRepository perfilRepository;

    public CatalogoV1Service(
            TipoDocumentoRepository tipoDocumentoRepository,
            EstadoUsuarioRepository estadoUsuarioRepository,
            EstadoClienteContactoRepository estadoClienteContactoRepository,
            TipoClienteRepository tipoClienteRepository,
            EstadoProductoRepository estadoProductoRepository,
            EstadoCotizacionRepository estadoCotizacionRepository,
            PerfilRepository perfilRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.estadoUsuarioRepository = estadoUsuarioRepository;
        this.estadoClienteContactoRepository = estadoClienteContactoRepository;
        this.tipoClienteRepository = tipoClienteRepository;
        this.estadoProductoRepository = estadoProductoRepository;
        this.estadoCotizacionRepository = estadoCotizacionRepository;
        this.perfilRepository = perfilRepository;
    }

    @Transactional(readOnly = true)
    public List<CatalogoItemResponse> tiposDocumento() {
        return tipoDocumentoRepository.findAll().stream().map(t -> new CatalogoItemResponse(t.idTipoDoc, t.descTipoDoc)).toList();
    }

    @Transactional(readOnly = true)
    public List<CatalogoItemResponse> estadosUsuario() {
        return estadoUsuarioRepository.findAll().stream().map(e -> new CatalogoItemResponse(e.idEstadoUsuario, e.desEstado)).toList();
    }

    @Transactional(readOnly = true)
    public List<CatalogoItemResponse> estadosClienteContacto() {
        return estadoClienteContactoRepository.findAll().stream().map(e -> new CatalogoItemResponse(e.idEstadoClienteContacto, e.desEstadoClienteContacto)).toList();
    }

    @Transactional(readOnly = true)
    public List<CatalogoItemResponse> tiposCliente() {
        return tipoClienteRepository.findAll().stream().map(t -> new CatalogoItemResponse(t.idTipoCliente, t.descTipoCliente)).toList();
    }

    @Transactional(readOnly = true)
    public List<CatalogoItemResponse> estadosProducto() {
        return estadoProductoRepository.findAll().stream().map(e -> new CatalogoItemResponse(e.idEstadoProducto, e.descEstadoProducto)).toList();
    }

    @Transactional(readOnly = true)
    public List<CatalogoItemResponse> estadosCotizacion() {
        return estadoCotizacionRepository.findAll().stream().map(e -> new CatalogoItemResponse(e.idEstadoCotizacion, e.descEstadoCotizacion)).toList();
    }

    @Transactional(readOnly = true)
    public List<CatalogoItemResponse> perfiles() {
        return perfilRepository.findAll().stream().map(p -> new CatalogoItemResponse(p.idPerfil, p.nombrePerfil)).toList();
    }
}

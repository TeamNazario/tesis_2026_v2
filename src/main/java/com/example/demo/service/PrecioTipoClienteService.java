package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.PrecioTipoCliente;
import com.example.demo.repository.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrecioTipoClienteService {
    private final PrecioTipoClienteRepository repository;
    private final TipoClienteRepository tipoClienteRepository;
    private final EstadoProductoRepository estadoProductoRepository;
    private final ProductoRepository productoRepository;
    private final AuditoriaService auditoriaService;
    private final AccessControlService accessControlService;

    public PrecioTipoClienteService(PrecioTipoClienteRepository repository, TipoClienteRepository tipoClienteRepository, EstadoProductoRepository estadoProductoRepository, ProductoRepository productoRepository, AuditoriaService auditoriaService, AccessControlService accessControlService) {
        this.repository = repository;
        this.tipoClienteRepository = tipoClienteRepository;
        this.estadoProductoRepository = estadoProductoRepository;
        this.productoRepository = productoRepository;
        this.auditoriaService = auditoriaService;
        this.accessControlService = accessControlService;
    }

    @Transactional(readOnly = true)
    public List<PrecioTipoClienteResponse> findAll() { return repository.findAll().stream().map(this::map).toList(); }

    @Transactional(readOnly = true)
    public PrecioTipoClienteResponse findById(Integer id) {
        return map(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PrecioTipoCliente", id)));
    }

    @Transactional(readOnly = true)
    public List<PrecioTipoClienteResponse> findByProductoId(Integer idProducto) {
        return repository.findByIdProducto(idProducto).stream().map(this::map).toList();
    }

    @Transactional(readOnly = true)
    public List<PrecioTipoClienteResponse> buscar(Integer idProducto, Integer idTipoCliente, Integer idEstadoProducto) {
        return repository.buscar(idProducto, idTipoCliente, idEstadoProducto).stream()
                .map(this::map)
                .toList();
    }

    @Transactional
    public PrecioTipoClienteResponse create(PrecioTipoClienteCreateRequest request, String actor) {
        accessControlService.validarPuedeGestionarPrecios();
        PrecioTipoCliente p = new PrecioTipoCliente();
        p.precioUnitario = request.precioUnitario();
        p.moneda = request.moneda();
        p.tipoCliente = tipoClienteRepository.findById(request.idTipoCliente()).orElseThrow(() -> new ResourceNotFoundException("TipoCliente", request.idTipoCliente()));
        p.estadoProducto = estadoProductoRepository.findById(request.idEstadoProducto()).orElseThrow(() -> new ResourceNotFoundException("EstadoProducto", request.idEstadoProducto()));
        p.producto = productoRepository.findById(request.idProducto()).orElseThrow(() -> new ResourceNotFoundException("Producto", request.idProducto()));
        p.usuRegistro = actor;
        p.fecRegistro = LocalDateTime.now();
        PrecioTipoClienteResponse response = map(repository.save(p));
        auditoriaService.registrarCreacion("PRECIO_TIPO_CLIENTE", String.valueOf(response.idPrecio()), response, "PRECIOS_TIPO_CLIENTE", "Creacion de precio por tipo de cliente");
        return response;
    }

    @Transactional
    public PrecioTipoClienteResponse update(Integer id, PrecioTipoClienteUpdateRequest request, String actor) {
        accessControlService.validarPuedeGestionarPrecios();
        PrecioTipoCliente p = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PrecioTipoCliente", id));
        PrecioTipoClienteResponse anterior = map(p);
        if (request.precioUnitario() != null) p.precioUnitario = request.precioUnitario();
        if (request.moneda() != null) p.moneda = request.moneda();
        if (request.idTipoCliente() != null) p.tipoCliente = tipoClienteRepository.findById(request.idTipoCliente()).orElseThrow(() -> new ResourceNotFoundException("TipoCliente", request.idTipoCliente()));
        if (request.idEstadoProducto() != null) p.estadoProducto = estadoProductoRepository.findById(request.idEstadoProducto()).orElseThrow(() -> new ResourceNotFoundException("EstadoProducto", request.idEstadoProducto()));
        if (request.idProducto() != null) p.producto = productoRepository.findById(request.idProducto()).orElseThrow(() -> new ResourceNotFoundException("Producto", request.idProducto()));
        p.usuActualiza = actor;
        p.fecActualiza = LocalDateTime.now();
        PrecioTipoClienteResponse nuevo = map(repository.save(p));
        auditoriaService.registrarActualizacion("PRECIO_TIPO_CLIENTE", String.valueOf(id), anterior, nuevo, "PRECIOS_TIPO_CLIENTE", "Actualizacion de precio por tipo de cliente");
        return nuevo;
    }

    private PrecioTipoClienteResponse map(PrecioTipoCliente p) {
        Integer idTipoCliente = p.tipoCliente != null ? p.tipoCliente.idTipoCliente : p.idTipoCliente;
        String tipoCliente = p.tipoCliente != null ? p.tipoCliente.descTipoCliente : null;
        Integer idEstadoProducto = p.estadoProducto != null ? p.estadoProducto.idEstadoProducto : p.idEstadoProducto;
        String estadoProducto = p.estadoProducto != null ? p.estadoProducto.descEstadoProducto : null;
        Integer idProducto = p.producto != null ? p.producto.idProducto : p.idProducto;
        String producto = p.producto != null ? p.producto.nombreProducto : null;
        return new PrecioTipoClienteResponse(
                p.idPrecio,
                p.precioUnitario,
                p.moneda,
                idTipoCliente,
                tipoCliente,
                idEstadoProducto,
                estadoProducto,
                idProducto,
                producto
        );
    }
}

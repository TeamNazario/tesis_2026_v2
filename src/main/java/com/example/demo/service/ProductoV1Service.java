package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Estado;
import com.example.demo.model.Producto;
import com.example.demo.repository.EstadoRepository;
import com.example.demo.repository.EstadoProductoRepository;
import com.example.demo.repository.ProductoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoV1Service {
    private final ProductoRepository repository;
    private final EstadoProductoRepository estadoProductoRepository;
    private final EstadoRepository estadoRepository;

    public ProductoV1Service(
            ProductoRepository repository,
            EstadoProductoRepository estadoProductoRepository,
            EstadoRepository estadoRepository
    ) {
        this.repository = repository;
        this.estadoProductoRepository = estadoProductoRepository;
        this.estadoRepository = estadoRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductoV1Response> findAll() { return repository.findAll().stream().map(this::map).toList(); }

    @Transactional(readOnly = true)
    public ProductoV1Response findById(Integer id) { return map(findEntity(id)); }

    @Transactional
    public ProductoV1Response create(ProductoCreateRequest request, String actor) {
        Producto p = new Producto();
        p.nombreProducto = request.nombreProducto();
        p.unidadMedida = request.unidadMedida();
        p.peso = request.peso();
        p.volumen = request.volumen();
        p.precioBaseUnitario = BigDecimal.ZERO;
        p.concentracionUreaAus32 = BigDecimal.ZERO;
        p.stockFisico = request.stockFisico();
        p.stockReservado = request.stockReservado();
        p.stockDisponible = request.stockDisponible() == null ? Math.max(0, request.stockFisico() - request.stockReservado()) : request.stockDisponible();
        p.stockMinimoSeguridad = request.stockMinimo();
        p.cantMinVenta = request.cantMinVenta();
        p.estado = resolveEstadoInterno();
        p.estadoProducto = estadoProductoRepository.findById(request.idEstadoProducto()).orElseThrow(() -> new ResourceNotFoundException("EstadoProducto", request.idEstadoProducto()));
        p.usuRegistro = actor;
        p.fecRegistro = LocalDateTime.now();
        return map(repository.save(p));
    }

    @Transactional
    public ProductoV1Response update(Integer id, ProductoUpdateRequest request, String actor) {
        Producto p = findEntity(id);
        if (request.nombreProducto() != null) p.nombreProducto = request.nombreProducto();
        if (request.unidadMedida() != null) p.unidadMedida = request.unidadMedida();
        if (request.peso() != null) p.peso = request.peso();
        if (request.volumen() != null) p.volumen = request.volumen();
        if (request.stockFisico() != null) p.stockFisico = request.stockFisico();
        if (request.stockReservado() != null) p.stockReservado = request.stockReservado();
        if (request.stockDisponible() != null) p.stockDisponible = request.stockDisponible();
        if (request.stockMinimo() != null) p.stockMinimoSeguridad = request.stockMinimo();
        if (request.cantMinVenta() != null) p.cantMinVenta = request.cantMinVenta();
        if (request.idEstadoProducto() != null) p.estadoProducto = estadoProductoRepository.findById(request.idEstadoProducto()).orElseThrow(() -> new ResourceNotFoundException("EstadoProducto", request.idEstadoProducto()));
        if (p.precioBaseUnitario == null) p.precioBaseUnitario = BigDecimal.ZERO;
        if (p.concentracionUreaAus32 == null) p.concentracionUreaAus32 = BigDecimal.ZERO;
        if (p.estado == null) p.estado = resolveEstadoInterno();
        p.usuActualiza = actor;
        p.fecActualiza = LocalDateTime.now();
        return map(repository.save(p));
    }

    @Transactional
    public ProductoV1Response patchEstado(Integer id, Integer estadoId, String actor) {
        Producto p = findEntity(id);
        p.estadoProducto = estadoProductoRepository.findById(estadoId).orElseThrow(() -> new ResourceNotFoundException("EstadoProducto", estadoId));
        p.usuActualiza = actor;
        p.fecActualiza = LocalDateTime.now();
        return map(repository.save(p));
    }

    private Producto findEntity(Integer id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto", id)); }

    private Estado resolveEstadoInterno() {
        return estadoRepository.findFirstByDescEstadoIgnoreCase("Habilitado")
                .orElseGet(() -> estadoRepository.findById(1).orElseThrow(() -> new IllegalStateException("No se encontro un estado interno valido para Producto.")));
    }

    private ProductoV1Response map(Producto p) {
        return new ProductoV1Response(p.idProducto, p.nombreProducto, p.unidadMedida, p.peso, p.volumen, p.stockFisico, p.stockReservado, p.stockDisponible, p.stockMinimoSeguridad, p.cantMinVenta,
                p.estadoProducto == null ? null : p.estadoProducto.idEstadoProducto,
                p.estadoProducto == null ? null : p.estadoProducto.descEstadoProducto);
    }
}

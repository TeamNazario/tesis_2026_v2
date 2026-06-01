package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Producto;
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

    public ProductoV1Service(
            ProductoRepository repository,
            EstadoProductoRepository estadoProductoRepository
    ) {
        this.repository = repository;
        this.estadoProductoRepository = estadoProductoRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductoV1Response> findAll() { return repository.findAll().stream().map(this::map).toList(); }

    @Transactional(readOnly = true)
    public ProductoV1Response findById(Integer id) { return map(findEntity(id)); }

    @Transactional
    public ProductoV1Response create(ProductoCreateRequest request, String actor) {
        validateStocks(request.stockFisico(), request.stockReservado(), request.stockDisponible());
        Producto p = new Producto();
        p.nombreProducto = request.nombreProducto();
        p.unidadMedida = request.unidadMedida();
        p.peso = request.peso();
        p.volumen = request.volumen();
        p.stockFisico = request.stockFisico();
        p.stockReservado = request.stockReservado();
        p.stockDisponible = request.stockFisico() - request.stockReservado();
        p.stockMinimoSeguridad = request.stockMinimo();
        p.cantMinVenta = request.cantMinVenta();
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
        validateStocks(p.stockFisico, p.stockReservado, request.stockDisponible());
        p.stockDisponible = p.stockFisico - p.stockReservado;
        if (request.stockMinimo() != null) p.stockMinimoSeguridad = request.stockMinimo();
        if (request.cantMinVenta() != null) p.cantMinVenta = request.cantMinVenta();
        if (request.idEstadoProducto() != null) p.estadoProducto = estadoProductoRepository.findById(request.idEstadoProducto()).orElseThrow(() -> new ResourceNotFoundException("EstadoProducto", request.idEstadoProducto()));
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

    private ProductoV1Response map(Producto p) {
        Integer idEstado = p.estadoProducto == null ? p.idEstadoProducto : p.estadoProducto.idEstadoProducto;
        String descEstado = p.estadoProducto == null ? null : p.estadoProducto.descEstadoProducto;
        Integer stockDisponible = p.stockDisponible != null ? p.stockDisponible : computeStockDisponible(p.stockFisico, p.stockReservado);
        return new ProductoV1Response(
                p.idProducto,
                p.nombreProducto,
                p.unidadMedida,
                p.peso,
                p.volumen,
                p.stockFisico,
                p.stockReservado,
                stockDisponible,
                p.stockMinimoSeguridad,
                p.cantMinVenta,
                idEstado,
                descEstado
        );
    }

    private void validateStocks(Integer stockFisico, Integer stockReservado, Integer stockDisponibleRequest) {
        if (stockFisico == null || stockReservado == null) {
            throw new IllegalArgumentException("Los campos stockFisico y stockReservado son obligatorios.");
        }
        if (stockReservado > stockFisico) {
            throw new IllegalArgumentException("El stock reservado no puede ser mayor al stock fisico.");
        }
        int disponibleCalculado = stockFisico - stockReservado;
        if (disponibleCalculado < 0) {
            throw new IllegalArgumentException("El stock disponible no puede ser negativo.");
        }
        if (stockDisponibleRequest != null && !stockDisponibleRequest.equals(disponibleCalculado)) {
            throw new IllegalArgumentException("El stock disponible debe ser igual a stock fisico menos stock reservado.");
        }
    }

    private Integer computeStockDisponible(Integer stockFisico, Integer stockReservado) {
        if (stockFisico == null || stockReservado == null) {
            return null;
        }
        return stockFisico - stockReservado;
    }
}

package com.example.demo.service;

import com.example.demo.dto.ProductoRequest;
import com.example.demo.dto.ProductoResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ProductoMapper;
import com.example.demo.model.EstadoProducto;
import com.example.demo.model.Producto;
import com.example.demo.repository.EstadoProductoRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.util.ValidationErrors;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService extends CrudService<Producto, Integer> {
    private final ProductoRepository productoRepository;
    private final EstadoProductoRepository estadoProductoRepository;
    private final ProductoMapper productoMapper;

    public ProductoService(
            ProductoRepository repository,
            EstadoProductoRepository estadoProductoRepository,
            ProductoMapper productoMapper
    ) {
        super(repository, "Producto");
        this.productoRepository = repository;
        this.estadoProductoRepository = estadoProductoRepository;
        this.productoMapper = productoMapper;
    }

    @Override
    protected Integer parseId(String id) {
        return parseIntegerId(id);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> findAllDto() {
        return productoRepository.findAll().stream()
                .map(productoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse findDtoById(Integer id) {
        return productoMapper.toResponse(findProducto(id));
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> findByEstado(Integer idEstadoProducto) {
        if (!estadoProductoRepository.existsById(idEstadoProducto)) {
            throw new ResourceNotFoundException("EstadoProducto", idEstadoProducto);
        }
        return productoRepository.findByEstadoProductoIdEstadoProducto(idEstadoProducto).stream()
                .map(productoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> findActivos() {
        return productoRepository.findByEstadoProductoDescEstadoProductoIgnoreCase("Habilitado").stream()
                .map(productoMapper::toResponse)
                .toList();
    }

    @Transactional
    public ProductoResponse create(ProductoRequest request) {
        validateRequest(request);
        EstadoProducto estadoProducto = findEstadoProducto(request.idEstadoProducto());
        Producto producto = productoMapper.toEntity(request, estadoProducto);
        return productoMapper.toResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse update(Integer id, ProductoRequest request) {
        validateRequest(request);
        Producto producto = findProducto(id);
        EstadoProducto estadoProducto = findEstadoProducto(request.idEstadoProducto());
        productoMapper.updateEntity(producto, request, estadoProducto);
        return productoMapper.toResponse(productoRepository.save(producto));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto", id);
        }
        productoRepository.deleteById(id);
    }

    private Producto findProducto(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
    }

    private EstadoProducto findEstadoProducto(Integer id) {
        return estadoProductoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoProducto", id));
    }

    private void validateRequest(ProductoRequest request) {
        ValidationErrors errors = new ValidationErrors();

        if (request.stockFisico() != null
                && request.stockReservado() != null
                && request.stockReservado() > request.stockFisico()) {
            errors.add("stockReservado", "El stock reservado no puede superar el stock fisico.");
        }

        if (request.stockDisponible() != null
                && request.stockFisico() != null
                && request.stockReservado() != null
                && !request.stockDisponible().equals(request.stockFisico() - request.stockReservado())) {
            errors.add("stockDisponible", "El stock disponible debe ser igual a stock fisico menos stock reservado.");
        }

        errors.throwIfAny("El producto contiene datos inconsistentes.");
    }
}

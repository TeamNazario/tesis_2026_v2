package com.example.demo.service;

import com.example.demo.dto.CotizacionDetalleResponse;
import com.example.demo.dto.CotizacionDetalleUpsertRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.CotizacionMapper;
import com.example.demo.model.Cotizacion;
import com.example.demo.model.CotizacionDetalle;
import com.example.demo.model.Producto;
import com.example.demo.repository.CotizacionRepository;
import com.example.demo.repository.CotizacionDetalleRepository;
import com.example.demo.repository.ProductoRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CotizacionDetalleService extends CrudService<CotizacionDetalle, Integer> {
    private final CotizacionDetalleRepository detalleRepository;
    private final CotizacionRepository cotizacionRepository;
    private final ProductoRepository productoRepository;
    private final CotizacionMapper mapper;

    public CotizacionDetalleService(
            CotizacionDetalleRepository repository,
            CotizacionRepository cotizacionRepository,
            ProductoRepository productoRepository,
            CotizacionMapper mapper
    ) {
        super(repository, "CotizacionDetalle");
        this.detalleRepository = repository;
        this.cotizacionRepository = cotizacionRepository;
        this.productoRepository = productoRepository;
        this.mapper = mapper;
    }

    @Override
    protected Integer parseId(String id) {
        return parseIntegerId(id);
    }

    @Transactional(readOnly = true)
    public List<CotizacionDetalleResponse> findAllDto() {
        return detalleRepository.findAll().stream().map(mapper::toDetalleResponse).toList();
    }

    @Transactional(readOnly = true)
    public CotizacionDetalleResponse findDtoById(Integer id) {
        return mapper.toDetalleResponse(findDetalle(id));
    }

    @Transactional
    public CotizacionDetalleResponse create(CotizacionDetalleUpsertRequest request) {
        CotizacionDetalle detalle = new CotizacionDetalle();
        apply(detalle, request);
        return mapper.toDetalleResponse(detalleRepository.save(detalle));
    }

    @Transactional
    public CotizacionDetalleResponse update(Integer id, CotizacionDetalleUpsertRequest request) {
        CotizacionDetalle detalle = findDetalle(id);
        apply(detalle, request);
        return mapper.toDetalleResponse(detalleRepository.save(detalle));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!detalleRepository.existsById(id)) {
            throw new ResourceNotFoundException("CotizacionDetalle", id);
        }
        detalleRepository.deleteById(id);
    }

    private void apply(CotizacionDetalle detalle, CotizacionDetalleUpsertRequest request) {
        detalle.cotizacion = findCotizacion(request.idCotizacion());
        detalle.producto = findProducto(request.idProducto());
        detalle.cantidad = request.cantidad();
        detalle.precioUnitarioAplicado = request.precioUnitarioAplicado();
        detalle.subtotalLinea = request.precioUnitarioAplicado().multiply(BigDecimal.valueOf(request.cantidad()));
    }

    private CotizacionDetalle findDetalle(Integer id) {
        return detalleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CotizacionDetalle", id));
    }

    private Cotizacion findCotizacion(Integer id) {
        return cotizacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cotizacion", id));
    }

    private Producto findProducto(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
    }
}

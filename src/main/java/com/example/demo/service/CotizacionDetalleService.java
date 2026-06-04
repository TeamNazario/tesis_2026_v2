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
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CotizacionDetalleService extends CrudService<CotizacionDetalle, Integer> {
    private final CotizacionDetalleRepository detalleRepository;
    private final CotizacionRepository cotizacionRepository;
    private final ProductoRepository productoRepository;
    private final CotizacionMapper mapper;
    private final BigDecimal igvRate;

    public CotizacionDetalleService(
            CotizacionDetalleRepository repository,
            CotizacionRepository cotizacionRepository,
            ProductoRepository productoRepository,
            CotizacionMapper mapper,
            @Value("${app.business.igv-rate:0.18}") BigDecimal igvRate
    ) {
        super(repository, "CotizacionDetalle");
        this.detalleRepository = repository;
        this.cotizacionRepository = cotizacionRepository;
        this.productoRepository = productoRepository;
        this.mapper = mapper;
        this.igvRate = igvRate;
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
    public CotizacionDetalleResponse create(CotizacionDetalleUpsertRequest request, String actor) {
        CotizacionDetalle detalle = new CotizacionDetalle();
        apply(detalle, request);
        LocalDateTime now = LocalDateTime.now();
        detalle.usuRegistro = actor;
        detalle.fecRegistro = now;
        detalle.usuActualiza = null;
        detalle.fecActualiza = null;
        CotizacionDetalle saved = detalleRepository.save(detalle);
        recalculateCotizacionTotals(saved.cotizacion, actor);
        return mapper.toDetalleResponse(saved);
    }

    @Transactional
    public CotizacionDetalleResponse update(Integer id, CotizacionDetalleUpsertRequest request, String actor) {
        CotizacionDetalle detalle = findDetalle(id);
        apply(detalle, request);
        detalle.usuActualiza = actor;
        detalle.fecActualiza = LocalDateTime.now();
        CotizacionDetalle saved = detalleRepository.save(detalle);
        recalculateCotizacionTotals(saved.cotizacion, actor);
        return mapper.toDetalleResponse(saved);
    }

    @Transactional
    public void deleteById(Integer id, String actor) {
        CotizacionDetalle detalle = findDetalle(id);
        Cotizacion cotizacion = detalle.cotizacion;
        detalleRepository.delete(detalle);
        recalculateCotizacionTotals(cotizacion, actor);
    }

    private void apply(CotizacionDetalle detalle, CotizacionDetalleUpsertRequest request) {
        detalle.cotizacion = findCotizacion(request.idCotizacion());
        detalle.producto = findProducto(request.idProducto());
        detalle.cantidad = request.cantidad();
        if (request.precioUnitarioAplicado().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario aplicado debe ser mayor a 0.");
        }
        detalle.precioUni = request.precioUnitarioAplicado();
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

    private void recalculateCotizacionTotals(Cotizacion cotizacion, String actor) {
        if (cotizacion == null || cotizacion.idCotizacion == null) {
            return;
        }
        List<CotizacionDetalle> detalles = detalleRepository.findByCotizacionIdCotizacion(cotizacion.idCotizacion);
        BigDecimal subtotal = detalles.stream()
                .map(d -> d.precioUni == null || d.cantidad == null
                        ? BigDecimal.ZERO
                        : d.precioUni.multiply(BigDecimal.valueOf(d.cantidad)))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal igv = subtotal.multiply(igvRate).setScale(2, RoundingMode.HALF_UP);
        cotizacion.subtotal = subtotal;
        cotizacion.igv = igv;
        cotizacion.importeTotal = subtotal.add(igv).setScale(2, RoundingMode.HALF_UP);
        cotizacion.usuActualiza = actor;
        cotizacion.fecActualiza = LocalDateTime.now();
        cotizacionRepository.save(cotizacion);
    }
}

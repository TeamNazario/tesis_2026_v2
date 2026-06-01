package com.example.demo.service;

import com.example.demo.dto.CotizacionDetalleRequest;
import com.example.demo.dto.CotizacionDetalleResponse;
import com.example.demo.dto.CotizacionRequest;
import com.example.demo.dto.CotizacionResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.CotizacionMapper;
import com.example.demo.model.Cliente;
import com.example.demo.model.Cotizacion;
import com.example.demo.model.CotizacionDetalle;
import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.CotizacionDetalleRepository;
import com.example.demo.repository.CotizacionRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.util.ValidationErrors;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CotizacionService extends CrudService<Cotizacion, Integer> {
    private final CotizacionRepository cotizacionRepository;
    private final CotizacionDetalleRepository detalleRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final CotizacionMapper cotizacionMapper;
    private final BigDecimal igvRate;

    public CotizacionService(
            CotizacionRepository repository,
            CotizacionDetalleRepository detalleRepository,
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository,
            CotizacionMapper cotizacionMapper,
            @Value("${app.business.igv-rate:0.18}") BigDecimal igvRate
    ) {
        super(repository, "Cotizacion");
        this.cotizacionRepository = repository;
        this.detalleRepository = detalleRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.cotizacionMapper = cotizacionMapper;
        this.igvRate = igvRate;
    }

    @Override
    protected Integer parseId(String id) {
        return parseIntegerId(id);
    }

    @Transactional(readOnly = true)
    public List<CotizacionResponse> findAllDto() {
        return cotizacionRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CotizacionResponse findDtoById(Integer id) {
        return toResponse(findCotizacion(id));
    }

    @Transactional(readOnly = true)
    public CotizacionResponse findDtoByUuid(String uuidPublico) {
        if (uuidPublico == null || uuidPublico.isBlank()) {
            throw new IllegalArgumentException("El UUID publico de la cotizacion es obligatorio.");
        }
        Cotizacion cotizacion = cotizacionRepository.findByUuidPublico(uuidPublico)
                .orElseThrow(() -> new ResourceNotFoundException("Cotizacion", uuidPublico));
        return toResponse(cotizacion);
    }

    @Transactional(readOnly = true)
    public List<CotizacionDetalleResponse> findDetallesByCotizacion(Integer idCotizacion) {
        if (!cotizacionRepository.existsById(idCotizacion)) {
            throw new ResourceNotFoundException("Cotizacion", idCotizacion);
        }
        return detalleRepository.findByCotizacionIdCotizacion(idCotizacion).stream()
                .map(cotizacionMapper::toDetalleResponse)
                .toList();
    }

    @Transactional
    public CotizacionResponse create(CotizacionRequest request) {
        validateRequest(request);
        Cotizacion cotizacion = cotizacionMapper.toEntity(
                request,
                findCliente(request.idCliente()),
                findOptionalUsuario(request.idVendedor())
        );
        cotizacion.uuidPublico = UUID.randomUUID().toString();
        cotizacion.fechaEmision = request.fechaEmision() == null ? LocalDateTime.now() : request.fechaEmision();
        List<CotizacionDetalle> unsavedDetalles = buildDetalles(cotizacion, request.detalles());
        applyCalculatedTotals(cotizacion, unsavedDetalles);

        Cotizacion saved = cotizacionRepository.save(cotizacion);
        unsavedDetalles.forEach(detalle -> detalle.cotizacion = saved);
        List<CotizacionDetalle> detalles = detalleRepository.saveAll(unsavedDetalles);
        return cotizacionMapper.toResponse(saved, detalles);
    }

    @Transactional
    public CotizacionResponse update(Integer id, CotizacionRequest request) {
        validateRequest(request);
        Cotizacion cotizacion = findCotizacion(id);
        cotizacionMapper.updateEntity(
                cotizacion,
                request,
                findCliente(request.idCliente()),
                findOptionalUsuario(request.idVendedor())
        );
        List<CotizacionDetalle> unsavedDetalles = buildDetalles(cotizacion, request.detalles());
        applyCalculatedTotals(cotizacion, unsavedDetalles);

        Cotizacion saved = cotizacionRepository.save(cotizacion);
        detalleRepository.deleteAll(detalleRepository.findByCotizacionIdCotizacion(id));
        unsavedDetalles.forEach(detalle -> detalle.cotizacion = saved);
        List<CotizacionDetalle> detalles = detalleRepository.saveAll(unsavedDetalles);
        return cotizacionMapper.toResponse(saved, detalles);
    }

    @Transactional
    public void deleteById(Integer id) {
        Cotizacion cotizacion = findCotizacion(id);
        detalleRepository.deleteAll(detalleRepository.findByCotizacionIdCotizacion(id));
        cotizacionRepository.delete(cotizacion);
    }

    private CotizacionResponse toResponse(Cotizacion cotizacion) {
        List<CotizacionDetalle> detalles = detalleRepository.findByCotizacionIdCotizacion(cotizacion.idCotizacion);
        return cotizacionMapper.toResponse(cotizacion, detalles);
    }

    private List<CotizacionDetalle> buildDetalles(
            Cotizacion cotizacion,
            List<CotizacionDetalleRequest> requestDetalles
    ) {
        return requestDetalles.stream()
                .map(request -> toDetalleEntity(cotizacion, request))
                .toList();
    }

    private CotizacionDetalle toDetalleEntity(Cotizacion cotizacion, CotizacionDetalleRequest request) {
        Producto producto = findProducto(request.idProducto());
        CotizacionDetalle detalle = new CotizacionDetalle();
        detalle.cotizacion = cotizacion;
        detalle.producto = producto;
        detalle.cantidad = request.cantidad();
        detalle.precioUnitarioAplicado = request.precioUnitarioAplicado();
        detalle.subtotalLinea = request.precioUnitarioAplicado().multiply(BigDecimal.valueOf(request.cantidad()));
        return detalle;
    }

    private void applyCalculatedTotals(Cotizacion cotizacion, List<CotizacionDetalle> detalles) {
        BigDecimal subtotal = detalles.stream()
                .map(detalle -> detalle.subtotalLinea)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal igv = subtotal.multiply(igvRate).setScale(2, RoundingMode.HALF_UP);

        cotizacion.subtotal = subtotal;
        cotizacion.igv = igv;
        cotizacion.montoTotal = subtotal.add(igv).setScale(2, RoundingMode.HALF_UP);
    }

    private Cotizacion findCotizacion(Integer id) {
        return cotizacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cotizacion", id));
    }

    private Cliente findCliente(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
    }

    private Usuario findOptionalUsuario(Integer id) {
        if (id == null) {
            return null;
        }
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }

    private Producto findProducto(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
    }

    private void validateRequest(CotizacionRequest request) {
        ValidationErrors errors = new ValidationErrors();
        LocalDateTime fechaEmision = request.fechaEmision() == null ? LocalDateTime.now() : request.fechaEmision();

        if (request.fechaVencimiento() != null && request.fechaVencimiento().isBefore(fechaEmision)) {
            errors.add("fechaVencimiento", "La fecha de vencimiento no puede ser anterior a la fecha de emision.");
        }

        if (request.detalles() != null && request.detalles().stream()
                .anyMatch(detalle -> detalle.precioUnitarioAplicado() != null
                        && detalle.cantidad() != null
                        && detalle.precioUnitarioAplicado()
                        .multiply(BigDecimal.valueOf(detalle.cantidad()))
                        .compareTo(BigDecimal.ZERO) == 0)) {
            errors.add("detalles", "Cada detalle debe tener un subtotal mayor a 0.");
        }

        errors.throwIfAny("La cotizacion contiene datos inconsistentes.");
    }
}

package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CotizacionV1Service {
    private final CotizacionRepository cotizacionRepository;
    private final CotizacionDetalleRepository detalleRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final EstadoCotizacionRepository estadoCotizacionRepository;
    private final BigDecimal igvRate;

    public CotizacionV1Service(
            CotizacionRepository cotizacionRepository,
            CotizacionDetalleRepository detalleRepository,
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository,
            EstadoCotizacionRepository estadoCotizacionRepository,
            @Value("${app.business.igv-rate:0.18}") BigDecimal igvRate
    ) {
        this.cotizacionRepository = cotizacionRepository;
        this.detalleRepository = detalleRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.estadoCotizacionRepository = estadoCotizacionRepository;
        this.igvRate = igvRate;
    }

    @Transactional(readOnly = true)
    public List<CotizacionV1Response> findAll() { return cotizacionRepository.findAll().stream().map(this::map).toList(); }

    @Transactional(readOnly = true)
    public CotizacionV1Response findById(Integer id) { return map(findEntity(id)); }

    @Transactional
    public CotizacionV1Response create(CotizacionCreateRequest request) {
        validateCreateRequest(request);
        Cotizacion c = new Cotizacion();
        c.cliente = clienteRepository.findById(request.idCliente()).orElseThrow(() -> new ResourceNotFoundException("Cliente", request.idCliente()));
        c.vendedor = usuarioRepository.findById(request.idVendedor()).orElseThrow(() -> new ResourceNotFoundException("Usuario", request.idVendedor()));
        c.uuidPublico = UUID.randomUUID().toString();
        c.fechaEmision = request.fechaEmision() == null ? LocalDateTime.now() : request.fechaEmision();
        c.fechaVencimiento = request.fechaVencimiento();
        c.moneda = request.moneda();
        c.direccionDespacho = request.direccionDespacho();
        c.depProvDis = request.depProvDis();
        c.flagCubierto = request.flagCubierto() != null && request.flagCubierto();
        c.observaciones = request.observaciones();
        c.estadoCotizacionRef = estadoCotizacionRepository.findById(request.idEstadoCotizacion()).orElseThrow(() -> new ResourceNotFoundException("EstadoCotizacion", request.idEstadoCotizacion()));
        c.estadoCotizacion = c.estadoCotizacionRef.descEstadoCotizacion;
        c.origenCotizacion = "MANUAL";
        c.pdfPath = request.pdfPath();

        Cotizacion saved = cotizacionRepository.save(c);
        BigDecimal subtotal = BigDecimal.ZERO;
        for (DetalleCotizacionRequest d : request.detalles()) {
            Producto p = productoRepository.findById(d.idProducto()).orElseThrow(() -> new ResourceNotFoundException("Producto", d.idProducto()));
            CotizacionDetalle det = new CotizacionDetalle();
            det.cotizacion = saved;
            det.producto = p;
            det.cantidad = d.cantidad();
            det.precioUni = d.precioUni();
            det.precioUnitarioAplicado = d.precioUni();
            det.subtotalLinea = d.precioUni().multiply(BigDecimal.valueOf(d.cantidad()));
            subtotal = subtotal.add(det.subtotalLinea);
            detalleRepository.save(det);
        }
        c.subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
        c.igv = c.subtotal.multiply(igvRate).setScale(2, RoundingMode.HALF_UP);
        c.importeTotal = c.subtotal.add(c.igv).setScale(2, RoundingMode.HALF_UP);
        c.montoTotal = c.importeTotal;
        return map(cotizacionRepository.save(c));
    }

    @Transactional
    public CotizacionV1Response patchEstado(Integer id, Integer estadoId) {
        Cotizacion c = findEntity(id);
        c.estadoCotizacionRef = estadoCotizacionRepository.findById(estadoId).orElseThrow(() -> new ResourceNotFoundException("EstadoCotizacion", estadoId));
        c.estadoCotizacion = c.estadoCotizacionRef.descEstadoCotizacion;
        return map(cotizacionRepository.save(c));
    }

    private Cotizacion findEntity(Integer id) { return cotizacionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cotizacion", id)); }

    private CotizacionV1Response map(Cotizacion c) {
        List<DetalleCotizacionV1Response> detalles = detalleRepository.findByCotizacionIdCotizacion(c.idCotizacion).stream()
                .map(d -> new DetalleCotizacionV1Response(
                        d.idDetalle,
                        c.idCotizacion,
                        d.producto == null ? null : d.producto.idProducto,
                        d.producto == null ? null : d.producto.nombreProducto,
                        d.cantidad,
                        d.precioUni == null ? d.precioUnitarioAplicado : d.precioUni
                )).toList();
        return new CotizacionV1Response(
                c.idCotizacion,
                c.cliente == null ? null : c.cliente.idCliente,
                c.cliente == null ? null : c.cliente.razonSocial,
                c.vendedor == null ? null : c.vendedor.idUsuario,
                c.vendedor == null ? null : c.vendedor.nombres,
                c.fechaEmision,
                c.fechaVencimiento,
                c.moneda,
                c.subtotal,
                c.igv,
                c.importeTotal == null ? c.montoTotal : c.importeTotal,
                c.direccionDespacho,
                c.depProvDis,
                c.flagCubierto,
                c.observaciones,
                c.estadoCotizacionRef == null ? null : c.estadoCotizacionRef.idEstadoCotizacion,
                c.estadoCotizacionRef == null ? c.estadoCotizacion : c.estadoCotizacionRef.descEstadoCotizacion,
                c.pdfPath,
                detalles
        );
    }

    private void validateCreateRequest(CotizacionCreateRequest request) {
        if (request.detalles() == null || request.detalles().isEmpty()) {
            throw new IllegalArgumentException("La cotizacion debe contener al menos un detalle.");
        }
        LocalDateTime emision = request.fechaEmision() == null ? LocalDateTime.now() : request.fechaEmision();
        if (request.fechaVencimiento() != null && request.fechaVencimiento().isBefore(emision)) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a la fecha de emision.");
        }
    }

}

package com.example.demo.service;

import com.example.demo.dto.CotizacionCalcularItemRequest;
import com.example.demo.dto.CotizacionCalcularItemResponse;
import com.example.demo.dto.CotizacionCalcularResumenRequest;
import com.example.demo.dto.CotizacionCalcularResumenResponse;
import com.example.demo.dto.CotizacionCreateRequest;
import com.example.demo.dto.CotizacionPrecioProductoResponse;
import com.example.demo.dto.CotizacionResumenDetalleRequest;
import com.example.demo.dto.CotizacionV1Response;
import com.example.demo.dto.DetalleCotizacionRequest;
import com.example.demo.dto.DetalleCotizacionV1Response;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Cliente;
import com.example.demo.model.Cotizacion;
import com.example.demo.model.CotizacionDetalle;
import com.example.demo.model.EstadoCotizacion;
import com.example.demo.model.PrecioTipoCliente;
import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.CotizacionDetalleRepository;
import com.example.demo.repository.CotizacionRepository;
import com.example.demo.repository.EstadoCotizacionRepository;
import com.example.demo.repository.PrecioTipoClienteRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CotizacionV1Service {
    private static final int ESTADO_ACTIVO = 1;

    private final CotizacionRepository cotizacionRepository;
    private final CotizacionDetalleRepository detalleRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final EstadoCotizacionRepository estadoCotizacionRepository;
    private final PrecioTipoClienteRepository precioTipoClienteRepository;
    private final BigDecimal igvRate;

    public CotizacionV1Service(
            CotizacionRepository cotizacionRepository,
            CotizacionDetalleRepository detalleRepository,
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository,
            EstadoCotizacionRepository estadoCotizacionRepository,
            PrecioTipoClienteRepository precioTipoClienteRepository,
            @Value("${app.business.igv-rate:0.18}") BigDecimal igvRate
    ) {
        this.cotizacionRepository = cotizacionRepository;
        this.detalleRepository = detalleRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.estadoCotizacionRepository = estadoCotizacionRepository;
        this.precioTipoClienteRepository = precioTipoClienteRepository;
        this.igvRate = igvRate;
    }

    @Transactional(readOnly = true)
    public List<CotizacionV1Response> findAll(
            String search,
            Integer idCliente,
            Integer idVendedor,
            Integer idEstadoCotizacion,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        LocalDateTime inicio = fechaInicio == null ? null : fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin == null ? null : fechaFin.atTime(LocalTime.MAX);
        return cotizacionRepository.buscar(normalizeText(search), idCliente, idVendedor, idEstadoCotizacion, inicio, fin)
                .stream()
                .map(this::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public CotizacionV1Response findById(Integer id) {
        return map(findEntity(id));
    }

    @Transactional(readOnly = true)
    public CotizacionPrecioProductoResponse obtenerPrecioProducto(Integer idCliente, Integer idProducto, String moneda) {
        Cliente cliente = findCliente(idCliente);
        Producto producto = findProducto(idProducto);
        PrecioTipoCliente precio = findPrecio(cliente, producto, moneda);
        return new CotizacionPrecioProductoResponse(
                producto.idProducto,
                producto.nombreProducto,
                producto.unidadMedida,
                cliente.tipoCliente == null ? cliente.idTipoCliente : cliente.tipoCliente.idTipoCliente,
                cliente.tipoCliente == null ? null : cliente.tipoCliente.descTipoCliente,
                money(precio.precioUnitario),
                moneda
        );
    }

    @Transactional(readOnly = true)
    public CotizacionCalcularItemResponse calcularItem(CotizacionCalcularItemRequest request) {
        Cliente cliente = findCliente(request.idCliente());
        Producto producto = findProducto(request.idProducto());
        validateProducto(producto, request.cantidad());
        PrecioTipoCliente precio = findPrecio(cliente, producto, request.moneda());
        return buildItem(producto, request.cantidad(), precio.precioUnitario, request.moneda());
    }

    @Transactional(readOnly = true)
    public CotizacionCalcularResumenResponse calcularResumen(CotizacionCalcularResumenRequest request) {
        Cliente cliente = findCliente(request.idCliente());
        return calcularResumen(cliente, request.moneda(), request.detalles());
    }

    @Transactional
    public CotizacionV1Response create(CotizacionCreateRequest request, String actor) {
        Cliente cliente = findCliente(request.idCliente());
        Usuario vendedor = findUsuario(request.idVendedor());
        EstadoCotizacion estado = estadoCotizacionRepository.findById(request.idEstadoCotizacion())
                .orElseThrow(() -> new ResourceNotFoundException("EstadoCotizacion", request.idEstadoCotizacion()));
        validateFechaVencimiento(request.fechaVencimiento());
        LocalDateTime now = LocalDateTime.now();

        List<CotizacionResumenDetalleRequest> resumenDetalles = request.detalles().stream()
                .map(detalle -> new CotizacionResumenDetalleRequest(detalle.idProducto(), detalle.cantidad()))
                .toList();
        CotizacionCalcularResumenResponse resumen = calcularResumen(cliente, request.moneda(), resumenDetalles);

        Cotizacion cotizacion = new Cotizacion();
        cotizacion.cliente = cliente;
        cotizacion.vendedor = vendedor;
        cotizacion.fechaEmision = now;
        cotizacion.fechaVencimiento = request.fechaVencimiento();
        cotizacion.moneda = request.moneda();
        cotizacion.subtotal = resumen.subtotal();
        cotizacion.igv = resumen.igv();
        cotizacion.importeTotal = resumen.importeTotal();
        cotizacion.direccionDespacho = request.direccionDespacho();
        cotizacion.depProvDis = request.depProvDis();
        cotizacion.flagCubierto = request.flagCubierto() == null ? 0 : request.flagCubierto();
        cotizacion.observaciones = request.observaciones();
        cotizacion.estadoCotizacion = estado;
        cotizacion.usuRegistro = actor;
        cotizacion.fecRegistro = now;
        cotizacion.usuActualiza = null;
        cotizacion.fecActualiza = null;
        cotizacion.detalles = buildDetalles(cotizacion, request.detalles(), resumen.items(), actor, now);

        return map(cotizacionRepository.save(cotizacion));
    }

    @Transactional
    public CotizacionV1Response patchEstado(Integer id, Integer estadoId, String actor) {
        Cotizacion cotizacion = findEntity(id);
        cotizacion.estadoCotizacion = estadoCotizacionRepository.findById(estadoId)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoCotizacion", estadoId));
        markUpdated(cotizacion, actor);
        return map(cotizacionRepository.save(cotizacion));
    }

    @Transactional
    public void updatePdfPath(Integer idCotizacion, String pdfPath, String actor) {
        Cotizacion cotizacion = findEntity(idCotizacion);
        cotizacion.pdfPath = pdfPath;
        markUpdated(cotizacion, actor);
        cotizacionRepository.save(cotizacion);
    }

    @Transactional(readOnly = true)
    public Cotizacion findEntity(Integer id) {
        return cotizacionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cotizacion", id));
    }

    private CotizacionCalcularResumenResponse calcularResumen(
            Cliente cliente,
            String moneda,
            List<CotizacionResumenDetalleRequest> detalles
    ) {
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("La cotizacion debe contener al menos un detalle.");
        }
        List<CotizacionCalcularItemResponse> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CotizacionResumenDetalleRequest detalle : detalles) {
            Producto producto = findProducto(detalle.idProducto());
            validateProducto(producto, detalle.cantidad());
            PrecioTipoCliente precio = findPrecio(cliente, producto, moneda);
            CotizacionCalcularItemResponse item = buildItem(producto, detalle.cantidad(), precio.precioUnitario, moneda);
            items.add(item);
            subtotal = subtotal.add(item.importe());
        }
        subtotal = money(subtotal);
        BigDecimal igv = money(subtotal.multiply(igvRate));
        return new CotizacionCalcularResumenResponse(items, subtotal, igv, money(subtotal.add(igv)), moneda);
    }

    private List<CotizacionDetalle> buildDetalles(
            Cotizacion cotizacion,
            List<DetalleCotizacionRequest> requestDetalles,
            List<CotizacionCalcularItemResponse> items,
            String actor,
            LocalDateTime now
    ) {
        List<CotizacionDetalle> detalles = new ArrayList<>();
        for (int i = 0; i < requestDetalles.size(); i++) {
            DetalleCotizacionRequest request = requestDetalles.get(i);
            CotizacionCalcularItemResponse item = items.get(i);
            CotizacionDetalle detalle = new CotizacionDetalle();
            detalle.cotizacion = cotizacion;
            detalle.producto = findProducto(request.idProducto());
            detalle.cantidad = request.cantidad();
            detalle.precioUni = item.precioUnitario();
            detalle.usuRegistro = actor;
            detalle.fecRegistro = now;
            detalle.usuActualiza = null;
            detalle.fecActualiza = null;
            detalles.add(detalle);
        }
        return detalles;
    }

    private void markUpdated(Cotizacion cotizacion, String actor) {
        cotizacion.usuActualiza = actor;
        cotizacion.fecActualiza = LocalDateTime.now();
    }

    private CotizacionCalcularItemResponse buildItem(Producto producto, Integer cantidad, BigDecimal precioUnitario, String moneda) {
        BigDecimal precio = money(precioUnitario);
        BigDecimal importe = money(precio.multiply(BigDecimal.valueOf(cantidad)));
        return new CotizacionCalcularItemResponse(
                producto.idProducto,
                producto.nombreProducto,
                producto.unidadMedida,
                cantidad,
                precio,
                importe,
                moneda
        );
    }

    private PrecioTipoCliente findPrecio(Cliente cliente, Producto producto, String moneda) {
        Integer idTipoCliente = cliente.tipoCliente == null ? cliente.idTipoCliente : cliente.tipoCliente.idTipoCliente;
        if (idTipoCliente == null) {
            throw new IllegalStateException("El cliente no tiene tipo de cliente configurado.");
        }
        return precioTipoClienteRepository.findPrecioActivo(
                        producto.idProducto,
                        idTipoCliente,
                        currencyVariants(moneda)
                )
                .orElseThrow(() -> new IllegalStateException("No existe precio configurado para este producto y tipo de cliente."));
    }

    private void validateProducto(Producto producto, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        Integer estadoId = producto.estadoProducto == null ? producto.idEstadoProducto : producto.estadoProducto.idEstadoProducto;
        if (estadoId != null && estadoId != ESTADO_ACTIVO) {
            throw new IllegalStateException("El producto no se encuentra activo.");
        }
        if (producto.cantMinVenta != null && cantidad < producto.cantMinVenta) {
            throw new IllegalStateException("La cantidad no cumple la cantidad minima de venta del producto.");
        }
        Integer stockDisponible = producto.stockDisponible != null
                ? producto.stockDisponible
                : (producto.stockFisico == null || producto.stockReservado == null ? null : producto.stockFisico - producto.stockReservado);
        if (stockDisponible != null && cantidad > stockDisponible) {
            throw new IllegalStateException("El stock disponible del producto no es suficiente.");
        }
    }

    private void validateFechaVencimiento(LocalDateTime fechaVencimiento) {
        if (fechaVencimiento != null && fechaVencimiento.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a la fecha de emision.");
        }
    }

    private Cliente findCliente(Integer id) {
        return clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
    }

    private Usuario findUsuario(Integer id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }

    private Producto findProducto(Integer id) {
        return productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto", id));
    }

    private CotizacionV1Response map(Cotizacion c) {
        List<CotizacionDetalle> detalles = c.detalles == null || c.detalles.isEmpty()
                ? detalleRepository.findByCotizacionIdCotizacion(c.idCotizacion)
                : c.detalles;
        return new CotizacionV1Response(
                c.idCotizacion,
                c.cliente == null ? null : c.cliente.idCliente,
                c.cliente == null ? null : c.cliente.ruc,
                c.cliente == null ? null : c.cliente.razonSocial,
                c.cliente == null ? null : c.cliente.direccion,
                c.vendedor == null ? null : c.vendedor.idUsuario,
                c.vendedor == null ? null : fullName(c.vendedor),
                c.fechaEmision,
                c.fechaVencimiento,
                c.moneda,
                c.subtotal,
                c.igv,
                c.importeTotal,
                c.direccionDespacho,
                c.depProvDis,
                c.flagCubierto,
                c.observaciones,
                c.estadoCotizacion == null ? null : c.estadoCotizacion.idEstadoCotizacion,
                c.estadoCotizacion == null ? null : c.estadoCotizacion.descEstadoCotizacion,
                c.pdfPath,
                detalles.stream().map(this::mapDetalle).toList()
        );
    }

    private DetalleCotizacionV1Response mapDetalle(CotizacionDetalle d) {
        BigDecimal importe = d.precioUni == null || d.cantidad == null
                ? BigDecimal.ZERO
                : money(d.precioUni.multiply(BigDecimal.valueOf(d.cantidad)));
        return new DetalleCotizacionV1Response(
                d.idDetalleCoti,
                d.cotizacion == null ? null : d.cotizacion.idCotizacion,
                d.producto == null ? null : d.producto.idProducto,
                d.producto == null ? null : d.producto.nombreProducto,
                d.producto == null ? null : d.producto.unidadMedida,
                d.cantidad,
                d.precioUni,
                importe
        );
    }

    private String fullName(Usuario usuario) {
        return String.join(" ",
                safe(usuario.nombres),
                safe(usuario.apellidoPaterno),
                safe(usuario.apellidoMaterno)
        ).trim();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String normalizeText(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private List<String> currencyVariants(String moneda) {
        String normalized = moneda == null ? "" : moneda.trim().toLowerCase();
        if (normalized.equals("soles") || normalized.equals("pen")) {
            return List.of("soles", "pen");
        }
        if (normalized.equals("dolares") || normalized.equals("dólares") || normalized.equals("usd")) {
            return List.of("dolares", "dólares", "usd");
        }
        return List.of(normalized);
    }

    private BigDecimal money(BigDecimal value) {
        return (value == null ? BigDecimal.ZERO : value).setScale(2, RoundingMode.HALF_UP);
    }
}

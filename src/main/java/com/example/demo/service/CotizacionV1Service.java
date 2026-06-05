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
import java.time.Duration;
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
    private static final int VIGENCIA_HORAS = 24;

    private final CotizacionRepository cotizacionRepository;
    private final CotizacionDetalleRepository detalleRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final EstadoCotizacionRepository estadoCotizacionRepository;
    private final PrecioTipoClienteRepository precioTipoClienteRepository;
    private final ProductoStockService productoStockService;
    private final AuditoriaService auditoriaService;
    private final AccessControlService accessControlService;
    private final BigDecimal igvRate;

    public CotizacionV1Service(
            CotizacionRepository cotizacionRepository,
            CotizacionDetalleRepository detalleRepository,
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository,
            EstadoCotizacionRepository estadoCotizacionRepository,
            PrecioTipoClienteRepository precioTipoClienteRepository,
            ProductoStockService productoStockService,
            AuditoriaService auditoriaService,
            AccessControlService accessControlService,
            @Value("${app.business.igv-rate:0.18}") BigDecimal igvRate
    ) {
        this.cotizacionRepository = cotizacionRepository;
        this.detalleRepository = detalleRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.estadoCotizacionRepository = estadoCotizacionRepository;
        this.precioTipoClienteRepository = precioTipoClienteRepository;
        this.productoStockService = productoStockService;
        this.auditoriaService = auditoriaService;
        this.accessControlService = accessControlService;
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
        Integer vendedorPermitido = accessControlService.vendedorPermitido(idVendedor);
        return cotizacionRepository.buscar(normalizeText(search), idCliente, vendedorPermitido, idEstadoCotizacion, inicio, fin)
                .stream()
                .map(this::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public CotizacionV1Response findById(Integer id) {
        accessControlService.validarPuedeVerCotizacion(id);
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
        Integer idVendedor = accessControlService.vendedorParaCrearCotizacion(request.idVendedor());
        Usuario vendedor = findUsuario(idVendedor);
        EstadoCotizacion estado = findEstado(CotizacionEstadoNames.GENERADA);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fechaVencimiento = now.plusHours(VIGENCIA_HORAS);

        List<CotizacionResumenDetalleRequest> resumenDetalles = request.detalles().stream()
                .map(detalle -> new CotizacionResumenDetalleRequest(detalle.idProducto(), detalle.cantidad()))
                .toList();
        CotizacionCalcularResumenResponse resumen = calcularResumen(cliente, request.moneda(), resumenDetalles);

        Cotizacion cotizacion = new Cotizacion();
        cotizacion.cliente = cliente;
        cotizacion.vendedor = vendedor;
        cotizacion.fechaEmision = now;
        cotizacion.fechaVencimiento = fechaVencimiento;
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
        reservarStock(cotizacion, actor);

        CotizacionV1Response response = map(cotizacionRepository.save(cotizacion));
        auditoriaService.registrarCreacion("COTIZACION", String.valueOf(response.idCotizacion()), response, "COTIZACIONES", "Creacion de cotizacion y reserva de stock");
        return response;
    }

    @Transactional
    public CotizacionV1Response patchEstado(Integer id, Integer estadoId, String actor) {
        accessControlService.validarPuedeGestionarCotizacion(id);
        Cotizacion cotizacion = findEntity(id);
        CotizacionV1Response anterior = map(cotizacion);
        EstadoCotizacion nuevoEstado = estadoCotizacionRepository.findById(estadoId)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoCotizacion", estadoId));
        aplicarCambioEstado(cotizacion, nuevoEstado, actor);
        cotizacion.estadoCotizacion = nuevoEstado;
        markUpdated(cotizacion, actor);
        CotizacionV1Response nuevo = map(cotizacionRepository.save(cotizacion));
        String accion = same(nuevo.descEstadoCotizacion(), CotizacionEstadoNames.APROBADA) ? "APPROVE_QUOTATION" : "CHANGE_STATUS";
        auditoriaService.registrarAccion("COTIZACION", String.valueOf(id), accion, anterior, nuevo, "COTIZACIONES", "Cambio de estado de cotizacion");
        return nuevo;
    }

    @Transactional
    public int procesarCotizacionesVencidas(String actor) {
        EstadoCotizacion generada = findEstado(CotizacionEstadoNames.GENERADA);
        EstadoCotizacion vencida = findEstado(CotizacionEstadoNames.VENCIDA);
        List<Cotizacion> vencidas = cotizacionRepository.findVencidasGeneradas(generada.idEstadoCotizacion, LocalDateTime.now());
        for (Cotizacion cotizacion : vencidas) {
            CotizacionV1Response anterior = map(cotizacion);
            productoStockService.liberarStockReservado(cotizacion, actor);
            cotizacion.estadoCotizacion = vencida;
            markUpdated(cotizacion, actor);
            CotizacionV1Response nuevo = map(cotizacion);
            auditoriaService.registrarAccion("COTIZACION", String.valueOf(cotizacion.idCotizacion), "EXPIRE_QUOTATION", anterior, nuevo, "COTIZACIONES", "Proceso automatico de vencimiento de cotizaciones");
        }
        cotizacionRepository.saveAll(vencidas);
        return vencidas.size();
    }

    @Transactional
    public void updatePdfPath(Integer idCotizacion, String pdfPath, String actor) {
        accessControlService.validarPuedeGestionarCotizacion(idCotizacion);
        Cotizacion cotizacion = findEntity(idCotizacion);
        CotizacionV1Response anterior = map(cotizacion);
        cotizacion.pdfPath = pdfPath;
        markUpdated(cotizacion, actor);
        CotizacionV1Response nuevo = map(cotizacionRepository.save(cotizacion));
        auditoriaService.registrarAccion("COTIZACION", String.valueOf(idCotizacion), "GENERATE_PDF", anterior, nuevo, "COTIZACIONES", "Generacion de PDF de cotizacion");
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
            productoStockService.validarProductoCotizable(producto, detalle.cantidad());
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
        productoStockService.validarProductoCotizable(producto, cantidad);
        Integer stockDisponible = producto.stockDisponible != null
                ? producto.stockDisponible
                : (producto.stockFisico == null || producto.stockReservado == null ? null : producto.stockFisico - producto.stockReservado);
        if (stockDisponible != null && cantidad > stockDisponible) {
            throw new IllegalStateException("Stock insuficiente para el producto " + producto.nombreProducto + ".");
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
                isVencida(c),
                puedeAprobarse(c),
                tiempoRestanteSegundos(c),
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

    private void reservarStock(Cotizacion cotizacion, String actor) {
        for (CotizacionDetalle detalle : cotizacion.detalles) {
            Producto producto = productoStockService.findForUpdate(detalle.producto.idProducto);
            productoStockService.reservarStock(producto, detalle.cantidad, actor);
        }
    }

    private void aplicarCambioEstado(Cotizacion cotizacion, EstadoCotizacion nuevoEstado, String actor) {
        String actual = estadoNombre(cotizacion.estadoCotizacion);
        String nuevo = estadoNombre(nuevoEstado);

        if (same(nuevo, CotizacionEstadoNames.APROBADA)) {
            if (!same(actual, CotizacionEstadoNames.GENERADA)) {
                throw new IllegalArgumentException("Transicion de estado no permitida.");
            }
            if (cotizacion.fechaVencimiento != null && !LocalDateTime.now().isBefore(cotizacion.fechaVencimiento)) {
                throw new IllegalStateException("No se puede aprobar una cotizacion vencida.");
            }
            productoStockService.confirmarSalidaReservada(cotizacion, actor);
            return;
        }
        if (same(nuevo, CotizacionEstadoNames.RECHAZADA)
                || same(nuevo, CotizacionEstadoNames.ANULADA)
                || same(nuevo, CotizacionEstadoNames.VENCIDA)) {
            if (same(actual, CotizacionEstadoNames.GENERADA)) {
                productoStockService.liberarStockReservado(cotizacion, actor);
            }
            return;
        }
        if (!same(nuevo, actual)) {
            throw new IllegalArgumentException("Transicion de estado no permitida.");
        }
    }

    private EstadoCotizacion findEstado(String descripcion) {
        return estadoCotizacionRepository.findByDescEstadoCotizacionIgnoreCase(descripcion)
                .orElseThrow(() -> new IllegalStateException("No existe el estado de cotizacion " + descripcion + "."));
    }

    private boolean isVencida(Cotizacion cotizacion) {
        return same(estadoNombre(cotizacion.estadoCotizacion), CotizacionEstadoNames.VENCIDA)
                || (same(estadoNombre(cotizacion.estadoCotizacion), CotizacionEstadoNames.GENERADA)
                && cotizacion.fechaVencimiento != null
                && !LocalDateTime.now().isBefore(cotizacion.fechaVencimiento));
    }

    private boolean puedeAprobarse(Cotizacion cotizacion) {
        return same(estadoNombre(cotizacion.estadoCotizacion), CotizacionEstadoNames.GENERADA) && !isVencida(cotizacion);
    }

    private Long tiempoRestanteSegundos(Cotizacion cotizacion) {
        if (cotizacion.fechaVencimiento == null || isVencida(cotizacion)) {
            return 0L;
        }
        return Math.max(0L, Duration.between(LocalDateTime.now(), cotizacion.fechaVencimiento).getSeconds());
    }

    private String estadoNombre(EstadoCotizacion estado) {
        return estado == null ? "" : estado.descEstadoCotizacion;
    }

    private boolean same(String a, String b) {
        return a != null && b != null && a.trim().equalsIgnoreCase(b.trim());
    }
}

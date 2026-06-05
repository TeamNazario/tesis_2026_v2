package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Cotizacion;
import com.example.demo.model.CotizacionDetalle;
import com.example.demo.model.Producto;
import com.example.demo.repository.ProductoRepository;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ProductoStockService {
    private static final int ESTADO_ACTIVO = 1;

    private final ProductoRepository productoRepository;
    private final AuditoriaService auditoriaService;

    public ProductoStockService(ProductoRepository productoRepository, AuditoriaService auditoriaService) {
        this.productoRepository = productoRepository;
        this.auditoriaService = auditoriaService;
    }

    public Producto findForUpdate(Integer idProducto) {
        return productoRepository.findByIdForUpdate(idProducto)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", idProducto));
    }

    public void validarProductoCotizable(Producto producto, Integer cantidad) {
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
    }

    public void reservarStock(Producto producto, Integer cantidad, String actor) {
        validarProductoCotizable(producto, cantidad);
        if (stockDisponible(producto) < cantidad) {
            throw new IllegalStateException("Stock insuficiente para el producto " + producto.nombreProducto + ".");
        }
        Map<String, Object> anterior = stockSnapshot(producto);
        producto.stockDisponible = stockDisponible(producto) - cantidad;
        producto.stockReservado = stockReservado(producto) + cantidad;
        markUpdated(producto, actor);
        auditoriaService.registrarAccion("PRODUCTO", String.valueOf(producto.idProducto), "RESERVE_STOCK", anterior, stockSnapshot(producto), "STOCK", "Reserva de stock por cotizacion");
    }

    public void confirmarSalida(Producto producto, Integer cantidad, String actor) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        if (stockReservado(producto) < cantidad) {
            throw new IllegalStateException("Stock reservado inconsistente para el producto " + producto.nombreProducto + ".");
        }
        if (stockFisico(producto) < cantidad) {
            throw new IllegalStateException("Stock fisico insuficiente para el producto " + producto.nombreProducto + ".");
        }
        Map<String, Object> anterior = stockSnapshot(producto);
        producto.stockFisico = stockFisico(producto) - cantidad;
        producto.stockReservado = stockReservado(producto) - cantidad;
        producto.stockDisponible = stockDisponible(producto);
        markUpdated(producto, actor);
        auditoriaService.registrarAccion("PRODUCTO", String.valueOf(producto.idProducto), "CONFIRM_STOCK", anterior, stockSnapshot(producto), "STOCK", "Confirmacion de salida de stock reservado");
    }

    public void liberarStock(Producto producto, Integer cantidad, String actor) {
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        if (stockReservado(producto) < cantidad) {
            throw new IllegalStateException("Stock reservado inconsistente para el producto " + producto.nombreProducto + ".");
        }
        Map<String, Object> anterior = stockSnapshot(producto);
        producto.stockReservado = stockReservado(producto) - cantidad;
        producto.stockDisponible = stockDisponible(producto) + cantidad;
        markUpdated(producto, actor);
        auditoriaService.registrarAccion("PRODUCTO", String.valueOf(producto.idProducto), "RELEASE_STOCK", anterior, stockSnapshot(producto), "STOCK", "Liberacion de stock reservado");
    }

    public void liberarStockReservado(Cotizacion cotizacion, String actor) {
        for (CotizacionDetalle detalle : cotizacion.detalles) {
            Producto producto = findForUpdate(detalle.producto.idProducto);
            liberarStock(producto, detalle.cantidad, actor);
        }
    }

    public void confirmarSalidaReservada(Cotizacion cotizacion, String actor) {
        for (CotizacionDetalle detalle : cotizacion.detalles) {
            Producto producto = findForUpdate(detalle.producto.idProducto);
            confirmarSalida(producto, detalle.cantidad, actor);
        }
    }

    private int stockFisico(Producto producto) {
        return producto.stockFisico == null ? 0 : producto.stockFisico;
    }

    private int stockReservado(Producto producto) {
        return producto.stockReservado == null ? 0 : producto.stockReservado;
    }

    private int stockDisponible(Producto producto) {
        if (producto.stockDisponible != null) {
            return producto.stockDisponible;
        }
        return stockFisico(producto) - stockReservado(producto);
    }

    private void markUpdated(Producto producto, String actor) {
        producto.usuActualiza = actor;
        producto.fecActualiza = LocalDateTime.now();
    }

    private Map<String, Object> stockSnapshot(Producto producto) {
        Map<String, Object> snapshot = new LinkedHashMap<>();
        snapshot.put("idProducto", producto.idProducto);
        snapshot.put("nombreProducto", producto.nombreProducto);
        snapshot.put("stockFisico", producto.stockFisico);
        snapshot.put("stockReservado", producto.stockReservado);
        snapshot.put("stockDisponible", producto.stockDisponible);
        snapshot.put("stockMinimoSeguridad", producto.stockMinimoSeguridad);
        return snapshot;
    }
}

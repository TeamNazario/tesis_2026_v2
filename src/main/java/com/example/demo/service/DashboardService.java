package com.example.demo.service;

import com.example.demo.dto.CotizacionPorEstadoResponse;
import com.example.demo.dto.CotizacionPorVendedorResponse;
import com.example.demo.dto.CotizacionTendenciaResponse;
import com.example.demo.dto.DashboardResumenResponse;
import com.example.demo.dto.ProductoStockBajoResponse;
import com.example.demo.dto.StockProductoDashboardResponse;
import com.example.demo.dto.TopClienteResponse;
import com.example.demo.dto.TopProductoResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {
    @PersistenceContext
    private EntityManager entityManager;
    private final AccessControlService accessControlService;

    public DashboardService(AccessControlService accessControlService) {
        this.accessControlService = accessControlService;
    }

    @Transactional(readOnly = true)
    public DashboardResumenResponse resumen(DashboardFilter filter) {
        filter = scoped(filter);
        Object[] row = singleRow("""
                SELECT COUNT(c),
                       COALESCE(SUM(CASE WHEN LOWER(e.descEstadoCotizacion) = 'generada' THEN 1 ELSE 0 END), 0),
                       COALESCE(SUM(CASE WHEN LOWER(e.descEstadoCotizacion) = 'aprobada' THEN 1 ELSE 0 END), 0),
                       COALESCE(SUM(CASE WHEN LOWER(e.descEstadoCotizacion) = 'vencida' THEN 1 ELSE 0 END), 0),
                       COALESCE(SUM(CASE WHEN LOWER(e.descEstadoCotizacion) = 'rechazada' THEN 1 ELSE 0 END), 0),
                       COALESCE(SUM(CASE WHEN LOWER(e.descEstadoCotizacion) = 'anulada' THEN 1 ELSE 0 END), 0),
                       COALESCE(SUM(c.importeTotal), 0),
                       COALESCE(SUM(CASE WHEN LOWER(e.descEstadoCotizacion) = 'aprobada' THEN c.importeTotal ELSE 0 END), 0),
                       COUNT(DISTINCT c.cliente.idCliente)
                FROM Cotizacion c
                JOIN c.estadoCotizacion e
                """ + cotizacionWhere("c"), filter);

        long total = longValue(row[0]);
        long aprobadas = longValue(row[2]);
        BigDecimal importeCotizado = money(row[6]);
        BigDecimal importeAprobado = money(row[7]);
        BigDecimal ticketPromedio = total == 0 ? BigDecimal.ZERO : importeCotizado.divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
        BigDecimal tasaConversion = total == 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(aprobadas).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);

        return new DashboardResumenResponse(
                total,
                longValue(row[1]),
                aprobadas,
                longValue(row[3]),
                longValue(row[4]),
                longValue(row[5]),
                importeCotizado,
                importeAprobado,
                ticketPromedio,
                tasaConversion,
                longValue(row[8]),
                productosCotizados(filter),
                stockFisicoTotal(filter),
                stockReservadoTotal(filter),
                stockDisponibleTotal(filter),
                productosStockBajoCount(filter)
        );
    }

    @Transactional(readOnly = true)
    public List<CotizacionTendenciaResponse> cotizacionesTendencia(DashboardFilter filter) {
        filter = scoped(filter);
        Query query = query("""
                SELECT FUNCTION('date', c.fechaEmision), COUNT(c), COALESCE(SUM(c.importeTotal), 0)
                FROM Cotizacion c
                JOIN c.estadoCotizacion e
                """ + cotizacionWhere("c") + """
                GROUP BY FUNCTION('date', c.fechaEmision)
                ORDER BY FUNCTION('date', c.fechaEmision)
                """, filter);
        return rows(query).stream()
                .map(row -> new CotizacionTendenciaResponse(localDate(row[0]), longValue(row[1]), money(row[2])))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CotizacionPorEstadoResponse> cotizacionesPorEstado(DashboardFilter filter) {
        filter = scoped(filter);
        Query query = query("""
                SELECT e.descEstadoCotizacion, COUNT(c), COALESCE(SUM(c.importeTotal), 0)
                FROM Cotizacion c
                JOIN c.estadoCotizacion e
                """ + cotizacionWhere("c") + """
                GROUP BY e.descEstadoCotizacion
                ORDER BY COUNT(c) DESC
                """, filter);
        return rows(query).stream()
                .map(row -> new CotizacionPorEstadoResponse(stringValue(row[0]), longValue(row[1]), money(row[2])))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TopProductoResponse> topProductos(DashboardFilter filter, int limit) {
        filter = scoped(filter);
        Query query = query("""
                SELECT p.idProducto, p.nombreProducto, p.unidadMedida, COALESCE(SUM(d.cantidad), 0), COALESCE(SUM(d.cantidad * d.precioUni), 0)
                FROM CotizacionDetalle d
                JOIN d.producto p
                JOIN d.cotizacion c
                JOIN c.estadoCotizacion e
                """ + cotizacionWhere("c") + """
                GROUP BY p.idProducto, p.nombreProducto, p.unidadMedida
                ORDER BY SUM(d.cantidad) DESC, SUM(d.cantidad * d.precioUni) DESC
                """, filter);
        query.setMaxResults(limit(limit));
        return rows(query).stream()
                .map(row -> new TopProductoResponse(intValue(row[0]), stringValue(row[1]), stringValue(row[2]), longValue(row[3]), money(row[4])))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TopClienteResponse> topClientes(DashboardFilter filter, int limit) {
        filter = scoped(filter);
        Query query = query("""
                SELECT c.cliente.idCliente, c.cliente.razonSocial, c.cliente.ruc, COUNT(c), COALESCE(SUM(c.importeTotal), 0)
                FROM Cotizacion c
                JOIN c.estadoCotizacion e
                """ + cotizacionWhere("c") + """
                GROUP BY c.cliente.idCliente, c.cliente.razonSocial, c.cliente.ruc
                ORDER BY SUM(c.importeTotal) DESC, COUNT(c) DESC
                """, filter);
        query.setMaxResults(limit(limit));
        return rows(query).stream()
                .map(row -> new TopClienteResponse(intValue(row[0]), stringValue(row[1]), stringValue(row[2]), longValue(row[3]), money(row[4])))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CotizacionPorVendedorResponse> cotizacionesPorVendedor(DashboardFilter filter, int limit) {
        filter = scoped(filter);
        Query query = query("""
                SELECT c.vendedor.idUsuario,
                       CONCAT(c.vendedor.nombres, ' ', c.vendedor.apellidoPaterno, ' ', COALESCE(c.vendedor.apellidoMaterno, '')),
                       COUNT(c),
                       COALESCE(SUM(CASE WHEN LOWER(e.descEstadoCotizacion) = 'aprobada' THEN 1 ELSE 0 END), 0),
                       COALESCE(SUM(CASE WHEN LOWER(e.descEstadoCotizacion) = 'aprobada' THEN c.importeTotal ELSE 0 END), 0)
                FROM Cotizacion c
                JOIN c.estadoCotizacion e
                """ + cotizacionWhere("c") + """
                GROUP BY c.vendedor.idUsuario, c.vendedor.nombres, c.vendedor.apellidoPaterno, c.vendedor.apellidoMaterno
                ORDER BY COUNT(c) DESC
                """, filter);
        query.setMaxResults(limit(limit));
        return rows(query).stream()
                .map(row -> new CotizacionPorVendedorResponse(intValue(row[0]), stringValue(row[1]).trim(), longValue(row[2]), longValue(row[3]), money(row[4])))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StockProductoDashboardResponse> stockProductos(DashboardFilter filter, int limit) {
        filter = scoped(filter);
        Query query = entityManager.createQuery("""
                SELECT p.idProducto, p.nombreProducto, p.unidadMedida, p.stockFisico, p.stockReservado, p.stockDisponible, p.stockMinimoSeguridad
                FROM Producto p
                WHERE (:idProducto IS NULL OR p.idProducto = :idProducto)
                ORDER BY p.stockDisponible ASC, p.nombreProducto ASC
                """);
        query.setParameter("idProducto", filter.idProducto());
        query.setMaxResults(limit(limit));
        return rows(query).stream()
                .map(row -> {
                    Integer disponible = intValue(row[5]);
                    Integer minimo = intValue(row[6]);
                    return new StockProductoDashboardResponse(
                            intValue(row[0]),
                            stringValue(row[1]),
                            stringValue(row[2]),
                            intValue(row[3]),
                            intValue(row[4]),
                            disponible,
                            minimo,
                            disponible <= minimo
                    );
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoStockBajoResponse> productosStockBajo(DashboardFilter filter, int limit) {
        filter = scoped(filter);
        Query query = entityManager.createQuery("""
                SELECT p.idProducto, p.nombreProducto, p.unidadMedida, p.stockDisponible, p.stockMinimoSeguridad
                FROM Producto p
                WHERE (:idProducto IS NULL OR p.idProducto = :idProducto)
                  AND p.stockDisponible <= p.stockMinimoSeguridad
                ORDER BY p.stockDisponible ASC, p.nombreProducto ASC
                """);
        query.setParameter("idProducto", filter.idProducto());
        query.setMaxResults(limit(limit));
        return rows(query).stream()
                .map(row -> new ProductoStockBajoResponse(intValue(row[0]), stringValue(row[1]), stringValue(row[2]), intValue(row[3]), intValue(row[4])))
                .toList();
    }

    private long productosCotizados(DashboardFilter filter) {
        Object result = query("""
                SELECT COALESCE(SUM(d.cantidad), 0)
                FROM CotizacionDetalle d
                JOIN d.cotizacion c
                JOIN c.estadoCotizacion e
                """ + cotizacionWhere("c"), filter).getSingleResult();
        return longValue(result);
    }

    private long stockFisicoTotal(DashboardFilter filter) {
        return stockSum("p.stockFisico", filter);
    }

    private long stockReservadoTotal(DashboardFilter filter) {
        return stockSum("p.stockReservado", filter);
    }

    private long stockDisponibleTotal(DashboardFilter filter) {
        return stockSum("p.stockDisponible", filter);
    }

    private long productosStockBajoCount(DashboardFilter filter) {
        Query query = entityManager.createQuery("""
                SELECT COUNT(p)
                FROM Producto p
                WHERE (:idProducto IS NULL OR p.idProducto = :idProducto)
                  AND p.stockDisponible <= p.stockMinimoSeguridad
                """);
        query.setParameter("idProducto", filter.idProducto());
        return longValue(query.getSingleResult());
    }

    private long stockSum(String expression, DashboardFilter filter) {
        Query query = entityManager.createQuery("""
                SELECT COALESCE(SUM(%s), 0)
                FROM Producto p
                WHERE (:idProducto IS NULL OR p.idProducto = :idProducto)
                """.formatted(expression));
        query.setParameter("idProducto", filter.idProducto());
        return longValue(query.getSingleResult());
    }

    private String cotizacionWhere(String alias) {
        return """
                WHERE (:inicio IS NULL OR %s.fechaEmision >= :inicio)
                  AND (:fin IS NULL OR %s.fechaEmision <= :fin)
                  AND (:idVendedor IS NULL OR %s.vendedor.idUsuario = :idVendedor)
                  AND (:idCliente IS NULL OR %s.cliente.idCliente = :idCliente)
                  AND (:idTipoCliente IS NULL OR %s.cliente.tipoCliente.idTipoCliente = :idTipoCliente)
                  AND (:idEstadoCotizacion IS NULL OR %s.estadoCotizacion.idEstadoCotizacion = :idEstadoCotizacion)
                  AND (:moneda IS NULL OR LOWER(%s.moneda) = LOWER(:moneda))
                  AND (:idProducto IS NULL OR EXISTS (
                       SELECT 1 FROM CotizacionDetalle df
                       WHERE df.cotizacion = %s AND df.producto.idProducto = :idProducto
                  ))
                """.formatted(alias, alias, alias, alias, alias, alias, alias, alias);
    }

    private Query query(String jpql, DashboardFilter filter) {
        Query query = entityManager.createQuery(jpql);
        setCotizacionParams(query, filter);
        return query;
    }

    private Object[] singleRow(String jpql, DashboardFilter filter) {
        Object result = query(jpql, filter).getSingleResult();
        return (Object[]) result;
    }

    private void setCotizacionParams(Query query, DashboardFilter filter) {
        query.setParameter("inicio", filter.fechaInicio() == null ? null : filter.fechaInicio().atStartOfDay());
        query.setParameter("fin", filter.fechaFin() == null ? null : filter.fechaFin().atTime(LocalTime.MAX));
        query.setParameter("idVendedor", filter.idVendedor());
        query.setParameter("idCliente", filter.idCliente());
        query.setParameter("idProducto", filter.idProducto());
        query.setParameter("idTipoCliente", filter.idTipoCliente());
        query.setParameter("idEstadoCotizacion", filter.idEstadoCotizacion());
        query.setParameter("moneda", blankToNull(filter.moneda()));
    }

    @SuppressWarnings("unchecked")
    private List<Object[]> rows(Query query) {
        return query.getResultList();
    }

    private int limit(Integer value) {
        if (value == null || value <= 0) {
            return 10;
        }
        return Math.min(value, 50);
    }

    private Long longValue(Object value) {
        return value == null ? 0L : ((Number) value).longValue();
    }

    private Integer intValue(Object value) {
        return value == null ? 0 : ((Number) value).intValue();
    }

    private BigDecimal money(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal decimal) {
            return decimal.setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.valueOf(((Number) value).doubleValue()).setScale(2, RoundingMode.HALF_UP);
    }

    private String stringValue(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private LocalDate localDate(Object value) {
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        if (value instanceof Date date) {
            return date.toLocalDate();
        }
        if (value instanceof java.util.Date date) {
            return new java.sql.Date(date.getTime()).toLocalDate();
        }
        if (value instanceof LocalDateTime dateTime) {
            return dateTime.toLocalDate();
        }
        return LocalDate.parse(String.valueOf(value));
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private DashboardFilter scoped(DashboardFilter filter) {
        Integer idVendedor = accessControlService.vendedorPermitido(filter.idVendedor());
        if (idVendedor == filter.idVendedor()) {
            return filter;
        }
        return new DashboardFilter(
                filter.fechaInicio(),
                filter.fechaFin(),
                idVendedor,
                filter.idCliente(),
                filter.idProducto(),
                filter.idTipoCliente(),
                filter.idEstadoCotizacion(),
                filter.moneda()
        );
    }

    public record DashboardFilter(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            Integer idVendedor,
            Integer idCliente,
            Integer idProducto,
            Integer idTipoCliente,
            Integer idEstadoCotizacion,
            String moneda
    ) {}
}

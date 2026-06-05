package com.example.demo.service;

import com.example.demo.dto.ReporteClienteDTO;
import com.example.demo.dto.ReporteCotizacionDTO;
import com.example.demo.dto.ReporteCotizacionDetalleDTO;
import com.example.demo.dto.ReporteFiltroRequest;
import com.example.demo.dto.ReportePrecioTipoClienteDTO;
import com.example.demo.dto.ReporteProductoDTO;
import com.example.demo.dto.ReporteStockDTO;
import com.example.demo.dto.ReporteTopClienteDTO;
import com.example.demo.dto.ReporteTopProductoDTO;
import com.example.demo.dto.ReporteVendedorDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReporteServiceImpl implements ReporteService {
    @PersistenceContext
    private EntityManager entityManager;
    private final AccessControlService accessControlService;

    public ReporteServiceImpl(AccessControlService accessControlService) {
        this.accessControlService = accessControlService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteCotizacionDTO> cotizaciones(ReporteFiltroRequest filter) {
        filter = scoped(filter);
        validateDates(filter);
        Query query = cotizacionQuery("""
                SELECT c.idCotizacion, c.fechaEmision, c.fechaVencimiento, e.descEstadoCotizacion,
                       cli.razonSocial, cli.ruc,
                       CONCAT(v.nombres, ' ', v.apellidoPaterno, ' ', COALESCE(v.apellidoMaterno, '')),
                       c.moneda, c.subtotal, c.igv, c.importeTotal, c.direccionDespacho,
                       c.observaciones, c.pdfPath, COUNT(d)
                FROM Cotizacion c
                JOIN c.cliente cli
                JOIN c.vendedor v
                JOIN c.estadoCotizacion e
                LEFT JOIN c.detalles d
                """ + cotizacionWhere("c") + """
                GROUP BY c.idCotizacion, c.fechaEmision, c.fechaVencimiento, e.descEstadoCotizacion,
                         cli.razonSocial, cli.ruc, v.nombres, v.apellidoPaterno, v.apellidoMaterno,
                         c.moneda, c.subtotal, c.igv, c.importeTotal, c.direccionDespacho, c.observaciones, c.pdfPath
                ORDER BY c.fechaEmision DESC
                """, filter);
        return rows(query).stream()
                .map(row -> new ReporteCotizacionDTO(
                        intValue(row[0]),
                        cotizacionNumber(intValue(row[0])),
                        dateTime(row[1]),
                        dateTime(row[2]),
                        stringValue(row[3]),
                        stringValue(row[4]),
                        stringValue(row[5]),
                        stringValue(row[6]).trim(),
                        stringValue(row[7]),
                        money(row[8]),
                        money(row[9]),
                        money(row[10]),
                        stringValue(row[11]),
                        stringValue(row[12]),
                        stringValue(row[13]).isBlank() ? "No" : "Si",
                        longValue(row[14])
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteCotizacionDetalleDTO> cotizacionesDetalle(ReporteFiltroRequest filter) {
        filter = scoped(filter);
        validateDates(filter);
        Query query = cotizacionQuery("""
                SELECT c.idCotizacion, c.fechaEmision, e.descEstadoCotizacion,
                       cli.razonSocial, cli.ruc,
                       CONCAT(v.nombres, ' ', v.apellidoPaterno, ' ', COALESCE(v.apellidoMaterno, '')),
                       p.nombreProducto, p.unidadMedida, d.cantidad, d.precioUni,
                       d.cantidad * d.precioUni, c.moneda
                FROM CotizacionDetalle d
                JOIN d.cotizacion c
                JOIN c.cliente cli
                JOIN c.vendedor v
                JOIN c.estadoCotizacion e
                JOIN d.producto p
                """ + cotizacionWhere("c") + """
                ORDER BY c.fechaEmision DESC, c.idCotizacion DESC
                """, filter);
        return rows(query).stream()
                .map(row -> new ReporteCotizacionDetalleDTO(
                        intValue(row[0]),
                        dateTime(row[1]),
                        stringValue(row[2]),
                        stringValue(row[3]),
                        stringValue(row[4]),
                        stringValue(row[5]).trim(),
                        stringValue(row[6]),
                        stringValue(row[7]),
                        intValue(row[8]),
                        money(row[9]),
                        money(row[10]),
                        stringValue(row[11])
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteClienteDTO> clientes(ReporteFiltroRequest filter) {
        filter = scoped(filter);
        validateDates(filter);
        Query query = entityManager.createQuery("""
                SELECT c.idCliente, c.ruc, c.razonSocial, c.condicionSunat, c.estadoSunat,
                       c.direccion, c.departamento, c.provincia, c.distrito, c.ubigeo,
                       tc.descTipoCliente,
                       CONCAT(v.nombres, ' ', v.apellidoPaterno, ' ', COALESCE(v.apellidoMaterno, '')),
                       ecc.desEstadoClienteContacto, c.fechaRegistro, c.fecActualiza
                FROM Cliente c
                JOIN c.tipoCliente tc
                JOIN c.vendedorAsignado v
                JOIN c.estadoClienteContacto ecc
                WHERE (:idTipoCliente IS NULL OR tc.idTipoCliente = :idTipoCliente)
                  AND (:idVendedor IS NULL OR v.idUsuario = :idVendedor)
                  AND (:departamento IS NULL OR LOWER(c.departamento) = LOWER(:departamento))
                  AND (:provincia IS NULL OR LOWER(c.provincia) = LOWER(:provincia))
                  AND (:distrito IS NULL OR LOWER(c.distrito) = LOWER(:distrito))
                  AND (:search IS NULL OR LOWER(c.razonSocial) LIKE :search OR c.ruc LIKE :search)
                ORDER BY c.razonSocial ASC
                """);
        query.setParameter("idTipoCliente", filter.idTipoCliente());
        query.setParameter("idVendedor", filter.idVendedor());
        query.setParameter("departamento", blankToNull(filter.departamento()));
        query.setParameter("provincia", blankToNull(filter.provincia()));
        query.setParameter("distrito", blankToNull(filter.distrito()));
        query.setParameter("search", like(filter.search()));
        return rows(query).stream()
                .map(row -> new ReporteClienteDTO(
                        intValue(row[0]), stringValue(row[1]), stringValue(row[2]), stringValue(row[3]), stringValue(row[4]),
                        stringValue(row[5]), stringValue(row[6]), stringValue(row[7]), stringValue(row[8]), stringValue(row[9]),
                        stringValue(row[10]), stringValue(row[11]).trim(), stringValue(row[12]), dateTime(row[13]), dateTime(row[14])
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteProductoDTO> productos(ReporteFiltroRequest filter) {
        Query query = productosBaseQuery("""
                SELECT p.idProducto, p.nombreProducto, p.unidadMedida, p.peso, p.volumen,
                       p.stockFisico, p.stockReservado, p.stockDisponible, p.stockMinimoSeguridad,
                       p.cantMinVenta, ep.descEstadoProducto, p.fecRegistro, p.fecActualiza
                FROM Producto p
                JOIN p.estadoProducto ep
                """ + productoWhere() + """
                ORDER BY p.nombreProducto ASC
                """, filter);
        return rows(query).stream()
                .map(row -> new ReporteProductoDTO(
                        intValue(row[0]), stringValue(row[1]), stringValue(row[2]), money(row[3]), money(row[4]),
                        intValue(row[5]), intValue(row[6]), intValue(row[7]), intValue(row[8]), intValue(row[9]),
                        stringValue(row[10]), dateTime(row[11]), dateTime(row[12])
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportePrecioTipoClienteDTO> preciosTipoCliente(ReporteFiltroRequest filter) {
        Query query = entityManager.createQuery("""
                SELECT pr.idPrecio, p.nombreProducto, p.unidadMedida, tc.descTipoCliente,
                       pr.precioUnitario, pr.moneda, ep.descEstadoProducto, pr.fecRegistro, pr.fecActualiza
                FROM PrecioTipoCliente pr
                JOIN pr.producto p
                JOIN pr.tipoCliente tc
                JOIN pr.estadoProducto ep
                WHERE (:idProducto IS NULL OR p.idProducto = :idProducto)
                  AND (:idTipoCliente IS NULL OR tc.idTipoCliente = :idTipoCliente)
                  AND (:idEstadoProducto IS NULL OR ep.idEstadoProducto = :idEstadoProducto)
                  AND (:moneda IS NULL OR LOWER(pr.moneda) = LOWER(:moneda))
                ORDER BY p.nombreProducto ASC, tc.descTipoCliente ASC
                """);
        query.setParameter("idProducto", filter.idProducto());
        query.setParameter("idTipoCliente", filter.idTipoCliente());
        query.setParameter("idEstadoProducto", filter.idEstadoProducto());
        query.setParameter("moneda", blankToNull(filter.moneda()));
        return rows(query).stream()
                .map(row -> new ReportePrecioTipoClienteDTO(
                        intValue(row[0]), stringValue(row[1]), stringValue(row[2]), stringValue(row[3]),
                        money(row[4]), stringValue(row[5]), stringValue(row[6]), dateTime(row[7]), dateTime(row[8])
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteStockDTO> stock(ReporteFiltroRequest filter) {
        Query query = productosBaseQuery("""
                SELECT p.idProducto, p.nombreProducto, p.unidadMedida, p.stockFisico, p.stockReservado,
                       p.stockDisponible, p.stockMinimoSeguridad, p.cantMinVenta, ep.descEstadoProducto
                FROM Producto p
                JOIN p.estadoProducto ep
                """ + productoWhere() + """
                ORDER BY p.stockDisponible ASC, p.nombreProducto ASC
                """, filter);
        return rows(query).stream()
                .map(row -> {
                    Integer disponible = intValue(row[5]);
                    Integer minimo = intValue(row[6]);
                    return new ReporteStockDTO(
                            intValue(row[0]), stringValue(row[1]), stringValue(row[2]), intValue(row[3]), intValue(row[4]),
                            disponible, minimo, intValue(row[7]), disponible <= minimo ? "Si" : "No", stringValue(row[8])
                    );
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteVendedorDTO> vendedores(ReporteFiltroRequest filter) {
        filter = scoped(filter);
        validateDates(filter);
        Query query = cotizacionQuery("""
                SELECT v.idUsuario,
                       CONCAT(v.nombres, ' ', v.apellidoPaterno, ' ', COALESCE(v.apellidoMaterno, '')),
                       COUNT(c),
                       COALESCE(SUM(CASE WHEN LOWER(e.descEstadoCotizacion) = 'aprobada' THEN 1 ELSE 0 END), 0),
                       COALESCE(SUM(CASE WHEN LOWER(e.descEstadoCotizacion) = 'vencida' THEN 1 ELSE 0 END), 0),
                       COALESCE(SUM(CASE WHEN LOWER(e.descEstadoCotizacion) = 'rechazada' THEN 1 ELSE 0 END), 0),
                       COALESCE(SUM(c.importeTotal), 0),
                       COALESCE(SUM(CASE WHEN LOWER(e.descEstadoCotizacion) = 'aprobada' THEN c.importeTotal ELSE 0 END), 0)
                FROM Cotizacion c
                JOIN c.vendedor v
                JOIN c.estadoCotizacion e
                """ + cotizacionWhere("c") + """
                GROUP BY v.idUsuario, v.nombres, v.apellidoPaterno, v.apellidoMaterno
                ORDER BY COUNT(c) DESC
                """, filter);
        return rows(query).stream()
                .map(row -> {
                    Long total = longValue(row[2]);
                    Long aprobadas = longValue(row[3]);
                    BigDecimal tasa = total == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(aprobadas).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
                    return new ReporteVendedorDTO(
                            intValue(row[0]), stringValue(row[1]).trim(), total, aprobadas, longValue(row[4]), longValue(row[5]),
                            money(row[6]), money(row[7]), tasa
                    );
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteTopProductoDTO> productosMasCotizados(ReporteFiltroRequest filter) {
        filter = scoped(filter);
        validateDates(filter);
        Query query = cotizacionQuery("""
                SELECT p.idProducto, p.nombreProducto, p.unidadMedida,
                       COALESCE(SUM(d.cantidad), 0),
                       COALESCE(SUM(d.cantidad * d.precioUni), 0),
                       COUNT(DISTINCT c.idCotizacion)
                FROM CotizacionDetalle d
                JOIN d.producto p
                JOIN d.cotizacion c
                JOIN c.estadoCotizacion e
                """ + cotizacionWhere("c") + """
                GROUP BY p.idProducto, p.nombreProducto, p.unidadMedida
                ORDER BY SUM(d.cantidad) DESC, SUM(d.cantidad * d.precioUni) DESC
                """, filter);
        List<Object[]> data = rows(query);
        return java.util.stream.IntStream.range(0, data.size())
                .mapToObj(index -> {
                    Object[] row = data.get(index);
                    return new ReporteTopProductoDTO(index + 1, intValue(row[0]), stringValue(row[1]), stringValue(row[2]),
                            longValue(row[3]), money(row[4]), longValue(row[5]));
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteTopClienteDTO> clientesTop(ReporteFiltroRequest filter) {
        filter = scoped(filter);
        validateDates(filter);
        Query query = cotizacionQuery("""
                SELECT cli.idCliente, cli.ruc, cli.razonSocial, tc.descTipoCliente,
                       CONCAT(v.nombres, ' ', v.apellidoPaterno, ' ', COALESCE(v.apellidoMaterno, '')),
                       COUNT(c), COALESCE(SUM(c.importeTotal), 0),
                       COALESCE(SUM(CASE WHEN LOWER(e.descEstadoCotizacion) = 'aprobada' THEN c.importeTotal ELSE 0 END), 0),
                       MAX(c.fechaEmision)
                FROM Cotizacion c
                JOIN c.cliente cli
                JOIN cli.tipoCliente tc
                JOIN cli.vendedorAsignado v
                JOIN c.estadoCotizacion e
                """ + cotizacionWhere("c") + """
                GROUP BY cli.idCliente, cli.ruc, cli.razonSocial, tc.descTipoCliente, v.nombres, v.apellidoPaterno, v.apellidoMaterno
                ORDER BY SUM(c.importeTotal) DESC, COUNT(c) DESC
                """, filter);
        return rows(query).stream()
                .map(row -> new ReporteTopClienteDTO(
                        intValue(row[0]), stringValue(row[1]), stringValue(row[2]), stringValue(row[3]), stringValue(row[4]).trim(),
                        longValue(row[5]), money(row[6]), money(row[7]), dateTime(row[8])
                ))
                .toList();
    }

    private Query cotizacionQuery(String jpql, ReporteFiltroRequest filter) {
        Query query = entityManager.createQuery(jpql);
        query.setParameter("inicio", filter.fechaInicio() == null ? null : filter.fechaInicio().atStartOfDay());
        query.setParameter("fin", filter.fechaFin() == null ? null : filter.fechaFin().atTime(LocalTime.MAX));
        query.setParameter("idEstadoCotizacion", filter.idEstadoCotizacion());
        query.setParameter("idCliente", filter.idCliente());
        query.setParameter("idVendedor", filter.idVendedor());
        query.setParameter("idProducto", filter.idProducto());
        query.setParameter("idTipoCliente", filter.idTipoCliente());
        query.setParameter("moneda", blankToNull(filter.moneda()));
        query.setParameter("search", like(filter.search()));
        return query;
    }

    private String cotizacionWhere(String alias) {
        return """
                WHERE (:inicio IS NULL OR %s.fechaEmision >= :inicio)
                  AND (:fin IS NULL OR %s.fechaEmision <= :fin)
                  AND (:idEstadoCotizacion IS NULL OR %s.estadoCotizacion.idEstadoCotizacion = :idEstadoCotizacion)
                  AND (:idCliente IS NULL OR %s.cliente.idCliente = :idCliente)
                  AND (:idVendedor IS NULL OR %s.vendedor.idUsuario = :idVendedor)
                  AND (:idTipoCliente IS NULL OR %s.cliente.tipoCliente.idTipoCliente = :idTipoCliente)
                  AND (:moneda IS NULL OR LOWER(%s.moneda) = LOWER(:moneda))
                  AND (:search IS NULL OR LOWER(%s.cliente.razonSocial) LIKE :search OR %s.cliente.ruc LIKE :search)
                  AND (:idProducto IS NULL OR EXISTS (
                       SELECT 1 FROM CotizacionDetalle dx
                       WHERE dx.cotizacion = %s AND dx.producto.idProducto = :idProducto
                  ))
                """.formatted(alias, alias, alias, alias, alias, alias, alias, alias, alias, alias);
    }

    private Query productosBaseQuery(String jpql, ReporteFiltroRequest filter) {
        Query query = entityManager.createQuery(jpql);
        query.setParameter("idProducto", filter.idProducto());
        query.setParameter("idEstadoProducto", filter.idEstadoProducto());
        query.setParameter("stockBajo", filter.stockBajo());
        query.setParameter("search", like(filter.search()));
        return query;
    }

    private String productoWhere() {
        return """
                WHERE (:idProducto IS NULL OR p.idProducto = :idProducto)
                  AND (:idEstadoProducto IS NULL OR ep.idEstadoProducto = :idEstadoProducto)
                  AND (:stockBajo IS NULL OR (:stockBajo = true AND p.stockDisponible <= p.stockMinimoSeguridad) OR (:stockBajo = false AND p.stockDisponible > p.stockMinimoSeguridad))
                  AND (:search IS NULL OR LOWER(p.nombreProducto) LIKE :search)
                """;
    }

    private void validateDates(ReporteFiltroRequest filter) {
        if (filter.fechaInicio() != null && filter.fechaFin() != null && filter.fechaInicio().isAfter(filter.fechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser mayor a la fecha fin.");
        }
    }

    private ReporteFiltroRequest scoped(ReporteFiltroRequest filter) {
        Integer idVendedor = accessControlService.vendedorPermitido(filter.idVendedor());
        if (java.util.Objects.equals(idVendedor, filter.idVendedor())) {
            return filter;
        }
        return new ReporteFiltroRequest(
                filter.fechaInicio(),
                filter.fechaFin(),
                filter.idEstadoCotizacion(),
                filter.idCliente(),
                idVendedor,
                filter.idProducto(),
                filter.idTipoCliente(),
                filter.idEstadoProducto(),
                filter.moneda(),
                filter.departamento(),
                filter.provincia(),
                filter.distrito(),
                filter.stockBajo(),
                filter.search()
        );
    }

    @SuppressWarnings("unchecked")
    private List<Object[]> rows(Query query) {
        return query.getResultList();
    }

    private String cotizacionNumber(Integer id) {
        return "C001-" + String.format("%06d", id == null ? 0 : id);
    }

    private String like(String value) {
        return value == null || value.isBlank() ? null : "%" + value.trim().toLowerCase() + "%";
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String stringValue(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private Integer intValue(Object value) {
        return value == null ? 0 : ((Number) value).intValue();
    }

    private Long longValue(Object value) {
        return value == null ? 0L : ((Number) value).longValue();
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

    private LocalDateTime dateTime(Object value) {
        return value instanceof LocalDateTime dateTime ? dateTime : null;
    }
}

package com.example.demo.mapper;

import com.example.demo.dto.CotizacionDetalleResponse;
import com.example.demo.model.CotizacionDetalle;
import org.springframework.stereotype.Component;

@Component
public class CotizacionMapper {
    private final ReferenceMapper referenceMapper;

    public CotizacionMapper(ReferenceMapper referenceMapper) {
        this.referenceMapper = referenceMapper;
    }

    public Cotizacion toEntity(
            CotizacionRequest request,
            Cliente cliente,
            ZonaDespacho zona,
            Usuario vendedor
    ) {
        Cotizacion cotizacion = new Cotizacion();
        updateEntity(cotizacion, request, cliente, zona, vendedor);
        return cotizacion;
    }

    public void updateEntity(
            Cotizacion cotizacion,
            CotizacionRequest request,
            Cliente cliente,
            ZonaDespacho zona,
            Usuario vendedor
    ) {
        cotizacion.cliente = cliente;
        cotizacion.zona = zona;
        cotizacion.vendedor = vendedor;
        cotizacion.fechaEmision = request.fechaEmision();
        cotizacion.fechaVencimiento = request.fechaVencimiento();
        cotizacion.origenCotizacion = request.origenCotizacion();
        cotizacion.estadoCotizacion = request.estadoCotizacion();
        cotizacion.pdfPath = request.pdfPath();
    }

    public CotizacionResponse toResponse(Cotizacion cotizacion, List<CotizacionDetalle> detalles) {
        return new CotizacionResponse(
                cotizacion.idCotizacion,
                cotizacion.uuidPublico,
                referenceMapper.toReference(cotizacion.cliente),
                referenceMapper.toReference(cotizacion.zona),
                referenceMapper.toReference(cotizacion.vendedor),
                cotizacion.fechaEmision,
                cotizacion.fechaVencimiento,
                cotizacion.subtotal,
                cotizacion.igv,
                cotizacion.montoTotal,
                cotizacion.origenCotizacion,
                cotizacion.estadoCotizacion,
                cotizacion.pdfPath,
                detalles.stream().map(this::toDetalleResponse).toList()
        );
    }

    public CotizacionDetalleResponse toDetalleResponse(CotizacionDetalle detalle) {
        java.math.BigDecimal precio = detalle.precioUni == null ? java.math.BigDecimal.ZERO : detalle.precioUni;
        java.math.BigDecimal subtotal = precio.multiply(java.math.BigDecimal.valueOf(detalle.cantidad == null ? 0 : detalle.cantidad));
        return new CotizacionDetalleResponse(
                detalle.idDetalleCoti,
                referenceMapper.toReference(detalle.producto),
                detalle.cantidad,
                precio,
                subtotal
        );
    }

    private Integer parseEstadoCotizacion(String estadoCotizacion) {
        if (estadoCotizacion == null || estadoCotizacion.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(estadoCotizacion.trim());
        } catch (NumberFormatException ex) {
            return 1;
        }
    }
}

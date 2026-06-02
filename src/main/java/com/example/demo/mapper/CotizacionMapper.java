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
}

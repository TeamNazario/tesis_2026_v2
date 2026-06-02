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
        return new CotizacionDetalleResponse(
                detalle.idDetalle,
                referenceMapper.toReference(detalle.producto),
                detalle.cantidad,
                detalle.precioUnitarioAplicado,
                detalle.subtotalLinea
        );
    }
}

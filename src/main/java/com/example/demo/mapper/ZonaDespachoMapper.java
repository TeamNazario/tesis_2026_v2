package com.example.demo.mapper;

import com.example.demo.dto.ZonaDespachoRequest;
import com.example.demo.dto.ZonaDespachoResponse;
import com.example.demo.model.ZonaDespacho;
import org.springframework.stereotype.Component;

@Component
public class ZonaDespachoMapper {
    public ZonaDespacho toEntity(ZonaDespachoRequest request) {
        ZonaDespacho zona = new ZonaDespacho();
        updateEntity(zona, request);
        return zona;
    }

    public void updateEntity(ZonaDespacho zona, ZonaDespachoRequest request) {
        zona.departamento = request.departamento();
        zona.provincia = request.provincia();
        zona.coberturaEstandar = request.coberturaEstandar();
        zona.tiempoEntregaHoras = request.tiempoEntregaHoras();
    }

    public ZonaDespachoResponse toResponse(ZonaDespacho zona) {
        return new ZonaDespachoResponse(
                zona.idZona,
                zona.departamento,
                zona.provincia,
                zona.coberturaEstandar,
                zona.tiempoEntregaHoras
        );
    }
}

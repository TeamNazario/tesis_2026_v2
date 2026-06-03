package com.example.demo.mapper;

import com.example.demo.dto.TipoClienteComboResponse;
import com.example.demo.dto.TipoClienteResponse;
import com.example.demo.model.EstadoClienteContacto;
import com.example.demo.model.TipoCliente;
import org.springframework.stereotype.Component;

@Component
public class TipoClienteMapper {
    public TipoCliente toEntity(String descripcionNormalizada, EstadoClienteContacto estado) {
        TipoCliente entity = new TipoCliente();
        entity.descTipoCliente = descripcionNormalizada;
        entity.estadoClienteContacto = estado;
        return entity;
    }

    public void updateEntity(TipoCliente entity, String descripcionNormalizada, EstadoClienteContacto estado) {
        entity.descTipoCliente = descripcionNormalizada;
        entity.estadoClienteContacto = estado;
    }

    public TipoClienteResponse toResponse(TipoCliente entity) {
        Integer idEstado = entity.estadoClienteContacto == null
                ? entity.idEstadoClienteContacto
                : entity.estadoClienteContacto.idEstadoClienteContacto;
        String desEstado = entity.estadoClienteContacto == null ? null : entity.estadoClienteContacto.desEstadoClienteContacto;
        return new TipoClienteResponse(
                entity.idTipoCliente,
                entity.descTipoCliente,
                idEstado,
                desEstado,
                entity.usuRegistro,
                entity.fecRegistro,
                entity.usuActualiza,
                entity.fecActualiza
        );
    }

    public TipoClienteComboResponse toComboResponse(TipoCliente entity) {
        return new TipoClienteComboResponse(entity.idTipoCliente, entity.descTipoCliente);
    }
}

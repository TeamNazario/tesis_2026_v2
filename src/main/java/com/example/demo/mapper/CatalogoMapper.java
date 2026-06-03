package com.example.demo.mapper;

import com.example.demo.dto.PerfilRequest;
import com.example.demo.dto.PerfilResponse;
import com.example.demo.dto.TipoDocumentoRequest;
import com.example.demo.dto.TipoDocumentoResponse;
import com.example.demo.model.EstadoPerfil;
import com.example.demo.model.Perfil;
import com.example.demo.model.TipoDocumento;
import org.springframework.stereotype.Component;

@Component
public class CatalogoMapper {
    private final ReferenceMapper referenceMapper;

    public CatalogoMapper(ReferenceMapper referenceMapper) {
        this.referenceMapper = referenceMapper;
    }

    public TipoDocumento toEntity(TipoDocumentoRequest request) {
        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.descTipoDoc = request.descTipoDoc();
        return tipoDocumento;
    }

    public void updateEntity(TipoDocumento tipoDocumento, TipoDocumentoRequest request) {
        tipoDocumento.descTipoDoc = request.descTipoDoc();
    }

    public TipoDocumentoResponse toResponse(TipoDocumento tipoDocumento) {
        return new TipoDocumentoResponse(tipoDocumento.idTipoDoc, tipoDocumento.descTipoDoc);
    }

    public Perfil toEntity(PerfilRequest request, EstadoPerfil estadoPerfil) {
        Perfil perfil = new Perfil();
        updateEntity(perfil, request, estadoPerfil);
        return perfil;
    }

    public void updateEntity(Perfil perfil, PerfilRequest request, EstadoPerfil estadoPerfil) {
        perfil.nombrePerfil = request.nombrePerfil();
        perfil.descPerfil = request.descPerfil();
        perfil.estadoPerfil = estadoPerfil;
    }

    public PerfilResponse toResponse(Perfil perfil) {
        return new PerfilResponse(
                perfil.idPerfil,
                perfil.nombrePerfil,
                perfil.descPerfil,
                referenceMapper.toReference(perfil.estadoPerfil)
        );
    }
}

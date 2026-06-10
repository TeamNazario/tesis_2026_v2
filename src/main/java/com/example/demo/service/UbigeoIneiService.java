package com.example.demo.service;

import com.example.demo.dto.UbigeoCoberturaResponse;
import com.example.demo.dto.UbigeoIneiResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UbigeoInei;
import com.example.demo.repository.UbigeoIneiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UbigeoIneiService {

    private final UbigeoIneiRepository repository;

    public UbigeoIneiService(UbigeoIneiRepository repository) {
        this.repository = repository;
    }

    /** Busca un ubigeo por su codigo de 6 digitos */
    public UbigeoIneiResponse findByUbigeo(String ubigeo) {
        UbigeoInei entity = repository.findByUbigeo(ubigeo)
                .orElseThrow(() -> new ResourceNotFoundException("UbigeoInei", ubigeo));
        return toResponse(entity);
    }

    /** Verifica si un ubigeo tiene cobertura de despacho */
    public UbigeoCoberturaResponse findCobertura(String ubigeo) {
        UbigeoInei entity = repository.findByUbigeo(ubigeo)
                .orElseThrow(() -> new ResourceNotFoundException("UbigeoInei", ubigeo));
        return new UbigeoCoberturaResponse(
                entity.getUbigeo(),
                entity.getDistrito(),
                entity.getProvincia(),
                entity.getDepartamento(),
                entity.getFlagCobertura() != null && entity.getFlagCobertura() == 1
        );
    }

    /** Lista todos los departamentos distintos ordenados alfabeticamente */
    public List<String> findDepartamentos() {
        return repository.findAllDepartamentos();
    }

    /** Lista provincias, opcionalmente filtradas por departamento */
    public List<String> findProvincias(String departamento) {
        return repository.findProvincias(departamento);
    }

    /** Lista distritos (registros completos), con filtros opcionales por departamento y/o provincia */
    public List<UbigeoIneiResponse> findDistritos(String departamento, String provincia) {
        return repository.findDistritos(departamento, provincia)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /** Busca por codigo ubigeo (alias de findByUbigeo) */
    public UbigeoIneiResponse findByCodigo(String codigo) {
        return findByUbigeo(codigo);
    }

    // ─── Mapper ────────────────────────────────────────────────────────────────
    private UbigeoIneiResponse toResponse(UbigeoInei e) {
        return new UbigeoIneiResponse(
                e.getIdUbigeoInei(),
                e.getUbigeo(),
                e.getDistrito(),
                e.getProvincia(),
                e.getDepartamento(),
                e.getFlagCobertura() != null && e.getFlagCobertura() == 1
        );
    }
}

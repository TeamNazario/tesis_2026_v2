package com.example.demo.service;

import com.example.demo.dto.ZonaDespachoRequest;
import com.example.demo.dto.ZonaDespachoResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ZonaDespachoMapper;
import com.example.demo.model.ZonaDespacho;
import com.example.demo.repository.ZonaDespachoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ZonaDespachoService extends CrudService<ZonaDespacho, Integer> {
    private final ZonaDespachoRepository zonaRepository;
    private final ZonaDespachoMapper mapper;

    public ZonaDespachoService(ZonaDespachoRepository repository, ZonaDespachoMapper mapper) {
        super(repository, "ZonaDespacho");
        this.zonaRepository = repository;
        this.mapper = mapper;
    }

    @Override
    protected Integer parseId(String id) {
        return parseIntegerId(id);
    }

    @Transactional(readOnly = true)
    public List<ZonaDespachoResponse> findAllDto() {
        return zonaRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ZonaDespachoResponse findDtoById(Integer id) {
        return mapper.toResponse(findZona(id));
    }

    @Transactional(readOnly = true)
    public List<ZonaDespachoResponse> findByUbicacion(String departamento, String provincia) {
        if (departamento == null || departamento.isBlank()) {
            throw new IllegalArgumentException("El departamento es obligatorio para buscar zonas de despacho.");
        }

        if (provincia == null || provincia.isBlank()) {
            return zonaRepository.findByDepartamentoIgnoreCase(departamento).stream()
                    .map(mapper::toResponse)
                    .toList();
        }

        return zonaRepository.findByDepartamentoIgnoreCaseAndProvinciaIgnoreCase(departamento, provincia).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public ZonaDespachoResponse create(ZonaDespachoRequest request) {
        return mapper.toResponse(zonaRepository.save(mapper.toEntity(request)));
    }

    @Transactional
    public ZonaDespachoResponse update(Integer id, ZonaDespachoRequest request) {
        ZonaDespacho zona = findZona(id);
        mapper.updateEntity(zona, request);
        return mapper.toResponse(zonaRepository.save(zona));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!zonaRepository.existsById(id)) {
            throw new ResourceNotFoundException("ZonaDespacho", id);
        }
        zonaRepository.deleteById(id);
    }

    private ZonaDespacho findZona(Integer id) {
        return zonaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ZonaDespacho", id));
    }
}

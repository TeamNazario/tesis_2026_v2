package com.example.demo.service;

import com.example.demo.dto.EstadoRequest;
import com.example.demo.dto.EstadoResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.CatalogoMapper;
import com.example.demo.model.Estado;
import com.example.demo.repository.EstadoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstadoService extends CrudService<Estado, Integer> {
    private final EstadoRepository estadoRepository;
    private final CatalogoMapper mapper;

    public EstadoService(EstadoRepository repository, CatalogoMapper mapper) {
        super(repository, "Estado");
        this.estadoRepository = repository;
        this.mapper = mapper;
    }

    @Override
    protected Integer parseId(String id) {
        return parseIntegerId(id);
    }

    @Transactional(readOnly = true)
    public List<EstadoResponse> findAllDto() {
        return estadoRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public EstadoResponse findDtoById(Integer id) {
        return mapper.toResponse(findEstado(id));
    }

    @Transactional
    public EstadoResponse create(EstadoRequest request) {
        return mapper.toResponse(estadoRepository.save(mapper.toEntity(request)));
    }

    @Transactional
    public EstadoResponse update(Integer id, EstadoRequest request) {
        Estado estado = findEstado(id);
        mapper.updateEntity(estado, request);
        return mapper.toResponse(estadoRepository.save(estado));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!estadoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estado", id);
        }
        estadoRepository.deleteById(id);
    }

    private Estado findEstado(Integer id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado", id));
    }
}

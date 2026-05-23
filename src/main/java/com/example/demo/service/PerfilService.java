package com.example.demo.service;

import com.example.demo.dto.PerfilRequest;
import com.example.demo.dto.PerfilResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.CatalogoMapper;
import com.example.demo.model.Estado;
import com.example.demo.model.Perfil;
import com.example.demo.repository.EstadoRepository;
import com.example.demo.repository.PerfilRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerfilService extends CrudService<Perfil, Integer> {
    private final PerfilRepository perfilRepository;
    private final EstadoRepository estadoRepository;
    private final CatalogoMapper mapper;

    public PerfilService(
            PerfilRepository repository,
            EstadoRepository estadoRepository,
            CatalogoMapper mapper
    ) {
        super(repository, "Perfil");
        this.perfilRepository = repository;
        this.estadoRepository = estadoRepository;
        this.mapper = mapper;
    }

    @Override
    protected Integer parseId(String id) {
        return parseIntegerId(id);
    }

    @Transactional(readOnly = true)
    public List<PerfilResponse> findAllDto() {
        return perfilRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PerfilResponse findDtoById(Integer id) {
        return mapper.toResponse(findPerfil(id));
    }

    @Transactional
    public PerfilResponse create(PerfilRequest request) {
        Perfil perfil = mapper.toEntity(request, findEstado(request.idEstado()));
        return mapper.toResponse(perfilRepository.save(perfil));
    }

    @Transactional
    public PerfilResponse update(Integer id, PerfilRequest request) {
        Perfil perfil = findPerfil(id);
        mapper.updateEntity(perfil, request, findEstado(request.idEstado()));
        return mapper.toResponse(perfilRepository.save(perfil));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!perfilRepository.existsById(id)) {
            throw new ResourceNotFoundException("Perfil", id);
        }
        perfilRepository.deleteById(id);
    }

    private Perfil findPerfil(Integer id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil", id));
    }

    private Estado findEstado(Integer id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado", id));
    }
}

package com.example.demo.service;

import com.example.demo.dto.TipoDocumentoRequest;
import com.example.demo.dto.TipoDocumentoResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.CatalogoMapper;
import com.example.demo.model.TipoDocumento;
import com.example.demo.repository.TipoDocumentoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TipoDocumentoService extends CrudService<TipoDocumento, Integer> {
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final CatalogoMapper mapper;

    public TipoDocumentoService(TipoDocumentoRepository repository, CatalogoMapper mapper) {
        super(repository, "TipoDocumento");
        this.tipoDocumentoRepository = repository;
        this.mapper = mapper;
    }

    @Override
    protected Integer parseId(String id) {
        return parseIntegerId(id);
    }

    @Transactional(readOnly = true)
    public List<TipoDocumentoResponse> findAllDto() {
        return tipoDocumentoRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TipoDocumentoResponse findDtoById(Integer id) {
        return mapper.toResponse(findTipoDocumento(id));
    }

    @Transactional
    public TipoDocumentoResponse create(TipoDocumentoRequest request) {
        return mapper.toResponse(tipoDocumentoRepository.save(mapper.toEntity(request)));
    }

    @Transactional
    public TipoDocumentoResponse update(Integer id, TipoDocumentoRequest request) {
        TipoDocumento tipoDocumento = findTipoDocumento(id);
        mapper.updateEntity(tipoDocumento, request);
        return mapper.toResponse(tipoDocumentoRepository.save(tipoDocumento));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!tipoDocumentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("TipoDocumento", id);
        }
        tipoDocumentoRepository.deleteById(id);
    }

    private TipoDocumento findTipoDocumento(Integer id) {
        return tipoDocumentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoDocumento", id));
    }
}

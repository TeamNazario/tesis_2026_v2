package com.example.demo.service;

import com.example.demo.dto.LogInventarioRequest;
import com.example.demo.dto.LogInventarioResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.LogMapper;
import com.example.demo.model.LogInventario;
import com.example.demo.model.Producto;
import com.example.demo.repository.LogInventarioRepository;
import com.example.demo.repository.ProductoRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogInventarioService extends CrudService<LogInventario, Long> {
    private final LogInventarioRepository logRepository;
    private final ProductoRepository productoRepository;
    private final LogMapper mapper;

    public LogInventarioService(
            LogInventarioRepository repository,
            ProductoRepository productoRepository,
            LogMapper mapper
    ) {
        super(repository, "LogInventario");
        this.logRepository = repository;
        this.productoRepository = productoRepository;
        this.mapper = mapper;
    }

    @Override
    protected Long parseId(String id) {
        return parseLongId(id);
    }

    @Transactional(readOnly = true)
    public List<LogInventarioResponse> findAllDto() {
        return logRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public LogInventarioResponse findDtoById(Long id) {
        return mapper.toResponse(findLog(id));
    }

    @Transactional
    public LogInventarioResponse create(LogInventarioRequest request) {
        LogInventario log = mapper.toEntity(request, findProducto(request.idProducto()));
        if (log.fechaEvento == null) {
            log.fechaEvento = LocalDateTime.now();
        }
        return mapper.toResponse(logRepository.save(log));
    }

    @Transactional
    public LogInventarioResponse update(Long id, LogInventarioRequest request) {
        LogInventario log = findLog(id);
        mapper.updateEntity(log, request, findProducto(request.idProducto()));
        return mapper.toResponse(logRepository.save(log));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!logRepository.existsById(id)) {
            throw new ResourceNotFoundException("LogInventario", id);
        }
        logRepository.deleteById(id);
    }

    private LogInventario findLog(Long id) {
        return logRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LogInventario", id));
    }

    private Producto findProducto(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", id));
    }
}

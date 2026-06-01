package com.example.demo.service;

import com.example.demo.dto.TipoClienteComboResponse;
import com.example.demo.dto.TipoClienteCreateRequest;
import com.example.demo.dto.TipoClienteEstadoUpdateRequest;
import com.example.demo.dto.TipoClienteResponse;
import com.example.demo.dto.TipoClienteUpdateRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.TipoClienteMapper;
import com.example.demo.model.EstadoClienteContacto;
import com.example.demo.model.TipoCliente;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.EstadoClienteContactoRepository;
import com.example.demo.repository.TipoClienteRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TipoClienteServiceImpl implements TipoClienteService {
    private static final String ESTADO_ACTIVO = "Activo";
    private static final String ESTADO_INACTIVO = "Inactivo";

    private final TipoClienteRepository tipoClienteRepository;
    private final EstadoClienteContactoRepository estadoClienteContactoRepository;
    private final ClienteRepository clienteRepository;
    private final TipoClienteMapper mapper;

    public TipoClienteServiceImpl(
            TipoClienteRepository tipoClienteRepository,
            EstadoClienteContactoRepository estadoClienteContactoRepository,
            ClienteRepository clienteRepository,
            TipoClienteMapper mapper
    ) {
        this.tipoClienteRepository = tipoClienteRepository;
        this.estadoClienteContactoRepository = estadoClienteContactoRepository;
        this.clienteRepository = clienteRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TipoClienteResponse> listar(String search, Integer idEstadoClienteContacto, Pageable pageable) {
        String searchNormalizado = normalizeOptionalSearch(search);
        return tipoClienteRepository.search(searchNormalizado, idEstadoClienteContacto, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoClienteComboResponse> listarActivos() {
        return tipoClienteRepository.findByEstadoClienteContactoDesEstadoClienteContactoIgnoreCase(ESTADO_ACTIVO)
                .stream()
                .map(mapper::toComboResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoClienteResponse obtenerPorId(Integer id) {
        return mapper.toResponse(findTipoCliente(id));
    }

    @Override
    @Transactional
    public TipoClienteResponse crear(TipoClienteCreateRequest request, String actor) {
        String descripcion = normalizeRequiredDescription(request.descTipoCliente());
        validateUniqueDescriptionForCreate(descripcion);
        EstadoClienteContacto estado = findEstadoClienteContacto(request.idEstadoClienteContacto());

        TipoCliente entity = mapper.toEntity(descripcion, estado);
        entity.usuRegistro = actor;
        entity.fecRegistro = LocalDateTime.now();
        return mapper.toResponse(tipoClienteRepository.save(entity));
    }

    @Override
    @Transactional
    public TipoClienteResponse actualizar(Integer id, TipoClienteUpdateRequest request, String actor) {
        TipoCliente entity = findTipoCliente(id);
        String descripcion = normalizeRequiredDescription(request.descTipoCliente());
        validateUniqueDescriptionForUpdate(descripcion, id);
        EstadoClienteContacto estado = findEstadoClienteContacto(request.idEstadoClienteContacto());
        mapper.updateEntity(entity, descripcion, estado);
        entity.usuActualiza = actor;
        entity.fecActualiza = LocalDateTime.now();
        return mapper.toResponse(tipoClienteRepository.save(entity));
    }

    @Override
    @Transactional
    public TipoClienteResponse cambiarEstado(Integer id, TipoClienteEstadoUpdateRequest request, String actor) {
        TipoCliente entity = findTipoCliente(id);
        EstadoClienteContacto estado = findEstadoClienteContacto(request.idEstadoClienteContacto());
        entity.estadoClienteContacto = estado;
        entity.usuActualiza = actor;
        entity.fecActualiza = LocalDateTime.now();
        return mapper.toResponse(tipoClienteRepository.save(entity));
    }

    @Override
    @Transactional
    public void eliminar(Integer id, String actor) {
        TipoCliente entity = findTipoCliente(id);
        if (clienteRepository.existsByTipoCliente_IdTipoCliente(id)) {
            throw new IllegalStateException("No se puede eliminar o inactivar el tipo de cliente porque tiene clientes asociados.");
        }

        EstadoClienteContacto estadoInactivo = estadoClienteContactoRepository
                .findByDesEstadoClienteContactoIgnoreCase(ESTADO_INACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoClienteContacto", ESTADO_INACTIVO));

        entity.estadoClienteContacto = estadoInactivo;
        entity.usuActualiza = actor;
        entity.fecActualiza = LocalDateTime.now();
        tipoClienteRepository.save(entity);
    }

    private TipoCliente findTipoCliente(Integer id) {
        return tipoClienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoCliente", id));
    }

    private EstadoClienteContacto findEstadoClienteContacto(Integer id) {
        return estadoClienteContactoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoClienteContacto", id));
    }

    private void validateUniqueDescriptionForCreate(String description) {
        if (tipoClienteRepository.existsByDescTipoClienteIgnoreCase(description)) {
            throw new IllegalStateException("Ya existe un tipo de cliente con la descripcion indicada.");
        }
    }

    private void validateUniqueDescriptionForUpdate(String description, Integer idTipoCliente) {
        if (tipoClienteRepository.existsByDescTipoClienteIgnoreCaseAndIdTipoClienteNot(description, idTipoCliente)) {
            throw new IllegalStateException("Ya existe un tipo de cliente con la descripcion indicada.");
        }
    }

    private String normalizeRequiredDescription(String value) {
        String normalized = value == null ? "" : value.trim().replaceAll("\\s+", " ");
        if (normalized.isBlank()) {
            throw new IllegalArgumentException("La descripcion del tipo de cliente es obligatoria.");
        }
        return normalized;
    }

    private String normalizeOptionalSearch(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim();
        return normalized.isBlank() ? null : normalized;
    }
}

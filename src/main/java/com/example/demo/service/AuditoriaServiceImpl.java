package com.example.demo.service;

import com.example.demo.dto.AuditoriaFilterRequest;
import com.example.demo.dto.AuditoriaResponse;
import com.example.demo.mapper.AuditoriaMapper;
import com.example.demo.model.LogAuditoria;
import com.example.demo.repository.LogAuditoriaRepository;
import com.example.demo.util.AuditContextUtil;
import com.example.demo.util.AuditDiffUtil;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditoriaServiceImpl implements AuditoriaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditoriaServiceImpl.class);

    private final LogAuditoriaRepository repository;
    private final AuditoriaMapper mapper;
    private final AuditDiffUtil diffUtil;
    private final AuditContextUtil contextUtil;

    public AuditoriaServiceImpl(
            LogAuditoriaRepository repository,
            AuditoriaMapper mapper,
            AuditDiffUtil diffUtil,
            AuditContextUtil contextUtil
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.diffUtil = diffUtil;
        this.contextUtil = contextUtil;
    }

    @Override
    public void registrarCreacion(String nombreTabla, String idRegistro, Object nuevo, String modulo, String observacion) {
        registrarAccion(nombreTabla, idRegistro, "CREATE", null, nuevo, modulo, observacion);
    }

    @Override
    public void registrarActualizacion(String nombreTabla, String idRegistro, Object anterior, Object nuevo, String modulo, String observacion) {
        registrarAccion(nombreTabla, idRegistro, "UPDATE", anterior, nuevo, modulo, observacion);
    }

    @Override
    public void registrarCambioEstado(String nombreTabla, String idRegistro, Object anterior, Object nuevo, String modulo, String observacion) {
        registrarAccion(nombreTabla, idRegistro, "CHANGE_STATUS", anterior, nuevo, modulo, observacion);
    }

    @Override
    public void registrarEliminacion(String nombreTabla, String idRegistro, Object anterior, String modulo, String observacion) {
        registrarAccion(nombreTabla, idRegistro, "DELETE", anterior, null, modulo, observacion);
    }

    @Override
    @Transactional
    public void registrarAccion(String nombreTabla, String idRegistro, String accion, Object anterior, Object nuevo, String modulo, String observacion) {
        try {
            AuditContextUtil.AuditUser user = contextUtil.currentUser();
            AuditContextUtil.AuditRequest request = contextUtil.currentRequest();

            LogAuditoria log = new LogAuditoria();
            log.nombreTabla = nombreTabla;
            log.idRegistro = idRegistro;
            log.accion = accion;
            log.valoresAnterioresJson = diffUtil.convertirAJson(anterior);
            log.valoresNuevosJson = diffUtil.convertirAJson(nuevo);
            log.idUsuario = user.idUsuario();
            log.correoUsuario = user.correoUsuario();
            log.nombreUsuario = user.nombreUsuario();
            log.endpoint = request.endpoint();
            log.metodoHttp = request.metodoHttp();
            log.modulo = modulo;
            log.ipOrigen = request.ipOrigen();
            log.userAgent = request.userAgent();
            log.fechaEvento = LocalDateTime.now();
            log.observacion = observacion;
            repository.save(log);
        } catch (RuntimeException ex) {
            LOGGER.warn("No fue posible registrar auditoria {} {} {}: {}", accion, nombreTabla, idRegistro, ex.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditoriaResponse> buscarAuditoria(AuditoriaFilterRequest filter, Pageable pageable) {
        return repository.findAll(specification(filter), pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditoriaResponse> obtenerHistorialRegistro(String nombreTabla, String idRegistro) {
        return repository.findByNombreTablaAndIdRegistroOrderByFechaEventoDesc(nombreTabla, idRegistro)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    private Specification<LogAuditoria> specification(AuditoriaFilterRequest filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (notBlank(filter.nombreTabla())) {
                predicates.add(cb.equal(cb.upper(root.get("nombreTabla")), filter.nombreTabla().trim().toUpperCase()));
            }
            if (notBlank(filter.idRegistro())) {
                predicates.add(cb.equal(root.get("idRegistro"), filter.idRegistro().trim()));
            }
            if (notBlank(filter.accion())) {
                predicates.add(cb.equal(cb.upper(root.get("accion")), filter.accion().trim().toUpperCase()));
            }
            if (filter.idUsuario() != null) {
                predicates.add(cb.equal(root.get("idUsuario"), filter.idUsuario()));
            }
            if (notBlank(filter.correoUsuario())) {
                predicates.add(cb.like(cb.lower(root.get("correoUsuario")), "%" + filter.correoUsuario().trim().toLowerCase() + "%"));
            }
            if (notBlank(filter.modulo())) {
                predicates.add(cb.equal(cb.upper(root.get("modulo")), filter.modulo().trim().toUpperCase()));
            }
            if (filter.fechaInicio() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaEvento"), filter.fechaInicio().atStartOfDay()));
            }
            if (filter.fechaFin() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaEvento"), filter.fechaFin().atTime(LocalTime.MAX)));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    private boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }
}

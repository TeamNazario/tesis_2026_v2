package com.example.demo.repository;

import com.example.demo.model.LogAuditoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long>, JpaSpecificationExecutor<LogAuditoria> {
    List<LogAuditoria> findByNombreTablaAndIdRegistroOrderByFechaEventoDesc(String nombreTabla, String idRegistro);
}

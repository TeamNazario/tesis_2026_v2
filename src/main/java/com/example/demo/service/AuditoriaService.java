package com.example.demo.service;

import com.example.demo.dto.AuditoriaFilterRequest;
import com.example.demo.dto.AuditoriaResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditoriaService {
    void registrarCreacion(String nombreTabla, String idRegistro, Object nuevo, String modulo, String observacion);

    void registrarActualizacion(String nombreTabla, String idRegistro, Object anterior, Object nuevo, String modulo, String observacion);

    void registrarCambioEstado(String nombreTabla, String idRegistro, Object anterior, Object nuevo, String modulo, String observacion);

    void registrarEliminacion(String nombreTabla, String idRegistro, Object anterior, String modulo, String observacion);

    void registrarAccion(String nombreTabla, String idRegistro, String accion, Object anterior, Object nuevo, String modulo, String observacion);

    Page<AuditoriaResponse> buscarAuditoria(AuditoriaFilterRequest filter, Pageable pageable);

    List<AuditoriaResponse> obtenerHistorialRegistro(String nombreTabla, String idRegistro);
}

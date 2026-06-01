package com.example.demo.service;

import com.example.demo.dto.TipoClienteComboResponse;
import com.example.demo.dto.TipoClienteCreateRequest;
import com.example.demo.dto.TipoClienteEstadoUpdateRequest;
import com.example.demo.dto.TipoClienteResponse;
import com.example.demo.dto.TipoClienteUpdateRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoClienteService {
    Page<TipoClienteResponse> listar(String search, Integer idEstadoClienteContacto, Pageable pageable);
    List<TipoClienteComboResponse> listarActivos();
    TipoClienteResponse obtenerPorId(Integer id);
    TipoClienteResponse crear(TipoClienteCreateRequest request, String actor);
    TipoClienteResponse actualizar(Integer id, TipoClienteUpdateRequest request, String actor);
    TipoClienteResponse cambiarEstado(Integer id, TipoClienteEstadoUpdateRequest request, String actor);
    void eliminar(Integer id, String actor);
}

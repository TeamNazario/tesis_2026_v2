package com.example.demo.repository;

import com.example.demo.model.EstadoCotizacion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoCotizacionRepository extends JpaRepository<EstadoCotizacion, Integer> {
    Optional<EstadoCotizacion> findByDescEstadoCotizacionIgnoreCase(String descEstadoCotizacion);
}

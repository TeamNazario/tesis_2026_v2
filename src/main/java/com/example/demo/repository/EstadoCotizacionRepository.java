package com.example.demo.repository;

import com.example.demo.model.EstadoCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoCotizacionRepository extends JpaRepository<EstadoCotizacion, Integer> {
}

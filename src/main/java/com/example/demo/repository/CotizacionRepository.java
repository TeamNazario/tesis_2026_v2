package com.example.demo.repository;

import com.example.demo.model.Cotizacion;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Integer> {
    Optional<Cotizacion> findByUuidPublico(String uuidPublico);
}

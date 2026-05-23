package com.example.demo.repository;

import com.example.demo.model.Cotizacion;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Integer> {
    Optional<Cotizacion> findByUuidPublico(String uuidPublico);
}

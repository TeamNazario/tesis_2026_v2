package com.example.demo.repository;

import com.example.demo.model.Cotizacion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Integer> {
    @Override
    @EntityGraph(attributePaths = {"cliente", "vendedor", "estadoCotizacionRef"})
    List<Cotizacion> findAll();

    @Override
    @EntityGraph(attributePaths = {"cliente", "vendedor", "estadoCotizacionRef"})
    Optional<Cotizacion> findById(Integer id);

    Optional<Cotizacion> findByUuidPublico(String uuidPublico);
}

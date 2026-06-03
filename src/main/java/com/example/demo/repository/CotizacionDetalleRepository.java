package com.example.demo.repository;

import com.example.demo.model.CotizacionDetalle;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CotizacionDetalleRepository extends JpaRepository<CotizacionDetalle, Integer> {
    @Override
    @EntityGraph(attributePaths = {"producto", "cotizacion"})
    List<CotizacionDetalle> findAll();

    @Override
    @EntityGraph(attributePaths = {"producto", "cotizacion"})
    Optional<CotizacionDetalle> findById(Integer id);

    @EntityGraph(attributePaths = {"producto", "cotizacion"})
    List<CotizacionDetalle> findByCotizacionIdCotizacion(Integer idCotizacion);
}

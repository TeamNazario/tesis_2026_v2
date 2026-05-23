package com.example.demo.repository;

import com.example.demo.model.CotizacionDetalle;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CotizacionDetalleRepository extends JpaRepository<CotizacionDetalle, Integer> {
    List<CotizacionDetalle> findByCotizacionIdCotizacion(Integer idCotizacion);
}

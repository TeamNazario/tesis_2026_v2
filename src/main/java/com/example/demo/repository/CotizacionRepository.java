package com.example.demo.repository;

import com.example.demo.model.Cotizacion;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Integer> {
    @Override
    @EntityGraph(attributePaths = {"cliente", "vendedor", "estadoCotizacion"})
    List<Cotizacion> findAll();

    @Override
    @EntityGraph(attributePaths = {"cliente", "vendedor", "estadoCotizacion", "detalles", "detalles.producto"})
    java.util.Optional<Cotizacion> findById(Integer id);

    @EntityGraph(attributePaths = {"cliente", "vendedor", "estadoCotizacion"})
    @Query("""
            SELECT DISTINCT c
            FROM Cotizacion c
            JOIN c.cliente cliente
            JOIN c.vendedor vendedor
            JOIN c.estadoCotizacion estado
            WHERE (:search IS NULL OR LOWER(cliente.ruc) LIKE LOWER(CONCAT('%', :search, '%'))
                   OR LOWER(cliente.razonSocial) LIKE LOWER(CONCAT('%', :search, '%')))
              AND (:idCliente IS NULL OR cliente.idCliente = :idCliente)
              AND (:idVendedor IS NULL OR vendedor.idUsuario = :idVendedor)
              AND (:idEstadoCotizacion IS NULL OR estado.idEstadoCotizacion = :idEstadoCotizacion)
              AND (:fechaInicio IS NULL OR c.fechaEmision >= :fechaInicio)
              AND (:fechaFin IS NULL OR c.fechaEmision <= :fechaFin)
            ORDER BY c.fechaEmision DESC, c.idCotizacion DESC
            """)
    List<Cotizacion> buscar(
            @Param("search") String search,
            @Param("idCliente") Integer idCliente,
            @Param("idVendedor") Integer idVendedor,
            @Param("idEstadoCotizacion") Integer idEstadoCotizacion,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @EntityGraph(attributePaths = {"estadoCotizacion", "detalles", "detalles.producto"})
    @Query("""
            SELECT c
            FROM Cotizacion c
            JOIN c.estadoCotizacion estado
            WHERE estado.idEstadoCotizacion = :idEstadoGenerada
              AND c.fechaVencimiento <= :now
            """)
    List<Cotizacion> findVencidasGeneradas(
            @Param("idEstadoGenerada") Integer idEstadoGenerada,
            @Param("now") LocalDateTime now
    );
}

package com.example.demo.repository;

import com.example.demo.model.PrecioTipoCliente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PrecioTipoClienteRepository extends JpaRepository<PrecioTipoCliente, Integer> {
    @Override
    @EntityGraph(attributePaths = {"tipoCliente", "estadoProducto", "producto"})
    List<PrecioTipoCliente> findAll();

    @Override
    @EntityGraph(attributePaths = {"tipoCliente", "estadoProducto", "producto"})
    Optional<PrecioTipoCliente> findById(Integer id);

    @EntityGraph(attributePaths = {"tipoCliente", "estadoProducto", "producto"})
    List<PrecioTipoCliente> findByIdProducto(Integer idProducto);

    @Query("""
            SELECT p
            FROM PrecioTipoCliente p
            JOIN FETCH p.tipoCliente tc
            JOIN FETCH p.estadoProducto ep
            JOIN FETCH p.producto pr
            WHERE (:idProducto IS NULL OR p.idProducto = :idProducto)
              AND (:idTipoCliente IS NULL OR p.idTipoCliente = :idTipoCliente)
              AND (:idEstadoProducto IS NULL OR p.idEstadoProducto = :idEstadoProducto)
            """)
    List<PrecioTipoCliente> buscar(
            @Param("idProducto") Integer idProducto,
            @Param("idTipoCliente") Integer idTipoCliente,
            @Param("idEstadoProducto") Integer idEstadoProducto
    );

    @EntityGraph(attributePaths = {"tipoCliente", "estadoProducto", "producto"})
    @Query("""
            SELECT p
            FROM PrecioTipoCliente p
            JOIN FETCH p.tipoCliente tc
            JOIN FETCH p.estadoProducto ep
            JOIN FETCH p.producto pr
            WHERE p.idProducto = :idProducto
              AND p.idTipoCliente = :idTipoCliente
              AND LOWER(p.moneda) IN :monedas
              AND ep.idEstadoProducto = 1
            """)
    Optional<PrecioTipoCliente> findPrecioActivo(
            @Param("idProducto") Integer idProducto,
            @Param("idTipoCliente") Integer idTipoCliente,
            @Param("monedas") List<String> monedas
    );
}

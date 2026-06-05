package com.example.demo.repository;

import com.example.demo.model.Producto;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    @Override
    @EntityGraph(attributePaths = {"estadoProducto"})
    List<Producto> findAll();

    @Override
    @EntityGraph(attributePaths = {"estadoProducto"})
    Optional<Producto> findById(Integer id);

    List<Producto> findByEstadoProductoIdEstadoProducto(Integer idEstadoProducto);

    List<Producto> findByEstadoProductoDescEstadoProductoIgnoreCase(String descEstadoProducto);
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombreProducto);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {"estadoProducto"})
    @Query("SELECT p FROM Producto p WHERE p.idProducto = :id")
    Optional<Producto> findByIdForUpdate(@Param("id") Integer id);
}

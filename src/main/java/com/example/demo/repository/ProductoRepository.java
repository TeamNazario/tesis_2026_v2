package com.example.demo.repository;

import com.example.demo.model.Producto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

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
}

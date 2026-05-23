package com.example.demo.repository;

import com.example.demo.model.LogInventario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogInventarioRepository extends JpaRepository<LogInventario, Long> {
    List<LogInventario> findByProductoIdProducto(Integer idProducto);
}

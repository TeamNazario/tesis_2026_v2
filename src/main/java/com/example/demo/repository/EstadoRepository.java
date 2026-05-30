package com.example.demo.repository;

import com.example.demo.model.Estado;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {
    Optional<Estado> findFirstByDescEstadoIgnoreCase(String descEstado);
}

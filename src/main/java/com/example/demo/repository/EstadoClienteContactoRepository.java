package com.example.demo.repository;

import com.example.demo.model.EstadoClienteContacto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoClienteContactoRepository extends JpaRepository<EstadoClienteContacto, Integer> {
    Optional<EstadoClienteContacto> findByDesEstadoClienteContactoIgnoreCase(String desEstadoClienteContacto);
}

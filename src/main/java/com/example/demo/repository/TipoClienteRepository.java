package com.example.demo.repository;

import com.example.demo.model.TipoCliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoClienteRepository extends JpaRepository<TipoCliente, Integer> {
    Optional<TipoCliente> findByDescTipoCliente(String descTipoCliente);
}

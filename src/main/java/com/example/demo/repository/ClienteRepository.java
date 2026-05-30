package com.example.demo.repository;

import com.example.demo.model.Cliente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    @Override
    @EntityGraph(attributePaths = {"vendedorAsignado", "tipoCliente", "estadoClienteContacto"})
    List<Cliente> findAll();

    @Override
    @EntityGraph(attributePaths = {"vendedorAsignado", "tipoCliente", "estadoClienteContacto"})
    Optional<Cliente> findById(Integer id);

    @EntityGraph(attributePaths = {"vendedorAsignado", "tipoCliente", "estadoClienteContacto"})
    Optional<Cliente> findByRuc(String ruc);

    boolean existsByRuc(String ruc);
}

package com.example.demo.repository;

import com.example.demo.model.ContactoCliente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactoClienteRepository extends JpaRepository<ContactoCliente, Integer> {
    @Override
    @EntityGraph(attributePaths = {"cliente", "tipoDocumento", "estadoClienteContacto"})
    Optional<ContactoCliente> findById(Integer id);

    @Override
    @EntityGraph(attributePaths = {"cliente", "tipoDocumento", "estadoClienteContacto"})
    List<ContactoCliente> findAll();

    @EntityGraph(attributePaths = {"cliente", "tipoDocumento", "estadoClienteContacto"})
    List<ContactoCliente> findByClienteIdCliente(Integer idCliente);
}

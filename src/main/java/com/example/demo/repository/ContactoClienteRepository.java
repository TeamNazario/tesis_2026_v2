package com.example.demo.repository;

import com.example.demo.model.ContactoCliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactoClienteRepository extends JpaRepository<ContactoCliente, Integer> {
    List<ContactoCliente> findByClienteIdCliente(Integer idCliente);
}

package com.example.demo.repository;

import com.example.demo.model.PrecioTipoCliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrecioTipoClienteRepository extends JpaRepository<PrecioTipoCliente, Integer> {
    List<PrecioTipoCliente> findByProductoIdProducto(Integer idProducto);
}

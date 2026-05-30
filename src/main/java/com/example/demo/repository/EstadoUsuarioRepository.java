package com.example.demo.repository;

import com.example.demo.model.EstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoUsuarioRepository extends JpaRepository<EstadoUsuario, Integer> {
}

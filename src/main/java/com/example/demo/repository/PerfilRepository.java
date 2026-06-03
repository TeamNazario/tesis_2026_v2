package com.example.demo.repository;

import com.example.demo.model.Perfil;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    @EntityGraph(attributePaths = {"estadoPerfil"})
    List<Perfil> findAll();

    @EntityGraph(attributePaths = {"estadoPerfil"})
    Optional<Perfil> findById(Integer id);
}

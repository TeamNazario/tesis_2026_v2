package com.example.demo.repository;

import com.example.demo.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @EntityGraph(attributePaths = {"estadoUsuario", "perfil", "tipoDocumento"})
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    boolean existsByNroDocumento(String nroDocumento);
}

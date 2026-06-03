package com.example.demo.repository;

import com.example.demo.model.TipoCliente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoClienteRepository extends JpaRepository<TipoCliente, Integer> {
    @Override
    @EntityGraph(attributePaths = {"estadoClienteContacto"})
    Optional<TipoCliente> findById(Integer id);

    Optional<TipoCliente> findByDescTipoCliente(String descTipoCliente);
    Optional<TipoCliente> findByDescTipoClienteIgnoreCase(String descTipoCliente);
    boolean existsByDescTipoClienteIgnoreCase(String descTipoCliente);
    boolean existsByDescTipoClienteIgnoreCaseAndIdTipoClienteNot(String descTipoCliente, Integer idTipoCliente);
    List<TipoCliente> findByEstadoClienteContactoDesEstadoClienteContactoIgnoreCase(String estado);
    List<TipoCliente> findByEstadoClienteContactoIdEstadoClienteContacto(Integer idEstadoClienteContacto);

    @Query("""
            SELECT t
            FROM TipoCliente t
            WHERE (:search IS NULL OR LOWER(t.descTipoCliente) LIKE LOWER(CONCAT('%', :search, '%')))
              AND (:idEstadoClienteContacto IS NULL OR t.estadoClienteContacto.idEstadoClienteContacto = :idEstadoClienteContacto)
            """)
    Page<TipoCliente> search(
            @Param("search") String search,
            @Param("idEstadoClienteContacto") Integer idEstadoClienteContacto,
            Pageable pageable
    );
}

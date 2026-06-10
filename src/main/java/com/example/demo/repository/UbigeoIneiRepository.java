package com.example.demo.repository;

import com.example.demo.model.UbigeoInei;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UbigeoIneiRepository extends JpaRepository<UbigeoInei, Integer> {

    Optional<UbigeoInei> findByUbigeo(String ubigeo);

    @Query("SELECT DISTINCT u.departamento FROM UbigeoInei u ORDER BY u.departamento ASC")
    List<String> findAllDepartamentos();

    @Query("SELECT DISTINCT u.provincia FROM UbigeoInei u " +
           "WHERE (:departamento IS NULL OR u.departamento = :departamento) " +
           "ORDER BY u.provincia ASC")
    List<String> findProvincias(@Param("departamento") String departamento);

    @Query("SELECT u FROM UbigeoInei u " +
           "WHERE (:departamento IS NULL OR u.departamento = :departamento) " +
           "AND (:provincia IS NULL OR u.provincia = :provincia) " +
           "ORDER BY u.distrito ASC")
    List<UbigeoInei> findDistritos(@Param("departamento") String departamento,
                                    @Param("provincia") String provincia);
}

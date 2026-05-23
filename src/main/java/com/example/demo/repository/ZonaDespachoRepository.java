package com.example.demo.repository;

import com.example.demo.model.ZonaDespacho;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZonaDespachoRepository extends JpaRepository<ZonaDespacho, Integer> {
    List<ZonaDespacho> findByDepartamentoAndProvincia(String departamento, String provincia);

    List<ZonaDespacho> findByDepartamentoIgnoreCaseAndProvinciaIgnoreCase(String departamento, String provincia);

    List<ZonaDespacho> findByDepartamentoIgnoreCase(String departamento);
}

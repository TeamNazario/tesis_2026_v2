package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "zona_despacho")
public class ZonaDespacho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona")
    public Integer idZona;

    @Column(name = "departamento", nullable = false, length = 50)
    public String departamento;

    @Column(name = "provincia", nullable = false, length = 50)
    public String provincia;

    @Column(name = "cobertura_estandar", nullable = false)
    public Boolean coberturaEstandar;

    @Column(name = "tiempo_entrega_horas", nullable = false)
    public Integer tiempoEntregaHoras;
}

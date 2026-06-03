package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estado_cotizacion")
public class EstadoCotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_cotizacion")
    public Integer idEstadoCotizacion;

    @Column(name = "desc_estado_cotizacion", nullable = false, length = 50)
    public String descEstadoCotizacion;
}

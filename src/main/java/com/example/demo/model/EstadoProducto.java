package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ESTADO_PRODUCTO")
public class EstadoProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ESTADO_PRODUCTO")
    public Integer idEstadoProducto;

    @Column(name = "DESC_ESTADO_PRODUCTO", nullable = false, length = 50)
    public String descEstadoProducto;
}

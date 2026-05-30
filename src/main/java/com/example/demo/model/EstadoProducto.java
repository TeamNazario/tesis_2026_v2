package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estado_producto")
public class EstadoProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_producto")
    public Integer idEstadoProducto;

    @Column(name = "desc_estado_producto", nullable = false, length = 50)
    public String descEstadoProducto;
}

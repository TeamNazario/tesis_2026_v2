package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estado_usuario")
public class EstadoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_usuario")
    public Integer idEstadoUsuario;

    @Column(name = "des_estado", nullable = false, length = 50)
    public String desEstado;
}

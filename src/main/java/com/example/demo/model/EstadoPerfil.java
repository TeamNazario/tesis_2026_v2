package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estado_perfil")
public class EstadoPerfil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_perfil")
    public Integer idEstadoPerfil;

    @Column(name = "des_estado", nullable = false, length = 50)
    public String desEstado;
}

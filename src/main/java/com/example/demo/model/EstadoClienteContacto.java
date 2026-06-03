package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estado_cliente_contacto")
public class EstadoClienteContacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_cliente_contacto")
    public Integer idEstadoClienteContacto;

    @Column(name = "des_estado_cliente_contacto", nullable = false, length = 50)
    public String desEstadoClienteContacto;
}

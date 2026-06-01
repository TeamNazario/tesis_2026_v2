package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TIPO_CLIENTE")
public class TipoCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TIPO_CLIENTE")
    public Integer idTipoCliente;

    @Column(name = "DESC_TIPO_CLIENTE", nullable = false, length = 200)
    public String descTipoCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO_CLIENTE_CONTACTO", nullable = false)
    public EstadoClienteContacto estadoClienteContacto;
    @Column(name = "ID_ESTADO_CLIENTE_CONTACTO", insertable = false, updatable = false)
    public Integer idEstadoClienteContacto;

    @Column(name = "USU_REGISTRO", length = 50)
    public String usuRegistro;

    @Column(name = "FEC_REGISTRO")
    public LocalDateTime fecRegistro;

    @Column(name = "USU_ACTUALIZA", length = 50)
    public String usuActualiza;

    @Column(name = "FEC_ACTUALIZA")
    public LocalDateTime fecActualiza;
}

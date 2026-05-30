package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tipo_cliente")
public class TipoCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_cliente")
    public Integer idTipoCliente;

    @Column(name = "desc_tipo_cliente", nullable = false, length = 120)
    public String descTipoCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_cliente_contacto", nullable = false)
    public EstadoClienteContacto estadoClienteContacto;

    @Column(name = "usu_registro", length = 50)
    public String usuRegistro;

    @Column(name = "fec_registro")
    public LocalDateTime fecRegistro;

    @Column(name = "usu_actualiza", length = 50)
    public String usuActualiza;

    @Column(name = "fec_actualiza")
    public LocalDateTime fecActualiza;
}

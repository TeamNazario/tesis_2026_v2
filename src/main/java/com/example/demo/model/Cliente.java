package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    public Integer idCliente;

    @Column(name = "ruc", nullable = false, length = 11)
    public String ruc;

    @Column(name = "razon_social", nullable = false, length = 200)
    public String razonSocial;

    @Transient
    public String nombreComercial;

    @Column(name = "condicion_sunat", nullable = false, length = 50)
    public String condicionSunat;

    @Column(name = "estado_sunat", nullable = false, length = 50)
    public String estadoSunat;

    @Column(name = "direccion", length = 255)
    public String direccion;

    @Column(name = "departamento", length = 50)
    public String departamento;

    @Column(name = "provincia", length = 50)
    public String provincia;

    @Column(name = "distrito", length = 50)
    public String distrito;

    @Column(name = "ubigeo", length = 6)
    public String ubigeo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vendedor_asignado", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Usuario vendedorAsignado;
    @Column(name = "id_vendedor_asignado", insertable = false, updatable = false)
    public Integer idVendedorAsignado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_cliente", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public TipoCliente tipoCliente;
    @Column(name = "id_tipo_cliente", insertable = false, updatable = false)
    public Integer idTipoCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_cliente_contacto", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public EstadoClienteContacto estadoClienteContacto;
    @Column(name = "id_estado_cliente_contacto", insertable = false, updatable = false)
    public Integer idEstadoClienteContacto;

    @Column(name = "usu_registro", length = 50)
    public String usuarioRegistro;

    @Column(name = "fec_registro")
    public LocalDateTime fechaRegistro;

    @Column(name = "usu_actualiza", length = 50)
    public String usuActualiza;

    @Column(name = "fec_actualiza")
    public LocalDateTime fecActualiza;
}

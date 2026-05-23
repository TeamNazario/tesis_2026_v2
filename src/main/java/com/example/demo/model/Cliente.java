package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @Column(name = "nombre_comercial", length = 200)
    public String nombreComercial;

    @Column(name = "condicion_sunat", nullable = false, length = 50)
    public String condicionSunat;

    @Column(name = "estado_sunat", nullable = false, length = 50)
    public String estadoSunat;

    @Lob
    @Column(name = "direccion")
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
    @JoinColumn(name = "id_vendedor_asignado")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Usuario vendedorAsignado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Estado estado;

    @Column(name = "usuario_registro", length = 50)
    public String usuarioRegistro;

    @Column(name = "fecha_registro")
    public LocalDateTime fechaRegistro;
}

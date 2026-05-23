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
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    public Integer idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Perfil perfil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_doc", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public TipoDocumento tipoDocumento;

    @Column(name = "nro_documento", nullable = false, length = 20)
    public String nroDocumento;

    @Column(name = "nombres", nullable = false, length = 100)
    public String nombres;

    @Column(name = "apellido_paterno", nullable = false, length = 100)
    public String apellidoPaterno;

    @Column(name = "apellido_materno", length = 100)
    public String apellidoMaterno;

    @Column(name = "correo", nullable = false, length = 150)
    public String correo;

    @Column(name = "celular", length = 20)
    public String celular;

    @Column(name = "password_hash", nullable = false)
    public String passwordHash;

    @Column(name = "intentos_fallidos", nullable = false)
    public Integer intentosFallidos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Estado estado;

    @Column(name = "usuario_registro", length = 50)
    public String usuarioRegistro;

    @Column(name = "fecha_registro")
    public LocalDateTime fechaRegistro;

    @Column(name = "usuario_actualiza", length = 50)
    public String usuarioActualiza;

    @Column(name = "fecha_actualiza")
    public LocalDateTime fechaActualiza;
}

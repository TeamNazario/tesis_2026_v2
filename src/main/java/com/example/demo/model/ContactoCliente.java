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

@Entity
@Table(name = "contacto_cliente")
public class ContactoCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contacto")
    public Integer idContacto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_doc", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public TipoDocumento tipoDocumento;

    @Column(name = "nro_documento", nullable = false, length = 20)
    public String nroDocumento;

    @Column(name = "nombre", nullable = false, length = 100)
    public String nombre;

    @Column(name = "apellido_paterno", nullable = false, length = 100)
    public String apellidoPaterno;

    @Column(name = "apellido_materno", length = 100)
    public String apellidoMaterno;

    @Column(name = "correo", length = 150)
    public String correo;

    @Column(name = "celular", length = 20)
    public String celular;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Estado estado;
}

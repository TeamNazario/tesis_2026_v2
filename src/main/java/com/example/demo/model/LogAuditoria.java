package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "LOG_AUDITORIA")
public class LogAuditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LOG_AUDITORIA")
    public Long idLogAuditoria;

    @Column(name = "NOMBRE_TABLA", nullable = false, length = 100)
    public String nombreTabla;

    @Column(name = "ID_REGISTRO", nullable = false, length = 100)
    public String idRegistro;

    @Column(name = "ACCION", nullable = false, length = 50)
    public String accion;

    @Column(name = "CAMPO_MODIFICADO", length = 150)
    public String campoModificado;

    @Lob
    @Column(name = "VALOR_ANTERIOR")
    public String valorAnterior;

    @Lob
    @Column(name = "VALOR_NUEVO")
    public String valorNuevo;

    @Lob
    @Column(name = "VALORES_ANTERIORES_JSON")
    public String valoresAnterioresJson;

    @Lob
    @Column(name = "VALORES_NUEVOS_JSON")
    public String valoresNuevosJson;

    @Column(name = "ID_USUARIO")
    public Long idUsuario;

    @Column(name = "CORREO_USUARIO", length = 150)
    public String correoUsuario;

    @Column(name = "NOMBRE_USUARIO", length = 200)
    public String nombreUsuario;

    @Column(name = "ENDPOINT", length = 250)
    public String endpoint;

    @Column(name = "METODO_HTTP", length = 20)
    public String metodoHttp;

    @Column(name = "MODULO", length = 100)
    public String modulo;

    @Column(name = "IP_ORIGEN", length = 100)
    public String ipOrigen;

    @Column(name = "USER_AGENT", length = 500)
    public String userAgent;

    @Column(name = "FECHA_EVENTO", nullable = false)
    public LocalDateTime fechaEvento;

    @Column(name = "OBSERVACION", length = 500)
    public String observacion;
}

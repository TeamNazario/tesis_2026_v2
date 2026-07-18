package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_chat")
public class HistorialChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    public Long idHistorial;

    @Column(name = "llave_sesion", nullable = false, length = 255)
    public String llaveSesion;

    @Column(name = "proceso", nullable = false, length = 50)
    public String proceso;

    @Column(name = "fecha_inicio", nullable = false)
    public LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    public LocalDateTime fechaFin;

    @Column(name = "conversacion", columnDefinition = "LONGTEXT")
    public String conversacion;

    @Column(name = "estado", nullable = false, length = 20)
    public String estado = "ACTIVO";

    @Column(name = "usu_registro", length = 100)
    public String usuRegistro;

    @Column(name = "fec_registro", nullable = false)
    public LocalDateTime fecRegistro;
}

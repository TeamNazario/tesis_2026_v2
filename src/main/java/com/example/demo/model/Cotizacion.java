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
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cotizacion")
public class Cotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cotizacion")
    public Integer idCotizacion;

    @Transient
    public String uuidPublico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Cliente cliente;

    @Transient
    public ZonaDespacho zona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vendedor")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Usuario vendedor;

    @Column(name = "fecha_emision")
    public LocalDateTime fechaEmision;

    @Column(name = "fecha_vencimiento", nullable = false)
    public LocalDateTime fechaVencimiento;

    @Column(name = "subtotal", nullable = false, precision = 38, scale = 2)
    public BigDecimal subtotal;

    @Column(name = "igv", nullable = false, precision = 38, scale = 2)
    public BigDecimal igv;

    @Column(name = "importe_total", nullable = false, precision = 10, scale = 2)
    public BigDecimal montoTotal;

    @Column(name = "moneda", nullable = false, length = 10)
    public String moneda = "SOLES";

    @Transient
    public String origenCotizacion;

    @Column(name = "id_estado_cotizacion", nullable = false)
    public Integer idEstadoCotizacion;

    @Transient
    public String estadoCotizacion;

    @Column(name = "flag_cubierto", nullable = false)
    public Integer flagCubierto = 0;

    @Column(name = "pdf_path")
    public String pdfPath;
}

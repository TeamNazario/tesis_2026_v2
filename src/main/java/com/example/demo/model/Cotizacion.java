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
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cotizacion")
public class Cotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cotizacion")
    public Integer idCotizacion;

    @Column(name = "uuid_publico", nullable = false, length = 36)
    public String uuidPublico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_zona", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @Column(name = "monto_total", nullable = false, precision = 38, scale = 2)
    public BigDecimal montoTotal;

    @Column(name = "importe_total", precision = 18, scale = 2)
    public BigDecimal importeTotal;

    @Column(name = "moneda", length = 10)
    public String moneda;

    @Column(name = "direccion_despacho")
    public String direccionDespacho;

    @Column(name = "dep_prov_dis", length = 150)
    public String depProvDis;

    @Column(name = "flag_cubierto")
    public Boolean flagCubierto;

    @Column(name = "observaciones", length = 500)
    public String observaciones;

    @Column(name = "origen_cotizacion", nullable = false, length = 10)
    public String origenCotizacion;

    @Column(name = "estado_cotizacion", nullable = false, length = 10)
    public String estadoCotizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_cotizacion")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public EstadoCotizacion estadoCotizacionRef;

    @Column(name = "pdf_path")
    public String pdfPath;
}

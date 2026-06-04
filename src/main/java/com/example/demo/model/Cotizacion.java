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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COTIZACION")
public class Cotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COTIZACION")
    public Integer idCotizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VENDEDOR", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Usuario vendedor;

    @Column(name = "FECHA_EMISION")
    public LocalDateTime fechaEmision;

    @Column(name = "FECHA_VENCIMIENTO", nullable = false)
    public LocalDateTime fechaVencimiento;

    @Column(name = "MONEDA", nullable = false, length = 10)
    public String moneda;

    @Column(name = "SUBTOTAL", nullable = false, precision = 18, scale = 2)
    public BigDecimal subtotal;

    @Column(name = "IGV", nullable = false, precision = 18, scale = 2)
    public BigDecimal igv;

    @Column(name = "IMPORTE_TOTAL", nullable = false, precision = 18, scale = 2)
    public BigDecimal importeTotal;

    @Column(name = "DIRECCION_DESPACHO")
    public String direccionDespacho;

    @Column(name = "DEP_PROV_DIS", length = 150)
    public String depProvDis;

    @Column(name = "FLAG_CUBIERTO")
    public Integer flagCubierto;

    @Column(name = "OBSERVACIONES", length = 500)
    public String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO_COTIZACION", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public EstadoCotizacion estadoCotizacion;

    @Column(name = "PDF_PATH")
    public String pdfPath;

    @Column(name = "USU_REGISTRO", length = 50)
    public String usuRegistro;

    @Column(name = "FEC_REGISTRO")
    public LocalDateTime fecRegistro;

    @Column(name = "USU_ACTUALIZA", length = 50)
    public String usuActualiza;

    @Column(name = "FEC_ACTUALIZA")
    public LocalDateTime fecActualiza;

    @OneToMany(mappedBy = "cotizacion", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    public List<CotizacionDetalle> detalles = new ArrayList<>();
}

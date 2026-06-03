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
import jakarta.persistence.Transient;
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

    @Column(name = "uuid_publico", nullable = false, length = 36)
    public String uuidPublico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE", nullable = false)
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

    @Column(name = "monto_total", nullable = false, precision = 38, scale = 2)
    public BigDecimal montoTotal;

    @Column(name = "origen_cotizacion", nullable = false, length = 10)
    public String origenCotizacion;

    @Column(name = "estado_cotizacion", nullable = false, length = 10)
    public String estadoCotizacion;

    @Column(name = "PDF_PATH")
    public String pdfPath;

    @OneToMany(mappedBy = "cotizacion", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    public List<CotizacionDetalle> detalles = new ArrayList<>();
}

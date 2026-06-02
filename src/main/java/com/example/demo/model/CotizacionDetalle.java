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

@Entity
@Table(name = "DETALLE_COTIZACION")
public class CotizacionDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE_COTI")
    public Integer idDetalleCoti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_COTIZACION", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Cotizacion cotizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PRODUCTO", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Producto producto;

    @Column(name = "CANTIDAD", nullable = false)
    public Integer cantidad;

    @Column(name = "PRECIO_UNI", nullable = false, precision = 18, scale = 2)
    public BigDecimal precioUni;
}

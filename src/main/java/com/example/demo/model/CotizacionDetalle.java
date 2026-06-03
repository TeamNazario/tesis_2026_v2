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

@Entity
@Table(name = "cotizacion_detalle")
public class CotizacionDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    public Integer idDetalle;

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

    @Column(name = "precio_unitario_aplicado", nullable = false, precision = 38, scale = 2)
    public BigDecimal precioUnitarioAplicado;

    @Column(name = "subtotal_linea", nullable = false, precision = 38, scale = 2)
    public BigDecimal subtotalLinea;
}

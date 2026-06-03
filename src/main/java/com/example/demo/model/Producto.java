package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCTO")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODUCTO")
    public Integer idProducto;

    @Column(name = "NOMBRE_PRODUCTO", nullable = false, length = 150)
    public String nombreProducto;

    @Transient
    @Lob
    public String descripcionTecnica;

    @Column(name = "UNIDAD_MEDIDA", nullable = false, length = 20)
    public String unidadMedida;

    @Transient
    public BigDecimal precioBaseUnitario;

    @Transient
    public BigDecimal concentracionUreaAus32;

    @Column(name = "STOCK_FISICO", nullable = false)
    public Integer stockFisico;

    @Column(name = "STOCK_RESERVADO", nullable = false)
    public Integer stockReservado;

    @Column(name = "STOCK_DISPONIBLE")
    public Integer stockDisponible;

    @Column(name = "STOCK_MINIMO", nullable = false)
    public Integer stockMinimoSeguridad;

    @Column(name = "PESO", precision = 10, scale = 2)
    public BigDecimal peso;

    @Column(name = "VOLUMEN", precision = 10, scale = 2)
    public BigDecimal volumen;

    @Column(name = "CANT_MIN_VENTA")
    public Integer cantMinVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO_PRODUCTO", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public EstadoProducto estadoProducto;
    @Column(name = "ID_ESTADO_PRODUCTO", insertable = false, updatable = false)
    public Integer idEstadoProducto;

    @Column(name = "USU_REGISTRO", length = 50)
    public String usuRegistro;

    @Column(name = "FEC_REGISTRO")
    public LocalDateTime fecRegistro;

    @Column(name = "USU_ACTUALIZA", length = 50)
    public String usuActualiza;

    @Column(name = "FEC_ACTUALIZA")
    public LocalDateTime fecActualiza;
}

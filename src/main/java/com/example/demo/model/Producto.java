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
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    public Integer idProducto;

    @Column(name = "nombre_producto", nullable = false, length = 150)
    public String nombreProducto;

    @Lob
    @Column(name = "descripcion_tecnica")
    public String descripcionTecnica;

    @Column(name = "unidad_medida", nullable = false, length = 20)
    public String unidadMedida;

    @Column(name = "precio_base_unitario", nullable = false, precision = 38, scale = 2)
    public BigDecimal precioBaseUnitario;

    @Column(name = "concentración_urea_aus32", nullable = false, precision = 38, scale = 2)
    public BigDecimal concentracionUreaAus32;

    @Column(name = "stock_fisico", nullable = false)
    public Integer stockFisico;

    @Column(name = "stock_reservado", nullable = false)
    public Integer stockReservado;

    @Column(name = "stock_disponible", insertable = false, updatable = false)
    public Integer stockDisponible;

    @Column(name = "stock_minimo_seguridad", nullable = false)
    public Integer stockMinimoSeguridad;

    @Column(name = "peso", precision = 12, scale = 3)
    public BigDecimal peso;

    @Column(name = "volumen", precision = 12, scale = 3)
    public BigDecimal volumen;

    @Column(name = "cant_min_venta")
    public Integer cantMinVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Estado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_producto")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public EstadoProducto estadoProducto;

    @Column(name = "usu_registro", length = 50)
    public String usuRegistro;

    @Column(name = "fec_registro")
    public LocalDateTime fecRegistro;

    @Column(name = "usu_actualiza", length = 50)
    public String usuActualiza;

    @Column(name = "fec_actualiza")
    public LocalDateTime fecActualiza;
}

package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRECIO_TIPO_CLIENTE")
public class PrecioTipoCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRECIO")
    public Integer idPrecio;

    @Column(name = "PRECIO_UNITARIO", nullable = false, precision = 18, scale = 2)
    public BigDecimal precioUnitario;

    @Column(name = "MONEDA", nullable = false, length = 10)
    public String moneda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_CLIENTE", nullable = false)
    public TipoCliente tipoCliente;
    @Column(name = "ID_TIPO_CLIENTE", insertable = false, updatable = false)
    public Integer idTipoCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESTADO_PRODUCTO", nullable = false)
    public EstadoProducto estadoProducto;
    @Column(name = "ID_ESTADO_PRODUCTO", insertable = false, updatable = false)
    public Integer idEstadoProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PRODUCTO", nullable = false)
    public Producto producto;
    @Column(name = "ID_PRODUCTO", insertable = false, updatable = false)
    public Integer idProducto;

    @Column(name = "USU_REGISTRO", length = 50)
    public String usuRegistro;

    @Column(name = "FEC_REGISTRO")
    public LocalDateTime fecRegistro;

    @Column(name = "USU_ACTUALIZA", length = 50)
    public String usuActualiza;

    @Column(name = "FEC_ACTUALIZA")
    public LocalDateTime fecActualiza;
}

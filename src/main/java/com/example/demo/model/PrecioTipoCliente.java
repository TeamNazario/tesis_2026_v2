package com.example.demo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "precio_tipo_cliente")
public class PrecioTipoCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_precio")
    public Integer idPrecio;

    @Column(name = "precio_unitario", nullable = false, precision = 18, scale = 2)
    public BigDecimal precioUnitario;

    @Column(name = "moneda", nullable = false, length = 10)
    public String moneda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_cliente", nullable = false)
    public TipoCliente tipoCliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_producto", nullable = false)
    public EstadoProducto estadoProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    public Producto producto;

    @Column(name = "usu_registro", length = 50)
    public String usuRegistro;

    @Column(name = "fec_registro")
    public LocalDateTime fecRegistro;

    @Column(name = "usu_actualiza", length = 50)
    public String usuActualiza;

    @Column(name = "fec_actualiza")
    public LocalDateTime fecActualiza;
}

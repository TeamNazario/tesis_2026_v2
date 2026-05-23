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
import java.time.LocalDateTime;

@Entity
@Table(name = "log_inventario")
public class LogInventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log_inventario")
    public Long idLogInventario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public Producto producto;

    @Column(name = "id_usuario")
    public Integer idUsuario;

    @Column(name = "tipo_movimiento", nullable = false, length = 19)
    public String tipoMovimiento;

    @Column(name = "cantidad", nullable = false)
    public Integer cantidad;

    @Column(name = "stock_fisico_momento", nullable = false)
    public Integer stockFisicoMomento;

    @Column(name = "stock_reservado_momento", nullable = false)
    public Integer stockReservadoMomento;

    @Column(name = "fecha_evento")
    public LocalDateTime fechaEvento;
}

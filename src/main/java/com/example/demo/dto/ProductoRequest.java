package com.example.demo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductoRequest(
        @NotBlank(message = "El nombre del producto es obligatorio.")
        @Size(max = 150, message = "El nombre del producto no debe superar 150 caracteres.")
        String nombreProducto,

        String descripcionTecnica,

        @NotBlank(message = "La unidad de medida es obligatoria.")
        @Size(max = 20, message = "La unidad de medida no debe superar 20 caracteres.")
        String unidadMedida,

        @NotNull(message = "El precio base unitario es obligatorio.")
        @DecimalMin(value = "0.00", message = "El precio base unitario debe ser mayor o igual a 0.")
        BigDecimal precioBaseUnitario,

        @NotNull(message = "La concentracion de urea es obligatoria.")
        @DecimalMin(value = "0.00", message = "La concentracion de urea debe ser mayor o igual a 0.")
        BigDecimal concentracionUreaAus32,

        @NotNull(message = "El stock fisico es obligatorio.")
        @Min(value = 0, message = "El stock fisico debe ser mayor o igual a 0.")
        Integer stockFisico,

        @NotNull(message = "El stock reservado es obligatorio.")
        @Min(value = 0, message = "El stock reservado debe ser mayor o igual a 0.")
        Integer stockReservado,

        @Min(value = 0, message = "El stock disponible debe ser mayor o igual a 0.")
        Integer stockDisponible,

        @NotNull(message = "El stock minimo de seguridad es obligatorio.")
        @Min(value = 0, message = "El stock minimo de seguridad debe ser mayor o igual a 0.")
        Integer stockMinimoSeguridad,

        @NotNull(message = "El estado del producto es obligatorio.")
        Integer idEstado
) {
}

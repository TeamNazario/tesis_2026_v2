package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public record CotizacionRequest(
        @NotNull(message = "El cliente es obligatorio.")
        Integer idCliente,

        Integer idVendedor,

        LocalDateTime fechaEmision,

        @NotNull(message = "La fecha de vencimiento es obligatoria.")
        LocalDateTime fechaVencimiento,

        @NotBlank(message = "El origen de la cotizacion es obligatorio.")
        @Size(max = 10, message = "El origen de la cotizacion no debe superar 10 caracteres.")
        String origenCotizacion,

        @NotBlank(message = "El estado de la cotizacion es obligatorio.")
        @Size(max = 10, message = "El estado de la cotizacion no debe superar 10 caracteres.")
        String estadoCotizacion,

        @Size(max = 255, message = "La ruta del PDF no debe superar 255 caracteres.")
        String pdfPath,

        @NotEmpty(message = "La cotizacion debe incluir al menos un detalle.")
        List<@Valid CotizacionDetalleRequest> detalles
) {
}

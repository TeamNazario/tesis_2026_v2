package com.example.demo.dto;

import java.math.BigDecimal;

public record DashboardResumenResponse(
        Long totalCotizaciones,
        Long cotizacionesGeneradas,
        Long cotizacionesAprobadas,
        Long cotizacionesVencidas,
        Long cotizacionesRechazadas,
        Long cotizacionesAnuladas,
        BigDecimal importeTotalCotizado,
        BigDecimal importeTotalAprobado,
        BigDecimal ticketPromedio,
        BigDecimal tasaConversion,
        Long clientesAtendidos,
        Long productosCotizados,
        Long stockFisicoTotal,
        Long stockReservadoTotal,
        Long stockDisponibleTotal,
        Long productosStockBajo
) {}

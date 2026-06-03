package com.example.demo.dto;

public record CotizacionPdfResponse(
        Integer idCotizacion,
        String pdfPath,
        String fileName,
        String message
) {}

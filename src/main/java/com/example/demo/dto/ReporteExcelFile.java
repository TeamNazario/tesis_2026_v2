package com.example.demo.dto;

public record ReporteExcelFile(
        String fileName,
        byte[] content
) {}

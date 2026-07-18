package com.example.demo.dto;

public record HistorialChatFinalizarRequest(
        String estado,
        String mensajeFinal,
        String conversacion
) {}

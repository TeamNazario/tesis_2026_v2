package com.example.demo.dto;

public record AuthResponse(
        String tokenType,
        String accessToken,
        long expiresInSeconds,
        UsuarioResponse usuario
) {
}

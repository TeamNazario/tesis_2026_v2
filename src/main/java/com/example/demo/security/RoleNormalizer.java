package com.example.demo.security;

import java.text.Normalizer;

public final class RoleNormalizer {
    private RoleNormalizer() {
    }

    public static String normalize(String role) {
        if (role == null || role.isBlank()) {
            return "USER";
        }
        String normalized = Normalizer.normalize(role, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^A-Za-z0-9]+", "_")
                .replaceAll("^_+|_+$", "")
                .toUpperCase();

        if (normalized.isBlank()) {
            return "USER";
        }

        if (normalized.equals("SISTEMAS")) {
            return "SISTEMAS";
        }
        if (normalized.equals("GERENTE")) {
            return "GERENTE";
        }
        if (normalized.equals("JEFE_DE_VENTAS") || normalized.equals("JEFE_VENTAS")) {
            return "JEFE_VENTAS";
        }
        if (normalized.equals("VENDEDOR")) {
            return "VENDEDOR";
        }
        if (normalized.equals("ADMINISTRATIVO")) {
            return "ADMINISTRATIVO";
        }

        if (normalized.startsWith("ADMIN")) {
            return "ADMIN";
        }

        return normalized;
    }
}

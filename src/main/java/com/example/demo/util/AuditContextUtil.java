package com.example.demo.util;

import com.example.demo.model.Usuario;
import com.example.demo.security.AuthenticatedUser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuditContextUtil {
    private static final String SYSTEM_EMAIL = "system@biofluid.local";
    private static final String SYSTEM_NAME = "SISTEMA";

    public AuditUser currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser userDetails) {
            Usuario usuario = userDetails.getUsuario();
            if (usuario != null) {
                return new AuditUser(
                        usuario.idUsuario == null ? null : usuario.idUsuario.longValue(),
                        safe(usuario.correo, SYSTEM_EMAIL),
                        fullName(usuario)
                );
            }
        }
        return systemUser();
    }

    public AuditRequest currentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            HttpServletRequest request = attributes.getRequest();
            return new AuditRequest(
                    request.getRequestURI(),
                    request.getMethod(),
                    clientIp(request),
                    truncate(request.getHeader("User-Agent"), 500)
            );
        }
        return new AuditRequest(null, null, null, null);
    }

    public AuditUser systemUser() {
        return new AuditUser(null, SYSTEM_EMAIL, SYSTEM_NAME);
    }

    private String fullName(Usuario usuario) {
        String name = String.join(" ",
                safe(usuario.nombres, ""),
                safe(usuario.apellidoPaterno, ""),
                safe(usuario.apellidoMaterno, "")
        ).trim();
        return name.isBlank() ? SYSTEM_NAME : name;
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String safe(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }

    public record AuditUser(Long idUsuario, String correoUsuario, String nombreUsuario) {}

    public record AuditRequest(String endpoint, String metodoHttp, String ipOrigen, String userAgent) {}
}

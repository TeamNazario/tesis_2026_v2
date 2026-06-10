package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro que autentica requests usando el header X-API-Key.
 * Si la clave es valida, el request se procesa con permisos de sistema
 * sin necesitar un JWT. Util para integraciones como n8n.
 */
@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-Key";

    @Value("${app.security.api-key:}")
    private String validApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String apiKey = request.getHeader(API_KEY_HEADER);

        if (StringUtils.hasText(apiKey)
                && StringUtils.hasText(validApiKey)
                && apiKey.equals(validApiKey)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Autenticar como usuario de sistema con acceso total.
            // NOTA: No incluir ROLE_VENDEDOR para evitar conflictos con
            // AccessControlService que requiere un AuthenticatedUser como principal.
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    "n8n-api-client",
                    null,
                    List.of(
                            new SimpleGrantedAuthority("ROLE_SISTEMAS"),
                            new SimpleGrantedAuthority("ROLE_GERENTE"),
                            new SimpleGrantedAuthority("ROLE_JEFE_VENTAS"),
                            new SimpleGrantedAuthority("ROLE_ADMINISTRATIVO"),
                            new SimpleGrantedAuthority("ROLE_ADMIN")
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}

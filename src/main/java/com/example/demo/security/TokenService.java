package com.example.demo.security;

import com.example.demo.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private static final int MIN_SECRET_LENGTH = 32;
    private final SecretKey signingKey;
    private final long expirationSeconds;

    public TokenService(
            @Value("${app.security.token-secret}") String secret,
            @Value("${app.security.token-expiration-minutes}") long expirationMinutes
    ) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < MIN_SECRET_LENGTH) {
            throw new IllegalStateException(
                    "app.security.token-secret debe tener al menos "
                            + MIN_SECRET_LENGTH
                            + " bytes para firmar JWT HS256."
            );
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationSeconds = expirationMinutes * 60;
    }

    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expirationSeconds);

        Long userId = null;
        if (userDetails instanceof AuthenticatedUser authenticatedUser) {
            Usuario usuario = authenticatedUser.getUsuario();
            if (usuario != null && usuario.idUsuario != null) {
                userId = usuario.idUsuario.longValue();
            }
        }

        List<String> authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String role = authorities.stream()
                .filter(authority -> authority.startsWith("ROLE_"))
                .findFirst()
                .map(authority -> authority.substring("ROLE_".length()))
                .orElse("USER");

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .claim("authorities", authorities)
                .claim("role", role)
                .claim("userId", userId)
                .signWith(signingKey)
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isValid(String token, UserDetails userDetails) {
        try {
            Claims claims = parseClaims(token);
            String username = claims.getSubject();
            Date expiration = claims.getExpiration();
            return username != null
                    && username.equals(userDetails.getUsername())
                    && expiration != null
                    && expiration.toInstant().isAfter(Instant.now());
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

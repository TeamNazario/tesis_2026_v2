package com.example.demo.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class TokenServiceTest {
    private static final String SECRET = "test-secret-at-least-32-bytes-long!!";

    @Test
    void generateAndValidateToken() {
        TokenService tokenService = new TokenService(SECRET, 5);
        UserDetails userDetails = User.withUsername("user@biofluid.local")
                .password("ignored")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        String token = tokenService.generateToken(userDetails);

        assertThat(token.split("\\.")).hasSize(3);
        assertThat(tokenService.extractUsername(token)).isEqualTo("user@biofluid.local");
        assertThat(tokenService.isValid(token, userDetails)).isTrue();
    }

    @Test
    void invalidSignatureReturnsFalse() {
        TokenService tokenService = new TokenService(SECRET, 5);
        UserDetails userDetails = User.withUsername("user@biofluid.local")
                .password("ignored")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        String token = tokenService.generateToken(userDetails);
        String invalidToken = token.substring(0, token.length() - 2) + "zz";

        assertThat(tokenService.isValid(invalidToken, userDetails)).isFalse();
    }

    @Test
    void malformedTokenThrowsWhenExtractingUsername() {
        TokenService tokenService = new TokenService(SECRET, 5);

        assertThatThrownBy(() -> tokenService.extractUsername("invalid-token"))
                .isInstanceOf(RuntimeException.class);
    }
}

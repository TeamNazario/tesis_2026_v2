package com.example.demo.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class TokenAuthenticationFilterTest {
    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void setsAuthenticationWhenBearerTokenIsValid() throws ServletException, IOException {
        TokenService tokenService = mock(TokenService.class);
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
        TokenAuthenticationFilter filter = new TokenAuthenticationFilter(tokenService, userDetailsService);

        UserDetails userDetails = User.withUsername("user@biofluid.local")
                .password("ignored")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        when(tokenService.extractUsername("valid-token")).thenReturn("user@biofluid.local");
        when(userDetailsService.loadUserByUsername("user@biofluid.local")).thenReturn(userDetails);
        when(tokenService.isValid("valid-token", userDetails)).thenReturn(true);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer valid-token");
        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("user@biofluid.local");
    }

    @Test
    void keepsContextEmptyWhenTokenIsInvalid() throws ServletException, IOException {
        TokenService tokenService = mock(TokenService.class);
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
        TokenAuthenticationFilter filter = new TokenAuthenticationFilter(tokenService, userDetailsService);

        when(tokenService.extractUsername(any())).thenThrow(new IllegalArgumentException("invalid"));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid-token");
        filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}

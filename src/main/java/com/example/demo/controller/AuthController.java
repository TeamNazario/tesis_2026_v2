package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.security.AuthenticatedUser;
import com.example.demo.service.AuthService;
import com.example.demo.mapper.UsuarioMapper;
import com.example.demo.dto.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UsuarioMapper usuarioMapper;

    public AuthController(AuthService authService, UsuarioMapper usuarioMapper) {
        this.authService = authService;
        this.usuarioMapper = usuarioMapper;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/me")
    public UsuarioResponse me(@AuthenticationPrincipal AuthenticatedUser user) {
        return usuarioMapper.toResponse(user.getUsuario());
    }
}

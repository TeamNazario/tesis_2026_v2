package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.mapper.UsuarioMapper;
import com.example.demo.model.EstadoUsuario;
import com.example.demo.model.Usuario;
import com.example.demo.repository.EstadoUsuarioRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.AuthenticatedUser;
import com.example.demo.security.RoleNormalizer;
import com.example.demo.security.TokenService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int ENABLED_STATUS_ID = 1;
    private static final int DISABLED_STATUS_ID = 2;
    private static final int BLOCKED_STATUS_ID = 3;

    private final UsuarioRepository usuarioRepository;
    private final EstadoUsuarioRepository estadoUsuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UsuarioMapper usuarioMapper;

    public AuthService(
            UsuarioRepository usuarioRepository,
            EstadoUsuarioRepository estadoUsuarioRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            UsuarioMapper usuarioMapper
    ) {
        this.usuarioRepository = usuarioRepository;
        this.estadoUsuarioRepository = estadoUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional(noRollbackFor = BadCredentialsException.class)
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.correo())
                .orElseThrow(() -> new BadCredentialsException("El correo ingresado no existe."));
        int statusId = resolveStatusId(usuario);

        if (statusId == BLOCKED_STATUS_ID) {
            throw new BadCredentialsException("La cuenta se encuentra bloqueada.");
        }
        if (statusId == DISABLED_STATUS_ID) {
            throw new BadCredentialsException("La cuenta se encuentra inhabiliada.");
        }

        if (!passwordEncoder.matches(request.password(), usuario.passwordHash)) {
            registerFailedAttempt(usuario);
            if (resolveStatusId(usuario) == BLOCKED_STATUS_ID) {
                throw new BadCredentialsException(
                        "La cuenta se encuentra bloqueada por superar "
                                + MAX_FAILED_ATTEMPTS
                                + " intentos fallidos."
                );
            }
            throw new BadCredentialsException("Credenciales invalidas.");
        }

        usuario.intentosFallidos = 0;
        if (usuario.estadoUsuario == null || usuario.estadoUsuario.idEstadoUsuario == null) {
            usuario.estadoUsuario = findEstadoUsuario(ENABLED_STATUS_ID);
        }
        usuario.usuarioActualiza = request.correo();
        usuario.fechaActualiza = LocalDateTime.now();
        Usuario savedUsuario = usuarioRepository.save(usuario);

        AuthenticatedUser userDetails = new AuthenticatedUser(
                savedUsuario,
                List.of(new SimpleGrantedAuthority("ROLE_" + resolveRole(savedUsuario)))
        );
        String token = tokenService.generateToken(userDetails);

        return new AuthResponse(
                "Bearer",
                token,
                tokenService.getExpirationSeconds(),
                usuarioMapper.toResponse(savedUsuario)
        );
    }

    private void registerFailedAttempt(Usuario usuario) {
        int attempts = usuario.intentosFallidos == null ? 1 : usuario.intentosFallidos + 1;
        usuario.intentosFallidos = attempts;
        if (attempts >= MAX_FAILED_ATTEMPTS) {
            usuario.estadoUsuario = findEstadoUsuario(BLOCKED_STATUS_ID);
        }
        usuario.usuarioActualiza = "auth-login";
        usuario.fechaActualiza = LocalDateTime.now();
        usuarioRepository.save(usuario);
    }

    private int resolveStatusId(Usuario usuario) {
        if (usuario.estadoUsuario != null && usuario.estadoUsuario.idEstadoUsuario != null) {
            return usuario.estadoUsuario.idEstadoUsuario;
        }
        throw new IllegalStateException("El usuario no tiene un estado configurado.");
    }

    private EstadoUsuario findEstadoUsuario(int idEstadoUsuario) {
        return estadoUsuarioRepository.findById(idEstadoUsuario)
                .orElseThrow(() ->
                        new IllegalStateException("EstadoUsuario no configurado con id: " + idEstadoUsuario));
    }

    private String resolveRole(Usuario usuario) {
        if (usuario.perfil == null || usuario.perfil.nombrePerfil == null || usuario.perfil.nombrePerfil.isBlank()) {
            return "USER";
        }
        return RoleNormalizer.normalize(usuario.perfil.nombrePerfil);
    }
}

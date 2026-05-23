package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.exception.BusinessValidationException;
import com.example.demo.mapper.UsuarioMapper;
import com.example.demo.model.Estado;
import com.example.demo.model.Perfil;
import com.example.demo.model.TipoDocumento;
import com.example.demo.model.Usuario;
import com.example.demo.repository.EstadoRepository;
import com.example.demo.repository.PerfilRepository;
import com.example.demo.repository.TipoDocumentoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.AuthenticatedUser;
import com.example.demo.security.TokenService;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final int ENABLED_STATUS_ID = 1;
    private static final int BLOCKED_STATUS_ID = 2;
    private static final int DISABLED_STATUS_ID = 3;

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final EstadoRepository estadoRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UsuarioMapper usuarioMapper;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PerfilRepository perfilRepository,
            TipoDocumentoRepository tipoDocumentoRepository,
            EstadoRepository estadoRepository,
            PasswordEncoder passwordEncoder,
            TokenService tokenService,
            UsuarioMapper usuarioMapper
    ) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.estadoRepository = estadoRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.findByCorreo(request.correo()).isPresent()) {
            Map<String, List<String>> fields = new LinkedHashMap<>();
            fields.put("correo", List.of("El correo ya se encuentra registrado."));
            throw new BusinessValidationException("No se pudo registrar el usuario.", fields);
        }

        Perfil perfil = perfilRepository.findById(request.idPerfil())
                .orElseThrow(() -> new IllegalArgumentException("Perfil no encontrado con id: " + request.idPerfil()));
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(request.idTipoDoc())
                .orElseThrow(() ->
                        new IllegalArgumentException("TipoDocumento no encontrado con id: " + request.idTipoDoc()));
        Estado estado = estadoRepository.findById(request.idEstado())
                .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado con id: " + request.idEstado()));

        Usuario usuario = new Usuario();
        usuario.perfil = perfil;
        usuario.tipoDocumento = tipoDocumento;
        usuario.estado = estado;
        usuario.nroDocumento = request.nroDocumento();
        usuario.nombres = request.nombres();
        usuario.apellidoPaterno = request.apellidoPaterno();
        usuario.apellidoMaterno = request.apellidoMaterno();
        usuario.correo = request.correo();
        usuario.celular = request.celular();
        usuario.passwordHash = passwordEncoder.encode(request.password());
        usuario.intentosFallidos = 0;
        usuario.usuarioRegistro = request.correo();
        usuario.fechaRegistro = LocalDateTime.now();
        usuario.usuarioActualiza = request.correo();
        usuario.fechaActualiza = LocalDateTime.now();

        Usuario saved = usuarioRepository.save(usuario);
        AuthenticatedUser userDetails = new AuthenticatedUser(
                saved,
                List.of(new SimpleGrantedAuthority("ROLE_" + resolveRole(saved)))
        );
        String token = tokenService.generateToken(userDetails);

        return new AuthResponse(
                "Bearer",
                token,
                tokenService.getExpirationSeconds(),
                usuarioMapper.toResponse(saved)
        );
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
        if (usuario.estado == null || usuario.estado.idEstado == null) {
            usuario.estado = findEstado(ENABLED_STATUS_ID);
        }
        usuario.fechaActualiza = LocalDateTime.now();
        usuarioRepository.save(usuario);

        AuthenticatedUser userDetails = new AuthenticatedUser(
                usuario,
                List.of(new SimpleGrantedAuthority("ROLE_" + resolveRole(usuario)))
        );
        String token = tokenService.generateToken(userDetails);

        return new AuthResponse(
                "Bearer",
                token,
                tokenService.getExpirationSeconds(),
                usuarioMapper.toResponse(usuario)
        );
    }

    private void registerFailedAttempt(Usuario usuario) {
        int attempts = usuario.intentosFallidos == null ? 1 : usuario.intentosFallidos + 1;
        usuario.intentosFallidos = attempts;
        if (attempts >= MAX_FAILED_ATTEMPTS) {
            usuario.estado = findEstado(BLOCKED_STATUS_ID);
        }
        usuario.usuarioActualiza = "auth-login";
        usuario.fechaActualiza = LocalDateTime.now();
        usuarioRepository.save(usuario);
    }

    private int resolveStatusId(Usuario usuario) {
        if (usuario.estado == null || usuario.estado.idEstado == null) {
            throw new IllegalStateException("El usuario no tiene un estado configurado.");
        }
        return usuario.estado.idEstado;
    }

    private Estado findEstado(int idEstado) {
        return estadoRepository.findById(idEstado)
                .orElseThrow(() -> new IllegalStateException("Estado no configurado con id: " + idEstado));
    }

    private String resolveRole(Usuario usuario) {
        if (usuario.perfil == null || usuario.perfil.nombrePerfil == null || usuario.perfil.nombrePerfil.isBlank()) {
            return "USER";
        }
        String normalized = Normalizer.normalize(usuario.perfil.nombrePerfil, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^A-Za-z0-9]+", "_")
                .replaceAll("^_+|_+$", "")
                .toUpperCase();
        return normalized.isBlank() ? "USER" : normalized;
    }
}

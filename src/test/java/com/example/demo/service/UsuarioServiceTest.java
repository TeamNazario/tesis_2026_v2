package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.dto.UsuarioRequest;
import com.example.demo.dto.UsuarioResponse;
import com.example.demo.mapper.ReferenceMapper;
import com.example.demo.mapper.UsuarioMapper;
import com.example.demo.model.EstadoUsuario;
import com.example.demo.model.Perfil;
import com.example.demo.model.TipoDocumento;
import com.example.demo.model.Usuario;
import com.example.demo.repository.EstadoUsuarioRepository;
import com.example.demo.repository.PerfilRepository;
import com.example.demo.repository.TipoDocumentoRepository;
import com.example.demo.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Mock
    private EstadoUsuarioRepository estadoUsuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UsuarioService service;

    @BeforeEach
    void setUp() {
        service = new UsuarioService(
                usuarioRepository,
                perfilRepository,
                tipoDocumentoRepository,
                estadoUsuarioRepository,
                new UsuarioMapper(new ReferenceMapper()),
                passwordEncoder
        );
    }

    @Test
    void createAsignaUsuarioRegistroYLimpiaAuditoriaDeActualizacion() {
        Perfil perfil = perfil(5, "Administrador");
        TipoDocumento tipoDocumento = tipoDocumento(1, "DNI");
        EstadoUsuario estadoUsuario = estadoUsuario(1, "Habilitado");
        when(usuarioRepository.findByCorreo("nuevo@demo.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findAll()).thenReturn(List.of());
        when(perfilRepository.findById(5)).thenReturn(Optional.of(perfil));
        when(tipoDocumentoRepository.findById(1)).thenReturn(Optional.of(tipoDocumento));
        when(estadoUsuarioRepository.findById(1)).thenReturn(Optional.of(estadoUsuario));
        when(passwordEncoder.encode("Secreta123")).thenReturn("encoded");
        when(usuarioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0, Usuario.class));

        UsuarioRequest request = new UsuarioRequest(
                5,
                1,
                "12345678",
                "Juan",
                "Perez",
                "Lopez",
                "nuevo@demo.com",
                "999999999",
                "Secreta123",
                0,
                1,
                null,
                null
        );

        UsuarioResponse response = service.create(request, "auditor@demo.com");

        assertThat(response.usuarioRegistro()).isEqualTo("auditor@demo.com");
        assertThat(response.usuarioActualiza()).isNull();
        assertThat(response.fechaActualiza()).isNull();
    }

    @Test
    void updateAsignaUsuarioActualizaYFechaSistema() {
        Perfil perfil = perfil(5, "Administrador");
        TipoDocumento tipoDocumento = tipoDocumento(1, "DNI");
        EstadoUsuario estadoUsuario = estadoUsuario(1, "Habilitado");
        Usuario existing = new Usuario();
        existing.idUsuario = 10;
        existing.usuarioRegistro = "creador@demo.com";
        existing.fechaRegistro = java.time.LocalDateTime.of(2026, 1, 1, 10, 0);
        when(usuarioRepository.findById(10)).thenReturn(Optional.of(existing));
        when(usuarioRepository.findByCorreo("actualizado@demo.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findAll()).thenReturn(List.of());
        when(perfilRepository.findById(5)).thenReturn(Optional.of(perfil));
        when(tipoDocumentoRepository.findById(1)).thenReturn(Optional.of(tipoDocumento));
        when(estadoUsuarioRepository.findById(1)).thenReturn(Optional.of(estadoUsuario));
        when(usuarioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0, Usuario.class));

        UsuarioRequest request = new UsuarioRequest(
                5,
                1,
                "87654321",
                "Juan",
                "Perez",
                "Lopez",
                "actualizado@demo.com",
                "988888888",
                null,
                0,
                1,
                null,
                null
        );

        UsuarioResponse response = service.update(10, request, "auditor@demo.com");
        assertThat(response.usuarioRegistro()).isEqualTo("creador@demo.com");
        assertThat(response.usuarioActualiza()).isEqualTo("auditor@demo.com");
        assertThat(response.fechaActualiza()).isNotNull();
    }

    private Perfil perfil(int id, String nombre) {
        Perfil perfil = new Perfil();
        perfil.idPerfil = id;
        perfil.nombrePerfil = nombre;
        return perfil;
    }

    private TipoDocumento tipoDocumento(int id, String descripcion) {
        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.idTipoDoc = id;
        tipoDocumento.descTipoDoc = descripcion;
        return tipoDocumento;
    }

    private EstadoUsuario estadoUsuario(int id, String descripcion) {
        EstadoUsuario estadoUsuario = new EstadoUsuario();
        estadoUsuario.idEstadoUsuario = id;
        estadoUsuario.desEstado = descripcion;
        return estadoUsuario;
    }
}

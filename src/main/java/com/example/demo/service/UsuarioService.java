package com.example.demo.service;

import com.example.demo.dto.UsuarioRequest;
import com.example.demo.dto.UsuarioResponse;
import com.example.demo.exception.ResourceNotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService extends CrudService<Usuario, Integer> {
    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final EstadoUsuarioRepository estadoUsuarioRepository;
    private final UsuarioMapper mapper;

    public UsuarioService(
            UsuarioRepository repository,
            PerfilRepository perfilRepository,
            TipoDocumentoRepository tipoDocumentoRepository,
            EstadoUsuarioRepository estadoUsuarioRepository,
            UsuarioMapper mapper
    ) {
        super(repository, "Usuario");
        this.usuarioRepository = repository;
        this.perfilRepository = perfilRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.estadoUsuarioRepository = estadoUsuarioRepository;
        this.mapper = mapper;
    }

    @Override
    protected Integer parseId(String id) {
        return parseIntegerId(id);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> findAllDto() {
        return usuarioRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse findDtoById(Integer id) {
        return mapper.toResponse(findUsuario(id));
    }

    @Transactional
    public UsuarioResponse update(Integer id, UsuarioRequest request) {
        Usuario usuario = findUsuario(id);
        mapper.updateEntity(
                usuario,
                request,
                findPerfil(request.idPerfil()),
                findTipoDocumento(request.idTipoDoc()),
                findEstadoUsuario(request.idEstado())
        );
        return mapper.toResponse(usuarioRepository.save(usuario));
    }

    private Usuario findUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }

    private Perfil findPerfil(Integer id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil", id));
    }

    private TipoDocumento findTipoDocumento(Integer id) {
        return tipoDocumentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoDocumento", id));
    }

    private EstadoUsuario findEstadoUsuario(Integer id) {
        return estadoUsuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoUsuario", id));
    }
}

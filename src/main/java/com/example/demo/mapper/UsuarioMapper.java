package com.example.demo.mapper;

import com.example.demo.dto.UsuarioRequest;
import com.example.demo.dto.UsuarioResponse;
import com.example.demo.model.Estado;
import com.example.demo.model.Perfil;
import com.example.demo.model.TipoDocumento;
import com.example.demo.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    private final ReferenceMapper referenceMapper;

    public UsuarioMapper(ReferenceMapper referenceMapper) {
        this.referenceMapper = referenceMapper;
    }

    public Usuario toEntity(
            UsuarioRequest request,
            Perfil perfil,
            TipoDocumento tipoDocumento,
            Estado estado
    ) {
        Usuario usuario = new Usuario();
        apply(usuario, request, perfil, tipoDocumento, estado);
        return usuario;
    }

    public void updateEntity(
            Usuario usuario,
            UsuarioRequest request,
            Perfil perfil,
            TipoDocumento tipoDocumento,
            Estado estado
    ) {
        apply(usuario, request, perfil, tipoDocumento, estado);
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.idUsuario,
                referenceMapper.toReference(usuario.perfil),
                referenceMapper.toReference(usuario.tipoDocumento),
                usuario.nroDocumento,
                usuario.nombres,
                usuario.apellidoPaterno,
                usuario.apellidoMaterno,
                usuario.correo,
                usuario.celular,
                usuario.intentosFallidos,
                referenceMapper.toReference(usuario.estado),
                usuario.usuarioRegistro,
                usuario.fechaRegistro,
                usuario.usuarioActualiza,
                usuario.fechaActualiza
        );
    }

    private void apply(
            Usuario usuario,
            UsuarioRequest request,
            Perfil perfil,
            TipoDocumento tipoDocumento,
            Estado estado
    ) {
        usuario.perfil = perfil;
        usuario.tipoDocumento = tipoDocumento;
        usuario.nroDocumento = request.nroDocumento();
        usuario.nombres = request.nombres();
        usuario.apellidoPaterno = request.apellidoPaterno();
        usuario.apellidoMaterno = request.apellidoMaterno();
        usuario.correo = request.correo();
        usuario.celular = request.celular();
        usuario.passwordHash = request.passwordHash();
        usuario.intentosFallidos = request.intentosFallidos();
        usuario.estado = estado;
        usuario.usuarioRegistro = request.usuarioRegistro();
        usuario.usuarioActualiza = request.usuarioActualiza();
    }
}

package com.example.demo.mapper;

import com.example.demo.dto.ReferenceResponse;
import com.example.demo.model.Cliente;
import com.example.demo.model.Estado;
import com.example.demo.model.EstadoClienteContacto;
import com.example.demo.model.EstadoPerfil;
import com.example.demo.model.EstadoUsuario;
import com.example.demo.model.Perfil;
import com.example.demo.model.Producto;
import com.example.demo.model.TipoDocumento;
import com.example.demo.model.Usuario;
import com.example.demo.model.ZonaDespacho;
import org.springframework.stereotype.Component;

@Component
public class ReferenceMapper {
    public ReferenceResponse toReference(Estado estado) {
        return estado == null ? null : new ReferenceResponse(estado.idEstado, estado.descEstado);
    }

    public ReferenceResponse toReference(EstadoUsuario estadoUsuario) {
        return estadoUsuario == null
                ? null
                : new ReferenceResponse(estadoUsuario.idEstadoUsuario, estadoUsuario.desEstado);
    }

    public ReferenceResponse toReference(EstadoPerfil estadoPerfil) {
        return estadoPerfil == null ? null : new ReferenceResponse(estadoPerfil.idEstadoPerfil, estadoPerfil.desEstado);
    }

    public ReferenceResponse toReference(EstadoClienteContacto estadoClienteContacto) {
        return estadoClienteContacto == null
                ? null
                : new ReferenceResponse(
                        estadoClienteContacto.idEstadoClienteContacto,
                        estadoClienteContacto.desEstadoClienteContacto
                );
    }

    public ReferenceResponse toReference(TipoDocumento tipoDocumento) {
        return tipoDocumento == null
                ? null
                : new ReferenceResponse(tipoDocumento.idTipoDoc, tipoDocumento.descTipoDoc);
    }

    public ReferenceResponse toReference(Perfil perfil) {
        return perfil == null ? null : new ReferenceResponse(perfil.idPerfil, perfil.nombrePerfil);
    }

    public ReferenceResponse toReference(Usuario usuario) {
        return usuario == null
                ? null
                : new ReferenceResponse(usuario.idUsuario, usuario.nombres + " " + usuario.apellidoPaterno);
    }

    public ReferenceResponse toReference(Cliente cliente) {
        return cliente == null ? null : new ReferenceResponse(cliente.idCliente, cliente.razonSocial);
    }

    public ReferenceResponse toReference(Producto producto) {
        return producto == null ? null : new ReferenceResponse(producto.idProducto, producto.nombreProducto);
    }

    public ReferenceResponse toReference(ZonaDespacho zona) {
        return zona == null
                ? null
                : new ReferenceResponse(zona.idZona, zona.departamento + " - " + zona.provincia);
    }
}

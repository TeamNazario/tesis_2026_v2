package com.example.demo.security;

import com.example.demo.model.Usuario;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticatedUser implements UserDetails {
    private final Usuario usuario;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthenticatedUser(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
        this.usuario = usuario;
        this.authorities = authorities;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return usuario.passwordHash;
    }

    @Override
    public String getUsername() {
        return usuario.correo;
    }

    @Override
    public boolean isAccountNonLocked() {
        return usuario.estado == null || usuario.estado.idEstado == null || usuario.estado.idEstado != 2;
    }

    @Override
    public boolean isEnabled() {
        return usuario.estado == null || usuario.estado.idEstado == null || usuario.estado.idEstado != 3;
    }
}

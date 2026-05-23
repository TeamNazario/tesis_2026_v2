package com.example.demo.mapper;

import com.example.demo.dto.ClienteRequest;
import com.example.demo.dto.ClienteResponse;
import com.example.demo.model.Cliente;
import com.example.demo.model.Estado;
import com.example.demo.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {
    private final ReferenceMapper referenceMapper;

    public ClienteMapper(ReferenceMapper referenceMapper) {
        this.referenceMapper = referenceMapper;
    }

    public Cliente toEntity(ClienteRequest request, Estado estado, Usuario vendedorAsignado) {
        Cliente cliente = new Cliente();
        apply(cliente, request, estado, vendedorAsignado);
        return cliente;
    }

    public void updateEntity(Cliente cliente, ClienteRequest request, Estado estado, Usuario vendedorAsignado) {
        apply(cliente, request, estado, vendedorAsignado);
    }

    public ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.idCliente,
                cliente.ruc,
                cliente.razonSocial,
                cliente.nombreComercial,
                cliente.condicionSunat,
                cliente.estadoSunat,
                cliente.direccion,
                cliente.departamento,
                cliente.provincia,
                cliente.distrito,
                cliente.ubigeo,
                referenceMapper.toReference(cliente.vendedorAsignado),
                referenceMapper.toReference(cliente.estado),
                cliente.usuarioRegistro,
                cliente.fechaRegistro
        );
    }

    private void apply(Cliente cliente, ClienteRequest request, Estado estado, Usuario vendedorAsignado) {
        cliente.ruc = request.ruc();
        cliente.razonSocial = request.razonSocial();
        cliente.nombreComercial = request.nombreComercial();
        cliente.condicionSunat = request.condicionSunat();
        cliente.estadoSunat = request.estadoSunat();
        cliente.direccion = request.direccion();
        cliente.departamento = request.departamento();
        cliente.provincia = request.provincia();
        cliente.distrito = request.distrito();
        cliente.ubigeo = request.ubigeo();
        cliente.vendedorAsignado = vendedorAsignado;
        cliente.estado = estado;
        cliente.usuarioRegistro = request.usuarioRegistro();
    }
}

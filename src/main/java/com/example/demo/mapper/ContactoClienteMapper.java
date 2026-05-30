package com.example.demo.mapper;

import com.example.demo.dto.ContactoClienteRequest;
import com.example.demo.dto.ContactoClienteResponse;
import com.example.demo.model.Cliente;
import com.example.demo.model.ContactoCliente;
import com.example.demo.model.EstadoClienteContacto;
import com.example.demo.model.TipoDocumento;
import org.springframework.stereotype.Component;

@Component
public class ContactoClienteMapper {
    private final ReferenceMapper referenceMapper;

    public ContactoClienteMapper(ReferenceMapper referenceMapper) {
        this.referenceMapper = referenceMapper;
    }

    public ContactoCliente toEntity(
            ContactoClienteRequest request,
            Cliente cliente,
            TipoDocumento tipoDocumento,
            EstadoClienteContacto estadoClienteContacto
    ) {
        ContactoCliente contacto = new ContactoCliente();
        updateEntity(contacto, request, cliente, tipoDocumento, estadoClienteContacto);
        return contacto;
    }

    public void updateEntity(
            ContactoCliente contacto,
            ContactoClienteRequest request,
            Cliente cliente,
            TipoDocumento tipoDocumento,
            EstadoClienteContacto estadoClienteContacto
    ) {
        contacto.cliente = cliente;
        contacto.tipoDocumento = tipoDocumento;
        contacto.nroDocumento = request.nroDocumento();
        contacto.nombre = request.nombre();
        contacto.apellidoPaterno = request.apellidoPaterno();
        contacto.apellidoMaterno = request.apellidoMaterno();
        contacto.correo = request.correo();
        contacto.celular = request.celular();
        contacto.estadoClienteContacto = estadoClienteContacto;
    }

    public ContactoClienteResponse toResponse(ContactoCliente contacto) {
        return new ContactoClienteResponse(
                contacto.idContacto,
                referenceMapper.toReference(contacto.cliente),
                referenceMapper.toReference(contacto.tipoDocumento),
                contacto.nroDocumento,
                contacto.nombre,
                contacto.apellidoPaterno,
                contacto.apellidoMaterno,
                contacto.correo,
                contacto.celular,
                referenceMapper.toReference(contacto.estadoClienteContacto)
        );
    }
}

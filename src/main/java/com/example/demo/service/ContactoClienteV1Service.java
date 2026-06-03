package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactoClienteV1Service {
    private final ContactoClienteRepository contactoRepository;
    private final ClienteRepository clienteRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final EstadoClienteContactoRepository estadoClienteContactoRepository;

    public ContactoClienteV1Service(ContactoClienteRepository contactoRepository, ClienteRepository clienteRepository, TipoDocumentoRepository tipoDocumentoRepository, EstadoClienteContactoRepository estadoClienteContactoRepository) {
        this.contactoRepository = contactoRepository;
        this.clienteRepository = clienteRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.estadoClienteContactoRepository = estadoClienteContactoRepository;
    }

    @Transactional(readOnly = true)
    public List<ContactoClienteV1Response> listByCliente(Integer clienteId) {
        return contactoRepository.findByClienteIdCliente(clienteId).stream().map(this::map).toList();
    }

    @Transactional
    public ContactoClienteV1Response create(Integer clienteId, ContactoClienteCreateRequest request, String actor) {
        ContactoCliente c = new ContactoCliente();
        c.cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException("Cliente", clienteId));
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(request.idTipoDoc()).orElseThrow(() -> new ResourceNotFoundException("TipoDocumento", request.idTipoDoc()));
        c.tipoDocumento = tipoDocumento;
        c.nroDocumento = request.nroDocumento();
        c.nombre = request.nombre();
        c.apellidoPaterno = request.apellidoPaterno();
        c.apellidoMaterno = request.apellidoMaterno();
        c.correo = request.correo();
        c.celular = request.celular();
        EstadoClienteContacto estadoClienteContacto = estadoClienteContactoRepository.findById(request.idEstadoClienteContacto()).orElseThrow(() -> new ResourceNotFoundException("EstadoClienteContacto", request.idEstadoClienteContacto()));
        c.estadoClienteContacto = estadoClienteContacto;
        c.usuRegistro = actor;
        c.fecRegistro = LocalDateTime.now();
        c.usuActualiza = null;
        c.fecActualiza = null;
        ContactoCliente saved = contactoRepository.save(c);
        return mapWithFallback(saved, request.idTipoDoc(), tipoDocumento.descTipoDoc, request.idEstadoClienteContacto(), estadoClienteContacto.desEstadoClienteContacto);
    
    }

    @Transactional
    public ContactoClienteV1Response update(
            Integer clienteId,
            Integer contactoId,
            ContactoClienteUpdateRequest request,
            String actor
    ) {
        ContactoCliente c = contactoRepository.findById(contactoId).orElseThrow(() -> new ResourceNotFoundException("ContactoCliente", contactoId));
        if (!c.cliente.idCliente.equals(clienteId)) throw new ResourceNotFoundException("ContactoCliente", contactoId);
        Integer resolvedTipoDocId = c.tipoDocumento != null ? c.tipoDocumento.idTipoDoc : c.idTipoDoc;
        String resolvedTipoDoc = c.tipoDocumento != null ? c.tipoDocumento.descTipoDoc : resolveTipoDocumento(resolvedTipoDocId);
        if (request.idTipoDoc() != null) {
            TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(request.idTipoDoc()).orElseThrow(() -> new ResourceNotFoundException("TipoDocumento", request.idTipoDoc()));
            c.tipoDocumento = tipoDocumento;
            resolvedTipoDocId = tipoDocumento.idTipoDoc;
            resolvedTipoDoc = tipoDocumento.descTipoDoc;
        }
        if (request.nroDocumento() != null) c.nroDocumento = request.nroDocumento();
        if (request.nombre() != null) c.nombre = request.nombre();
        if (request.apellidoPaterno() != null) c.apellidoPaterno = request.apellidoPaterno();
        if (request.apellidoMaterno() != null) c.apellidoMaterno = request.apellidoMaterno();
        if (request.correo() != null) c.correo = request.correo();
        if (request.celular() != null) c.celular = request.celular();
        Integer resolvedEstadoId = c.estadoClienteContacto != null ? c.estadoClienteContacto.idEstadoClienteContacto : c.idEstadoClienteContacto;
        String resolvedEstado = c.estadoClienteContacto != null ? c.estadoClienteContacto.desEstadoClienteContacto : resolveEstadoClienteContacto(resolvedEstadoId);
        if (request.idEstadoClienteContacto() != null) {
            EstadoClienteContacto estadoClienteContacto = estadoClienteContactoRepository.findById(request.idEstadoClienteContacto()).orElseThrow(() -> new ResourceNotFoundException("EstadoClienteContacto", request.idEstadoClienteContacto()));
            c.estadoClienteContacto = estadoClienteContacto;
            resolvedEstadoId = estadoClienteContacto.idEstadoClienteContacto;
            resolvedEstado = estadoClienteContacto.desEstadoClienteContacto;
        }
        c.usuActualiza = actor;
        c.fecActualiza = LocalDateTime.now();
        ContactoCliente saved = contactoRepository.save(c);
        return mapWithFallback(saved, resolvedTipoDocId, resolvedTipoDoc, resolvedEstadoId, resolvedEstado);
    }

    @Transactional
    public ContactoClienteV1Response patchEstado(Integer clienteId, Integer contactoId, Integer estadoId, String actor) {
        ContactoCliente c = contactoRepository.findById(contactoId).orElseThrow(() -> new ResourceNotFoundException("ContactoCliente", contactoId));
        if (!c.cliente.idCliente.equals(clienteId)) throw new ResourceNotFoundException("ContactoCliente", contactoId);
        c.estadoClienteContacto = estadoClienteContactoRepository.findById(estadoId).orElseThrow(() -> new ResourceNotFoundException("EstadoClienteContacto", estadoId));
        c.usuActualiza = actor;
        c.fecActualiza = LocalDateTime.now();
        return saveAndMap(c);
    }

    private ContactoClienteV1Response saveAndMap(ContactoCliente contacto) {
        ContactoCliente saved = contactoRepository.save(contacto);
        ContactoCliente hydrated = contactoRepository.findById(saved.idContacto).orElse(saved);
        return map(hydrated);
    }

    private ContactoClienteV1Response mapWithFallback(
            ContactoCliente contacto,
            Integer fallbackTipoDocId,
            String fallbackTipoDoc,
            Integer fallbackEstadoId,
            String fallbackEstado
    ) {
        ContactoClienteV1Response mapped = saveAndMap(contacto);
        Integer idTipoDoc = mapped.idTipoDoc() != null ? mapped.idTipoDoc() : fallbackTipoDocId;
        String tipoDocumento = mapped.tipoDocumento() != null ? mapped.tipoDocumento() : fallbackTipoDoc;
        Integer idEstadoClienteContacto = mapped.idEstadoClienteContacto() != null ? mapped.idEstadoClienteContacto() : fallbackEstadoId;
        String estadoClienteContacto = mapped.estadoClienteContacto() != null ? mapped.estadoClienteContacto() : fallbackEstado;
        return new ContactoClienteV1Response(
                mapped.idContacto(),
                mapped.idCliente(),
                idTipoDoc,
                tipoDocumento,
                mapped.nroDocumento(),
                mapped.nombre(),
                mapped.apellidoPaterno(),
                mapped.apellidoMaterno(),
                mapped.correo(),
                mapped.celular(),
                idEstadoClienteContacto,
                estadoClienteContacto
        );
    }

    private ContactoClienteV1Response map(ContactoCliente c) {
        Integer idTipoDoc = c.tipoDocumento != null ? c.tipoDocumento.idTipoDoc : c.idTipoDoc;
        String tipoDocumento = c.tipoDocumento != null ? c.tipoDocumento.descTipoDoc : resolveTipoDocumento(idTipoDoc);
        Integer idEstadoClienteContacto = c.estadoClienteContacto != null
                ? c.estadoClienteContacto.idEstadoClienteContacto
                : c.idEstadoClienteContacto;
        String estadoClienteContacto = c.estadoClienteContacto != null
                ? c.estadoClienteContacto.desEstadoClienteContacto
                : resolveEstadoClienteContacto(idEstadoClienteContacto);

        return new ContactoClienteV1Response(
                c.idContacto,
                c.cliente.idCliente,
                idTipoDoc,
                tipoDocumento,
                c.nroDocumento,
                c.nombre,
                c.apellidoPaterno,
                c.apellidoMaterno,
                c.correo,
                c.celular,
                idEstadoClienteContacto,
                estadoClienteContacto
        );
    }

    private String resolveTipoDocumento(Integer idTipoDoc) {
        if (idTipoDoc == null) {
            return null;
        }
        return tipoDocumentoRepository.findById(idTipoDoc).map(t -> t.descTipoDoc).orElse(null);
    }

    private String resolveEstadoClienteContacto(Integer idEstadoClienteContacto) {
        if (idEstadoClienteContacto == null) {
            return null;
        }
        return estadoClienteContactoRepository.findById(idEstadoClienteContacto)
                .map(e -> e.desEstadoClienteContacto)
                .orElse(null);
    }
}

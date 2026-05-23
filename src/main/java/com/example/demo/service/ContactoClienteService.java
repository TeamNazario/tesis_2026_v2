package com.example.demo.service;

import com.example.demo.dto.ContactoClienteRequest;
import com.example.demo.dto.ContactoClienteResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ContactoClienteMapper;
import com.example.demo.model.Cliente;
import com.example.demo.model.ContactoCliente;
import com.example.demo.model.Estado;
import com.example.demo.model.TipoDocumento;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.ContactoClienteRepository;
import com.example.demo.repository.EstadoRepository;
import com.example.demo.repository.TipoDocumentoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactoClienteService extends CrudService<ContactoCliente, Integer> {
    private final ContactoClienteRepository contactoRepository;
    private final ClienteRepository clienteRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final EstadoRepository estadoRepository;
    private final ContactoClienteMapper mapper;

    public ContactoClienteService(
            ContactoClienteRepository repository,
            ClienteRepository clienteRepository,
            TipoDocumentoRepository tipoDocumentoRepository,
            EstadoRepository estadoRepository,
            ContactoClienteMapper mapper
    ) {
        super(repository, "ContactoCliente");
        this.contactoRepository = repository;
        this.clienteRepository = clienteRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.estadoRepository = estadoRepository;
        this.mapper = mapper;
    }

    @Override
    protected Integer parseId(String id) {
        return parseIntegerId(id);
    }

    @Transactional(readOnly = true)
    public List<ContactoClienteResponse> findAllDto() {
        return contactoRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ContactoClienteResponse findDtoById(Integer id) {
        return mapper.toResponse(findContacto(id));
    }

    @Transactional(readOnly = true)
    public List<ContactoClienteResponse> findByCliente(Integer idCliente) {
        if (!clienteRepository.existsById(idCliente)) {
            throw new ResourceNotFoundException("Cliente", idCliente);
        }
        return contactoRepository.findByClienteIdCliente(idCliente).stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public ContactoClienteResponse create(ContactoClienteRequest request) {
        ContactoCliente contacto = mapper.toEntity(
                request,
                findCliente(request.idCliente()),
                findTipoDocumento(request.idTipoDoc()),
                findEstado(request.idEstado())
        );
        return mapper.toResponse(contactoRepository.save(contacto));
    }

    @Transactional
    public ContactoClienteResponse update(Integer id, ContactoClienteRequest request) {
        ContactoCliente contacto = findContacto(id);
        mapper.updateEntity(
                contacto,
                request,
                findCliente(request.idCliente()),
                findTipoDocumento(request.idTipoDoc()),
                findEstado(request.idEstado())
        );
        return mapper.toResponse(contactoRepository.save(contacto));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!contactoRepository.existsById(id)) {
            throw new ResourceNotFoundException("ContactoCliente", id);
        }
        contactoRepository.deleteById(id);
    }

    private ContactoCliente findContacto(Integer id) {
        return contactoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContactoCliente", id));
    }

    private Cliente findCliente(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
    }

    private TipoDocumento findTipoDocumento(Integer id) {
        return tipoDocumentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TipoDocumento", id));
    }

    private Estado findEstado(Integer id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado", id));
    }
}

package com.example.demo.service;

import com.example.demo.dto.ClienteRequest;
import com.example.demo.dto.ClienteResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ClienteMapper;
import com.example.demo.model.Cliente;
import com.example.demo.model.EstadoClienteContacto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.EstadoClienteContactoRepository;
import com.example.demo.repository.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService extends CrudService<Cliente, Integer> {
    private final ClienteRepository clienteRepository;
    private final EstadoClienteContactoRepository estadoClienteContactoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteMapper clienteMapper;

    public ClienteService(
            ClienteRepository repository,
            EstadoClienteContactoRepository estadoClienteContactoRepository,
            UsuarioRepository usuarioRepository,
            ClienteMapper clienteMapper
    ) {
        super(repository, "Cliente");
        this.clienteRepository = repository;
        this.estadoClienteContactoRepository = estadoClienteContactoRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    protected Integer parseId(String id) {
        return parseIntegerId(id);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> findAllDto() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse findDtoById(Integer id) {
        return clienteMapper.toResponse(findCliente(id));
    }

    @Transactional(readOnly = true)
    public ClienteResponse findDtoByRuc(String ruc) {
        if (ruc == null || !ruc.matches("\\d{11}")) {
            throw new IllegalArgumentException("El RUC debe tener 11 digitos numericos.");
        }
        return clienteMapper.toResponse(
                clienteRepository.findByRuc(ruc)
                        .orElseThrow(() -> new ResourceNotFoundException("Cliente", ruc))
        );
    }

    @Transactional
    public ClienteResponse create(ClienteRequest request) {
        EstadoClienteContacto estadoClienteContacto = findEstadoClienteContacto(request.idEstado());
        Usuario vendedor = findOptionalUsuario(request.idVendedorAsignado());
        Cliente cliente = clienteMapper.toEntity(request, estadoClienteContacto, vendedor);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponse update(Integer id, ClienteRequest request) {
        Cliente cliente = findCliente(id);
        EstadoClienteContacto estadoClienteContacto = findEstadoClienteContacto(request.idEstado());
        Usuario vendedor = findOptionalUsuario(request.idVendedorAsignado());
        clienteMapper.updateEntity(cliente, request, estadoClienteContacto, vendedor);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente", id);
        }
        clienteRepository.deleteById(id);
    }

    private Cliente findCliente(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
    }

    private EstadoClienteContacto findEstadoClienteContacto(Integer id) {
        return estadoClienteContactoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EstadoClienteContacto", id));
    }

    private Usuario findOptionalUsuario(Integer id) {
        if (id == null) {
            return null;
        }
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }
}

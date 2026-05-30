package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.BusinessValidationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteV1Service {
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoClienteRepository tipoClienteRepository;
    private final EstadoClienteContactoRepository estadoClienteContactoRepository;

    public ClienteV1Service(ClienteRepository clienteRepository, UsuarioRepository usuarioRepository, TipoClienteRepository tipoClienteRepository, EstadoClienteContactoRepository estadoClienteContactoRepository) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoClienteRepository = tipoClienteRepository;
        this.estadoClienteContactoRepository = estadoClienteContactoRepository;
    }

    @Transactional(readOnly = true)
    public List<ClienteV1Response> findAll() { return clienteRepository.findAll().stream().map(this::map).toList(); }

    @Transactional(readOnly = true)
    public ClienteV1Response findById(Integer id) { return map(findEntity(id)); }

    @Transactional(readOnly = true)
    public List<ClienteV1Response> buscar(String ruc, String razonSocial) {
        return clienteRepository.findAll().stream().filter(c -> (ruc == null || c.ruc.contains(ruc)) && (razonSocial == null || c.razonSocial.toLowerCase().contains(razonSocial.toLowerCase()))).map(this::map).toList();
    }

    @Transactional
    public ClienteV1Response create(ClienteCreateRequest request, String actor) {
        if (clienteRepository.existsByRuc(request.ruc())) {
            throw duplicate("ruc", "Ya existe un cliente con ese RUC.");
        }
        Cliente c = new Cliente();
        c.ruc = request.ruc();
        c.razonSocial = request.razonSocial();
        c.condicionSunat = request.condicionSunat();
        c.estadoSunat = request.estadoSunat();
        c.direccion = request.direccion();
        c.departamento = request.departamento();
        c.provincia = request.provincia();
        c.distrito = request.distrito();
        c.ubigeo = request.ubigeo();
        c.vendedorAsignado = usuarioRepository.findById(request.idVendedorAsignado())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", request.idVendedorAsignado()));
        c.tipoCliente = tipoClienteRepository.findById(request.idTipoCliente()).orElseThrow(() -> new ResourceNotFoundException("TipoCliente", request.idTipoCliente()));
        c.estadoClienteContacto = estadoClienteContactoRepository.findById(request.idEstadoClienteContacto()).orElseThrow(() -> new ResourceNotFoundException("EstadoClienteContacto", request.idEstadoClienteContacto()));
        c.usuarioRegistro = actor;
        c.fechaRegistro = LocalDateTime.now();
        c.usuActualiza = null;
        c.fecActualiza = null;
        return saveAndMap(c);
    }

    @Transactional
    public ClienteV1Response update(Integer id, ClienteUpdateRequest request, String actor) {
        Cliente c = findEntity(id);
        if (request.razonSocial() != null) c.razonSocial = request.razonSocial();
        if (request.condicionSunat() != null) c.condicionSunat = request.condicionSunat();
        if (request.estadoSunat() != null) c.estadoSunat = request.estadoSunat();
        if (request.direccion() != null) c.direccion = request.direccion();
        if (request.departamento() != null) c.departamento = request.departamento();
        if (request.provincia() != null) c.provincia = request.provincia();
        if (request.distrito() != null) c.distrito = request.distrito();
        if (request.ubigeo() != null) c.ubigeo = request.ubigeo();
        if (request.idVendedorAsignado() != null) c.vendedorAsignado = usuarioRepository.findById(request.idVendedorAsignado()).orElseThrow(() -> new ResourceNotFoundException("Usuario", request.idVendedorAsignado()));
        if (request.idTipoCliente() != null) c.tipoCliente = tipoClienteRepository.findById(request.idTipoCliente()).orElseThrow(() -> new ResourceNotFoundException("TipoCliente", request.idTipoCliente()));
        if (request.idEstadoClienteContacto() != null) c.estadoClienteContacto = estadoClienteContactoRepository.findById(request.idEstadoClienteContacto()).orElseThrow(() -> new ResourceNotFoundException("EstadoClienteContacto", request.idEstadoClienteContacto()));
        c.usuActualiza = actor;
        c.fecActualiza = LocalDateTime.now();
        return saveAndMap(c);
    }

    @Transactional
    public ClienteV1Response patchEstado(Integer id, Integer idEstadoClienteContacto, String actor) {
        Cliente c = findEntity(id);
        c.estadoClienteContacto = estadoClienteContactoRepository.findById(idEstadoClienteContacto).orElseThrow(() -> new ResourceNotFoundException("EstadoClienteContacto", idEstadoClienteContacto));
        c.usuActualiza = actor;
        c.fecActualiza = LocalDateTime.now();
        return saveAndMap(c);
    }

    private ClienteV1Response saveAndMap(Cliente cliente) {
        Cliente saved = clienteRepository.save(cliente);
        Cliente hydrated = clienteRepository.findById(saved.idCliente).orElse(saved);
        return map(hydrated);
    }

    private Cliente findEntity(Integer id) { return clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente", id)); }

    private ClienteV1Response map(Cliente c) {
        Integer idVendedorAsignado = c.vendedorAsignado != null ? c.vendedorAsignado.idUsuario : c.idVendedorAsignado;
        String vendedorAsignado = c.vendedorAsignado != null
                ? c.vendedorAsignado.nombres
                : resolveVendedorNombre(c.idVendedorAsignado);
        Integer idTipoCliente = c.tipoCliente != null ? c.tipoCliente.idTipoCliente : c.idTipoCliente;
        String tipoCliente = c.tipoCliente != null
                ? c.tipoCliente.descTipoCliente
                : resolveTipoClienteNombre(c.idTipoCliente);
        Integer idEstadoClienteContacto = c.estadoClienteContacto != null
                ? c.estadoClienteContacto.idEstadoClienteContacto
                : c.idEstadoClienteContacto;
        String estadoClienteContacto = c.estadoClienteContacto != null
                ? c.estadoClienteContacto.desEstadoClienteContacto
                : resolveEstadoClienteContactoNombre(idEstadoClienteContacto);

        return new ClienteV1Response(c.idCliente, c.ruc, c.razonSocial, c.condicionSunat, c.estadoSunat, c.direccion, c.departamento, c.provincia, c.distrito, c.ubigeo,
                idVendedorAsignado,
                vendedorAsignado,
                idTipoCliente,
                tipoCliente,
                idEstadoClienteContacto,
                estadoClienteContacto,
                c.usuarioRegistro, c.fechaRegistro, c.usuActualiza, c.fecActualiza);
    }

    private String resolveVendedorNombre(Integer idVendedorAsignado) {
        if (idVendedorAsignado == null) {
            return null;
        }
        return usuarioRepository.findById(idVendedorAsignado).map(u -> u.nombres).orElse(null);
    }

    private String resolveTipoClienteNombre(Integer idTipoCliente) {
        if (idTipoCliente == null) {
            return null;
        }
        return tipoClienteRepository.findById(idTipoCliente).map(t -> t.descTipoCliente).orElse(null);
    }

    private String resolveEstadoClienteContactoNombre(Integer idEstadoClienteContacto) {
        if (idEstadoClienteContacto == null) {
            return null;
        }
        return estadoClienteContactoRepository.findById(idEstadoClienteContacto)
                .map(e -> e.desEstadoClienteContacto)
                .orElse(null);
    }

    private BusinessValidationException duplicate(String field, String message) {
        Map<String, List<String>> fields = new LinkedHashMap<>();
        fields.put(field, List.of(message));
        return new BusinessValidationException(message, fields);
    }
}

package com.example.demo.controller;

import com.example.demo.dto.ContactoClienteRequest;
import com.example.demo.dto.ContactoClienteResponse;
import com.example.demo.service.ContactoClienteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contactos-cliente")
public class ContactoClienteController {
    private final ContactoClienteService service;

    public ContactoClienteController(ContactoClienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<ContactoClienteResponse> findAll() {
        return service.findAllDto();
    }

    @GetMapping("/{id}")
    public ContactoClienteResponse findById(@PathVariable Integer id) {
        return service.findDtoById(id);
    }

    @GetMapping("/cliente/{idCliente}")
    public List<ContactoClienteResponse> findByCliente(@PathVariable Integer idCliente) {
        return service.findByCliente(idCliente);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContactoClienteResponse create(@Valid @RequestBody ContactoClienteRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ContactoClienteResponse update(@PathVariable Integer id, @Valid @RequestBody ContactoClienteRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        service.deleteById(id);
    }
}

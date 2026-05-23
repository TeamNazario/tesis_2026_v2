package com.example.demo.controller;

import com.example.demo.dto.LogEficienciaChatbotRequest;
import com.example.demo.dto.LogEficienciaChatbotResponse;
import com.example.demo.service.LogEficienciaChatbotService;
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
@RequestMapping("/api/logs-eficiencia-chatbot")
public class LogEficienciaChatbotController {
    private final LogEficienciaChatbotService service;

    public LogEficienciaChatbotController(LogEficienciaChatbotService service) {
        this.service = service;
    }

    @GetMapping
    public List<LogEficienciaChatbotResponse> findAll() {
        return service.findAllDto();
    }

    @GetMapping("/{id}")
    public LogEficienciaChatbotResponse findById(@PathVariable Long id) {
        return service.findDtoById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LogEficienciaChatbotResponse create(@Valid @RequestBody LogEficienciaChatbotRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public LogEficienciaChatbotResponse update(
            @PathVariable Long id,
            @Valid @RequestBody LogEficienciaChatbotRequest request
    ) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }
}

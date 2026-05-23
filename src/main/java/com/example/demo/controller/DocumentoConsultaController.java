package com.example.demo.controller;

import com.example.demo.dto.DniConsultaResponse;
import com.example.demo.dto.RucConsultaResponse;
import com.example.demo.service.DocumentoConsultaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/documentos")
public class DocumentoConsultaController {
    private final DocumentoConsultaService documentoConsultaService;

    public DocumentoConsultaController(DocumentoConsultaService documentoConsultaService) {
        this.documentoConsultaService = documentoConsultaService;
    }

    @GetMapping("/dni/{numero}")
    public DniConsultaResponse consultarDni(@PathVariable String numero) {
        return documentoConsultaService.consultarDni(numero);
    }

    @GetMapping("/ruc/{numero}")
    public RucConsultaResponse consultarRuc(@PathVariable String numero) {
        return documentoConsultaService.consultarRuc(numero);
    }
}

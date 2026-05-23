package com.example.demo.service;

import com.example.demo.dto.DniConsultaResponse;
import com.example.demo.dto.RucConsultaResponse;

public interface DocumentoConsultaService {
    DniConsultaResponse consultarDni(String numero);

    RucConsultaResponse consultarRuc(String numero);
}

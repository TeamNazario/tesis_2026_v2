package com.example.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.DniConsultaResponse;
import com.example.demo.dto.RucConsultaResponse;
import com.example.demo.exception.DocumentoInvalidoException;
import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.service.DocumentoConsultaService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class DocumentoConsultaControllerTest {
    @Mock
    private DocumentoConsultaService documentoConsultaService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new DocumentoConsultaController(documentoConsultaService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void consultarDniRetornaRespuestaOk() throws Exception {
        when(documentoConsultaService.consultarDni("12345678")).thenReturn(new DniConsultaResponse(
                "12345678",
                "JUAN CARLOS",
                "PEREZ",
                "LOPEZ",
                "1",
                "JUAN CARLOS PEREZ LOPEZ"
        ));

        mockMvc.perform(get("/api/v1/documentos/dni/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.nombreCompleto").value("JUAN CARLOS PEREZ LOPEZ"));
    }

    @Test
    void consultarRucRetornaRespuestaOk() throws Exception {
        when(documentoConsultaService.consultarRuc("20123456789")).thenReturn(new RucConsultaResponse(
                "20123456789",
                "EMPRESA DEMO S.A.C.",
                "EMPRESA DEMO",
                "ACTIVO",
                "HABIDO",
                "AV DEMO 123",
                "LIMA",
                "LIMA",
                "ATE",
                "150103",
                List.of("999999999"),
                "1000.00",
                true,
                true,
                true,
                "RUC valido para cotizacion automatica."
        ));

        mockMvc.perform(get("/api/v1/documentos/ruc/20123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ruc").value("20123456789"))
                .andExpect(jsonPath("$.aptoParaCotizacion").value(true));
    }

    @Test
    void consultarDniInvalidoRetorna400() throws Exception {
        when(documentoConsultaService.consultarDni("123"))
                .thenThrow(new DocumentoInvalidoException("El DNI debe contener exactamente 8 digitos numericos."));

        mockMvc.perform(get("/api/v1/documentos/dni/123"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El DNI debe contener exactamente 8 digitos numericos."));
    }

}

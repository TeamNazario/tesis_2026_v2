package com.example.demo.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.dto.ProductoResponse;
import com.example.demo.dto.ReferenceResponse;
import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.service.ProductoService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {
    @Mock
    private ProductoService productoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ProductoController(productoService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void findActivosReturnsProducts() throws Exception {
        when(productoService.findActivos()).thenReturn(List.of(new ProductoResponse(
                1,
                "AUS32",
                "Descripcion",
                "GL",
                BigDecimal.TEN,
                BigDecimal.valueOf(32.5),
                100,
                10,
                90,
                20,
                new ReferenceResponse(1, "Activo")
        )));

        mockMvc.perform(get("/api/productos/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombreProducto").value("AUS32"));
    }

    @Test
    void createRejectsInvalidRequestBeforeCallingService() throws Exception {
        String body = """
                {
                  "nombreProducto": "",
                  "unidadMedida": "GL",
                  "precioBaseUnitario": 10,
                  "concentracionUreaAus32": 32.5,
                  "stockFisico": 100,
                  "stockReservado": 10,
                  "stockDisponible": 90,
                  "stockMinimoSeguridad": 20,
                  "idEstado": 1
                }
                """;

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.nombreProducto[0]").value("El nombre del producto es obligatorio."));

        verifyNoInteractions(productoService);
    }
}

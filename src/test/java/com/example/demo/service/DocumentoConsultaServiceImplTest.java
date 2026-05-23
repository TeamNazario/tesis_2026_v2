package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.demo.client.ApisPeruClient;
import com.example.demo.dto.DniConsultaResponse;
import com.example.demo.dto.RucConsultaResponse;
import com.example.demo.dto.external.ApisPeruDniResponse;
import com.example.demo.dto.external.ApisPeruRucResponse;
import com.example.demo.exception.DocumentoInvalidoException;
import com.example.demo.exception.ExternalApiException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DocumentoConsultaServiceImplTest {
    @Mock
    private ApisPeruClient apisPeruClient;

    private DocumentoConsultaServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DocumentoConsultaServiceImpl(apisPeruClient);
    }

    @Test
    void consultarDniValidoRetornaDatosMapeados() {
        when(apisPeruClient.consultarDni("12345678")).thenReturn(new ApisPeruDniResponse(
                "12345678",
                "JUAN CARLOS",
                "PEREZ",
                "LOPEZ",
                "1"
        ));

        DniConsultaResponse response = service.consultarDni("12345678");

        assertThat(response.dni()).isEqualTo("12345678");
        assertThat(response.nombreCompleto()).isEqualTo("JUAN CARLOS PEREZ LOPEZ");
    }

    @Test
    void consultarDniInvalidoLanzaBadRequest() {
        assertThatThrownBy(() -> service.consultarDni("123"))
                .isInstanceOf(DocumentoInvalidoException.class)
                .hasMessage("El DNI debe contener exactamente 8 digitos numericos.");
    }

    @Test
    void consultarRucValidoActivoHabidoMarcaApto() {
        when(apisPeruClient.consultarRuc("20123456789")).thenReturn(new ApisPeruRucResponse(
                "20123456789",
                "EMPRESA DEMO S.A.C.",
                "EMPRESA DEMO",
                List.of("999999999"),
                "ACTIVO",
                "HABIDO",
                "AV DEMO 123",
                "LIMA",
                "LIMA",
                "ATE",
                "150103",
                "1000.00"
        ));

        RucConsultaResponse response = service.consultarRuc("20123456789");

        assertThat(response.activo()).isTrue();
        assertThat(response.habido()).isTrue();
        assertThat(response.aptoParaCotizacion()).isTrue();
    }

    @Test
    void consultarRucInactivoNoApto() {
        when(apisPeruClient.consultarRuc("20123456789")).thenReturn(new ApisPeruRucResponse(
                "20123456789",
                "EMPRESA DEMO S.A.C.",
                "EMPRESA DEMO",
                List.of(),
                "BAJA",
                "HABIDO",
                "AV DEMO 123",
                "LIMA",
                "LIMA",
                "ATE",
                "150103",
                "1000.00"
        ));

        RucConsultaResponse response = service.consultarRuc("20123456789");

        assertThat(response.activo()).isFalse();
        assertThat(response.aptoParaCotizacion()).isFalse();
    }

    @Test
    void consultarRucNoHabidoNoApto() {
        when(apisPeruClient.consultarRuc("20123456789")).thenReturn(new ApisPeruRucResponse(
                "20123456789",
                "EMPRESA DEMO S.A.C.",
                "EMPRESA DEMO",
                List.of(),
                "ACTIVO",
                "NO HABIDO",
                "AV DEMO 123",
                "LIMA",
                "LIMA",
                "ATE",
                "150103",
                "1000.00"
        ));

        RucConsultaResponse response = service.consultarRuc("20123456789");

        assertThat(response.habido()).isFalse();
        assertThat(response.aptoParaCotizacion()).isFalse();
    }

    @Test
    void consultarRucInvalidoLanzaBadRequest() {
        assertThatThrownBy(() -> service.consultarRuc("123"))
                .isInstanceOf(DocumentoInvalidoException.class)
                .hasMessage("El RUC debe contener exactamente 11 digitos numericos.");
    }

    @Test
    void consultarRucPrefijoInvalidoLanzaBadRequest() {
        assertThatThrownBy(() -> service.consultarRuc("30123456789"))
                .isInstanceOf(DocumentoInvalidoException.class)
                .hasMessage("El RUC debe iniciar con 10 o 20.");
    }

    @Test
    void propagaErrorApiExterna() {
        when(apisPeruClient.consultarRuc("20123456789"))
                .thenThrow(new ExternalApiException("timeout"));

        assertThatThrownBy(() -> service.consultarRuc("20123456789"))
                .isInstanceOf(ExternalApiException.class);
    }
}

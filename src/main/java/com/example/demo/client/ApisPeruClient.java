package com.example.demo.client;

import com.example.demo.config.ApisPeruProperties;
import com.example.demo.dto.external.ApisPeruDniResponse;
import com.example.demo.dto.external.ApisPeruErrorResponse;
import com.example.demo.dto.external.ApisPeruRucResponse;
import com.example.demo.exception.DocumentoNoEncontradoException;
import com.example.demo.exception.ExternalApiException;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class ApisPeruClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApisPeruClient.class);

    private final WebClient apisPeruWebClient;
    private final ApisPeruProperties properties;
    private final ObjectMapper objectMapper;

    public ApisPeruClient(WebClient apisPeruWebClient, ApisPeruProperties properties, ObjectMapper objectMapper) {
        this.apisPeruWebClient = apisPeruWebClient;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public ApisPeruDniResponse consultarDni(String numero) {
        JsonNode payload = executeGet("/dni/{numero}", numero);
        return mapSuccessOrThrow(payload, ApisPeruDniResponse.class);
    }

    public ApisPeruRucResponse consultarRuc(String numero) {
        JsonNode payload = executeGet("/ruc/{numero}", numero);
        return mapSuccessOrThrow(payload, ApisPeruRucResponse.class);
    }

    private JsonNode executeGet(String path, String numero) {
        if (!StringUtils.hasText(properties.getToken())) {
            throw new ExternalApiException("No se encontro configuracion del token APISPERU para consulta de documentos.");
        }
        try {
            String responseBody = apisPeruWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(path)
                            .queryParam("token", properties.getToken())
                            .build(numero))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (responseBody == null || responseBody.isBlank()) {
                throw new ExternalApiException("El servicio externo devolvio una respuesta vacia.");
            }

            return objectMapper.readTree(responseBody);
        } catch (WebClientResponseException ex) {
            handleHttpError(ex);
            throw new ExternalApiException("Error no controlado en servicio externo.", ex);
        } catch (WebClientRequestException ex) {
            LOGGER.warn("Fallo de conectividad consultando APISPERU: {}", ex.getMessage());
            throw new ExternalApiException(
                    "No fue posible consultar el servicio externo de DNI/RUC. Intente nuevamente.",
                    ex
            );
        } catch (ExternalApiException | DocumentoNoEncontradoException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ExternalApiException("No fue posible interpretar la respuesta del servicio externo.", ex);
        }
    }

    private <T> T mapSuccessOrThrow(JsonNode payload, Class<T> targetType) {
        if (payload.has("success")) {
            boolean success = payload.path("success").asBoolean(false);
            if (!success) {
                ApisPeruErrorResponse error = objectMapper.convertValue(payload, ApisPeruErrorResponse.class);
                String message = error.message() == null
                        ? "No se encontro informacion para el documento consultado."
                        : error.message();
                String normalized = message.toLowerCase(Locale.ROOT);
                if (normalized.contains("no se encontro")
                        || normalized.contains("no existe")
                        || normalized.contains("no encontrado")) {
                    throw new DocumentoNoEncontradoException(message);
                }
                throw new ExternalApiException(message);
            }

            if (payload.has("data") && !payload.path("data").isNull()) {
                return objectMapper.convertValue(payload.path("data"), targetType);
            }
        }

        return objectMapper.convertValue(payload, targetType);
    }

    private void handleHttpError(WebClientResponseException ex) {
        HttpStatusCode status = ex.getStatusCode();
        if (status.value() == 404) {
            throw new DocumentoNoEncontradoException("No se encontro informacion para el documento consultado.");
        }
        if (status.value() == 429 || status.value() == 502 || status.value() == 503) {
            throw new ExternalApiException("No fue posible consultar el servicio externo de DNI/RUC. Intente nuevamente.", ex);
        }

        String body = ex.getResponseBodyAsString();
        LOGGER.warn("Error HTTP {} desde APISPERU. Body resumido: {}", status.value(), summarize(body));
        throw new ExternalApiException("Error consultando el servicio externo de DNI/RUC.", ex);
    }

    private String summarize(String text) {
        if (text == null) {
            return "";
        }
        return text.length() <= 180 ? text : text.substring(0, 180) + "...";
    }
}

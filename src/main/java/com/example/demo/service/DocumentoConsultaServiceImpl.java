package com.example.demo.service;

import com.example.demo.client.ApisPeruClient;
import com.example.demo.dto.DniConsultaResponse;
import com.example.demo.dto.RucConsultaResponse;
import com.example.demo.dto.external.ApisPeruDniResponse;
import com.example.demo.dto.external.ApisPeruRucResponse;
import com.example.demo.exception.DocumentoInvalidoException;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class DocumentoConsultaServiceImpl implements DocumentoConsultaService {
    private final ApisPeruClient apisPeruClient;

    public DocumentoConsultaServiceImpl(ApisPeruClient apisPeruClient) {
        this.apisPeruClient = apisPeruClient;
    }

    @Override
    public DniConsultaResponse consultarDni(String numero) {
        String sanitized = sanitize(numero);
        if (!sanitized.matches("^\\d{8}$")) {
            throw new DocumentoInvalidoException("El DNI debe contener exactamente 8 digitos numericos.");
        }

        ApisPeruDniResponse external = apisPeruClient.consultarDni(sanitized);
        String nombreCompleto = String.join(" ",
                        safe(external.nombres()),
                        safe(external.apellidoPaterno()),
                        safe(external.apellidoMaterno()))
                .trim()
                .replaceAll("\\s{2,}", " ");

        return new DniConsultaResponse(
                external.dni(),
                external.nombres(),
                external.apellidoPaterno(),
                external.apellidoMaterno(),
                external.codVerifica(),
                nombreCompleto
        );
    }

    @Override
    public RucConsultaResponse consultarRuc(String numero) {
        String sanitized = sanitize(numero);
        if (!sanitized.matches("^\\d{11}$")) {
            throw new DocumentoInvalidoException("El RUC debe contener exactamente 11 digitos numericos.");
        }
        if (!(sanitized.startsWith("10") || sanitized.startsWith("20"))) {
            throw new DocumentoInvalidoException("El RUC debe iniciar con 10 o 20.");
        }

        ApisPeruRucResponse external = apisPeruClient.consultarRuc(sanitized);
        boolean activo = "ACTIVO".equalsIgnoreCase(safe(external.estado()).trim());
        boolean habido = "HABIDO".equalsIgnoreCase(safe(external.condicion()).trim());
        boolean aptoParaCotizacion = activo && habido;
        String mensajeValidacion = aptoParaCotizacion
                ? "RUC valido para cotizacion automatica."
                : "RUC no apto para cotizacion automatica. Verifique estado y condicion SUNAT.";

        return new RucConsultaResponse(
                external.ruc(),
                external.razonSocial(),
                external.nombreComercial(),
                external.estado(),
                external.condicion(),
                external.direccion(),
                external.departamento(),
                external.provincia(),
                external.distrito(),
                external.ubigeo(),
                external.telefonos() == null ? List.of() : external.telefonos(),
                external.capital(),
                activo,
                habido,
                aptoParaCotizacion,
                mensajeValidacion
        );
    }

    private String sanitize(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }
        return value.trim().replace(" ", "");
    }

    private String safe(String value) {
        return value == null ? "" : value.toUpperCase(Locale.ROOT);
    }
}

package com.example.demo.controller;

import com.example.demo.dto.UbigeoCoberturaResponse;
import com.example.demo.dto.UbigeoIneiResponse;
import com.example.demo.service.UbigeoIneiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ubigeo")
@Tag(name = "Ubigeo INEI", description = "Consulta de ubicaciones geograficas del Peru (INEI) y cobertura de despacho BIOFLUID D32")
public class UbigeoIneiController {

    private final UbigeoIneiService service;

    public UbigeoIneiController(UbigeoIneiService service) {
        this.service = service;
    }

    @GetMapping("/departamentos")
    @Operation(summary = "Listar todos los departamentos", description = "Retorna la lista de departamentos del Peru ordenados alfabeticamente")
    public List<String> getDepartamentos() {
        return service.findDepartamentos();
    }

    @GetMapping("/provincias")
    @Operation(summary = "Listar provincias", description = "Retorna provincias, opcionalmente filtradas por departamento")
    public List<String> getProvincias(
            @Parameter(description = "Nombre del departamento para filtrar (opcional)")
            @RequestParam(required = false) String departamento) {
        return service.findProvincias(departamento);
    }

    @GetMapping("/distritos")
    @Operation(summary = "Listar distritos", description = "Retorna distritos con su ubigeo, opcionalmente filtrados por departamento y/o provincia")
    public List<UbigeoIneiResponse> getDistritos(
            @Parameter(description = "Nombre del departamento para filtrar (opcional)")
            @RequestParam(required = false) String departamento,
            @Parameter(description = "Nombre de la provincia para filtrar (opcional)")
            @RequestParam(required = false) String provincia) {
        return service.findDistritos(departamento, provincia);
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar por codigo ubigeo", description = "Retorna el registro completo de una ubicacion por su codigo ubigeo de 6 digitos")
    public UbigeoIneiResponse getByCodigo(
            @Parameter(description = "Codigo ubigeo de 6 digitos (ej: 130101)")
            @PathVariable String codigo) {
        return service.findByCodigo(codigo);
    }

    @GetMapping("/{ubigeo}")
    @Operation(summary = "Buscar por ubigeo", description = "Retorna la informacion completa de una ubicacion por su codigo ubigeo")
    public UbigeoIneiResponse getByUbigeo(
            @Parameter(description = "Codigo ubigeo de 6 digitos (ej: 130107)")
            @PathVariable String ubigeo) {
        return service.findByUbigeo(ubigeo);
    }

    @GetMapping("/{ubigeo}/cobertura")
    @Operation(summary = "Verificar cobertura de despacho", description = "Indica si BIOFLUID D32 tiene cobertura de despacho en la ubicacion del ubigeo dado")
    public UbigeoCoberturaResponse getCobertura(
            @Parameter(description = "Codigo ubigeo de 6 digitos (ej: 150101)")
            @PathVariable String ubigeo) {
        return service.findCobertura(ubigeo);
    }
}

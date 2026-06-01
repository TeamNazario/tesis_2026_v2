package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI biofluidOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Biofluid API")
                        .description("API REST para gestion de clientes, productos, cotizaciones, usuarios y logs. La gestion de estados se realiza mediante catalogos especificos por dominio.")
                        .version("1.1.0")
                        .contact(new Contact()
                                .name("Proyecto Tesis")
                                .email("soporte@biofluid.local"))
                        .license(new License()
                                .name("Uso academico")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                                .name(BEARER_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}

package com.dba.cnpj_manager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão de CNPJ e Filiais")
                        .version("v1.0.0")
                        .description(
                                "API RESTful para gerenciamento de Empresas Matrizes e suas respectivas Filiais, com suporte a geolocalização.")
                        .contact(new Contact()
                                .name("Dev Responsável")
                                .email("elleres.dev@gmail.com")));
    }
}
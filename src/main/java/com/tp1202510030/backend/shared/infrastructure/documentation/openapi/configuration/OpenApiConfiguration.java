package com.tp1202510030.backend.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI greenhouseOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Greenhouse API")
                        .description("Greenhouse application REST API documentation.")
                        .version("v1.0.0")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("alanegd")));
    }
}
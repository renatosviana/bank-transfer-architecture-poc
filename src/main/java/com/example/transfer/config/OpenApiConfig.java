package com.example.transfer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank Transfer Architecture Lab")
                        .version("1.0")
                        .description("Simple app to practice API-first, resilience, idempotency, cache, batch, and DLQ concepts."));
    }
}

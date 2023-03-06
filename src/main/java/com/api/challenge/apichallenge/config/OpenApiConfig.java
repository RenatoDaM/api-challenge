package com.api.challenge.apichallenge.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("api-challenge")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI apiChallengeOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Clientes API")
                        .description("Consome uma API externa, trata seus dados e salva em arquivo .csv")
                        .version("v1"));

    }
}

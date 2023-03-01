package com.api.challenge.apichallenge.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {
    @Bean
    public WebClient webClient() {
        WebClient client = WebClient.builder()
                .baseUrl("https://api.jsonbin.io/v3")
                .defaultHeader(HttpHeaders.AUTHORIZATION_KEY, HttpHeaders.AUTHORIZATION_VALUE)
                .build();
        return client;
    }

    @Bean
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();
        return mapper;
    }
}

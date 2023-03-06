package com.api.challenge.apichallenge.config;

import com.api.challenge.apichallenge.util.jsonparser.ClienteJsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SpringBeansConfig {
    @Bean
    public WebClient webClient() {
        WebClient client = WebClient.builder()
                .baseUrl("https://api.jsonbin.io/v3")
                .defaultHeader(HeadersDefault.AUTHORIZATION_KEY, HeadersDefault.AUTHORIZATION_VALUE)
                .build();
        return client;
    }

    @Bean
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();
        return mapper;
    }

    @Bean
    public ClienteJsonParser parser() {
        ClienteJsonParser parser = new ClienteJsonParser();
        return parser;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}

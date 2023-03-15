package com.api.challenge.apichallenge.integrationtests;

import com.api.challenge.apichallenge.config.HeadersDefault;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;

public class UriConnectionClientesJsonTest {
    @Test
    public void UriConnectionTest() {
        WebTestClient.bindToServer()
                .baseUrl("https://api.jsonbin.io/v3")
                .defaultHeader(HeadersDefault.AUTHORIZATION_KEY, HeadersDefault.AUTHORIZATION_VALUE)
                .build()
                .get()
                .uri("/b/63fa39efc0e7653a057e6fa7")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);
    }
}

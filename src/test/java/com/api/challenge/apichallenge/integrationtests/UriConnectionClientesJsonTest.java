package com.api.challenge.apichallenge.integrationtests;

import com.api.challenge.apichallenge.config.HeadersDefault;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
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

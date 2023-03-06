package com.api.challenge.apichallenge.dao;

import com.api.challenge.apichallenge.config.HeadersDefault;
import com.api.challenge.apichallenge.dto.v2.ClienteResponseWrapperDTOV2;
import com.api.challenge.apichallenge.exception.InvalidURIException;
import com.api.challenge.apichallenge.util.jsonparser.ClienteJsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
@Repository
public class ClienteDAO {
    @Autowired
    WebClient client;

    public Flux<String> postCSV() {
        return client.get()
                .uri("/b/63fa39efc0e7653a057e6fa7")
                .retrieve()
                .bodyToFlux(String.class);
    }

    public String getClientes() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity entity = new HttpEntity(null, headers);
        headers.set(HeadersDefault.AUTHORIZATION_KEY, HeadersDefault.AUTHORIZATION_VALUE);
        ResponseEntity<String> clientesJson = restTemplate.exchange("https://api.jsonbin.io/v3/b/63fa39efc0e7653a057e6fa7", HttpMethod.GET, entity, String.class);
        return clientesJson.getBody();
    }

    public Flux<String> getClientesV2() {
        Flux<String> record = client.get()
                .uri("/b/63fa39efc0e7653a057e6fa7")
                .retrieve()
                .bodyToFlux(String.class)
                .onErrorResume(e -> Flux.error(new InvalidURIException("Não foi possível realizar operação GET. URI inválida ou inacessível no momento.")));

        return record;
    }
}

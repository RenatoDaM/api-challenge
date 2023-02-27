package com.api.challenge.apichallenge.service;

import com.api.challenge.apichallenge.config.HttpHeaders;
import com.api.challenge.apichallenge.mapper.JSONClienteMapper;
import com.api.challenge.apichallenge.response.ClienteResponse;
import com.api.challenge.apichallenge.response.Record;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClienteService {


    // POST

    // GET
    @SuppressWarnings("unchecked")
    @JsonProperty("brand")
    public Page<ClienteResponse> getClientes(Pageable pageable) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        WebClient client = WebClient.builder()
                .baseUrl("https://api.jsonbin.io/v3")
                .defaultHeader(HttpHeaders.AUTHORIZATION_KEY, HttpHeaders.AUTHORIZATION_VALUE)
                .build();

        Mono<String> record = client.get()
                .uri("/b/63fa39efc0e7653a057e6fa7")
                .retrieve()
                .bodyToMono(String.class);

        String JSON_SOURCE = record.block();

        JsonNode jsonNode = objectMapper.readTree(JSON_SOURCE);
        JsonNode clientesNode = jsonNode.get("record");
        Record clientes = objectMapper.readValue(clientesNode.traverse(), new TypeReference<Record>(){});
        List<ClienteResponse> clienteList = clientes.getRecord().stream().sorted(Comparator.comparing(ClienteResponse::getNome)).collect(Collectors.toList());
        paginarLista(clienteList, pageable);

        return paginarLista(clienteList, pageable);

    }

    private Page<ClienteResponse> paginarLista(List<ClienteResponse> lista, Pageable pageable){
        int inicio, fim;
        inicio = (int) pageable.getOffset();
        fim = (inicio + pageable.getPageSize()) > lista.size() ? lista.size() : (inicio + pageable.getPageSize());
        Page<ClienteResponse> paginacao = new PageImpl<ClienteResponse>(lista.stream().collect(Collectors.toList()).subList(inicio, fim), pageable, lista.size());
        return paginacao;
    }
}

package com.api.challenge.apichallenge.service;

import com.api.challenge.apichallenge.config.HttpHeaders;
import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v1.ClienteResponseWrapper;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.response.v2.ClienteResponseWrapperV2;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        ClienteResponseWrapper clientes = objectMapper.readValue(clientesNode.traverse(), new TypeReference<ClienteResponseWrapper>(){});
        List<ClienteResponse> clienteList = clientes.getRecord().stream().sorted(Comparator.comparing(ClienteResponse::getNome)).collect(Collectors.toList());

        return paginarLista(clienteList, pageable);
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("brand")
    public Page<ClienteResponseV2> getClientesV2(Pageable pageable) throws IOException {
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
        ClienteResponseWrapperV2 clientes = objectMapper.readValue(clientesNode.traverse(), new TypeReference<ClienteResponseWrapperV2>(){});
        List<ClienteResponseV2> clienteResponseList = clientes.getRecord();
        ModelMapper mapper = new ModelMapper();

        List<ClienteResponseV2> clienteListV2 = clienteResponseList.stream()
                .map(cliente -> mapper.map(cliente, ClienteResponseV2.class))
                .sorted(Comparator.comparing(ClienteResponseV2::getNome))
                .collect(Collectors.toList());

        IntStream.rangeClosed(1, clienteListV2.size())
                .boxed()
                .forEach(i -> clienteListV2.get(i-1).setId(i));

        return paginarListaV2(clienteListV2, pageable);
    }

    private Page<ClienteResponse> paginarLista(List<ClienteResponse> lista, Pageable pageable){
        int inicio, fim;
        inicio = (int) pageable.getOffset();
        fim = (inicio + pageable.getPageSize()) > lista.size() ? lista.size() : (inicio + pageable.getPageSize());
        Page<ClienteResponse> paginacao = new PageImpl<ClienteResponse>(lista.stream().collect(Collectors.toList()).subList(inicio, fim), pageable, lista.size());
        return paginacao;
    }

    private Page<ClienteResponseV2> paginarListaV2(List<ClienteResponseV2> lista, Pageable pageable){
        int inicio, fim;
        inicio = (int) pageable.getOffset();
        fim = (inicio + pageable.getPageSize()) > lista.size() ? lista.size() : (inicio + pageable.getPageSize());
        Page<ClienteResponseV2> paginacao = new PageImpl<ClienteResponseV2>(lista.stream().collect(Collectors.toList()).subList(inicio, fim), pageable, lista.size());
        return paginacao;
    }
}

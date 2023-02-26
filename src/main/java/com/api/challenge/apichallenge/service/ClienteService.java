package com.api.challenge.apichallenge.service;

import com.api.challenge.apichallenge.config.HttpHeaders;
import com.api.challenge.apichallenge.mapper.JSONClienteMapper;
import com.api.challenge.apichallenge.model.Cliente;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {


    // POST

    // GET
    @SuppressWarnings("unchecked")
    @JsonProperty("brand")
    public Page<Cliente> getClientes(Pageable pageable) throws JsonProcessingException {
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

        // Aqui houve muitas alternativas para fazer o mapeamento, das que pesquisei
        // a mais simples e que seria menos necessário criar classes em vão foi o json.org.
        // Outra alternativa interessante seria o GSON.
        // Pelo jackson me parece que a forma mais comum seria realmente criar
        // classes "nestadas" igual o json, mas talvez haja uma forma mais inteligente
        // customizando por exemplo o ObjectMapper ou alguma utilização da biblioteca jackson.

        List<Cliente> clientes = new ArrayList<>();
        JSONClienteMapper.parseClienteJson(JSON_SOURCE, clientes);

        return paginarLista(clientes, pageable);

    }

    private Page<Cliente> paginarLista(List<Cliente> lista, Pageable pageable){
        int inicio, fim;
        inicio = (int) pageable.getOffset();
        fim = (inicio + pageable.getPageSize()) > lista.size() ? lista.size() : (inicio + pageable.getPageSize());
        Page<Cliente> paginacao = new PageImpl<Cliente>(lista.stream().collect(Collectors.toList()).subList(inicio, fim), pageable, lista.size());
        return paginacao;
    }
}

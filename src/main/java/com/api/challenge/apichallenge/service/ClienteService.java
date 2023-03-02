package com.api.challenge.apichallenge.service;

import com.api.challenge.apichallenge.config.WebConfig;
import com.api.challenge.apichallenge.util.csv.ClienteCSVHandler;
import com.api.challenge.apichallenge.util.dateutil.AniversarioParaDNConversor;
import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v1.ClienteResponseWrapper;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.response.v2.ClienteResponseWrapperV2;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WebClient client;

    public ClienteResponseV2 updateCSV(ClienteResponseV2 cliente) {
        return null;
    }

    public void deleteCSV() {

    }

    public Page<ClienteResponseV2> readCSV(Pageable pageable) throws FileNotFoundException {
        ClienteCSVHandler clienteCSVHandler = new ClienteCSVHandler(WebConfig.CSV_FILE_PATH);
        return paginarListaCSV(clienteCSVHandler.read(), pageable);
    }
    // POST
    @SuppressWarnings("unchecked")
    @JsonProperty("brand")
    public Flux<Object> postCSV() {
        ClienteCSVHandler csvWriter = new ClienteCSVHandler(WebConfig.CSV_FILE_PATH);
        return client.get()
                .uri("/b/63fa39efc0e7653a057e6fa7")
                .retrieve()
                .bodyToFlux(String.class)
                .map(this::parseJson)
                .map(this::extrairNodeClientes)
                .map(this::mapearParaListaDeClientes)
                .map(ClienteResponseWrapperV2::getClientesResponseV2List)
                .flatMap(clientes -> {

                    Flux<ClienteResponseV2> flux = Flux.fromIterable(clientes)
                            .map(AniversarioParaDNConversor::formatarAniversarioParaDataNascimento)
                            .sort(Comparator.comparing(ClienteResponseV2::getNome))
                            .zipWith(Flux.range(1, clientes.size()),
                                    (clienteResponse, id) -> {
                                        clienteResponse.setId(id);
                                        return clienteResponse;
                                    }).doOnNext(client -> {
                                try {
                                    csvWriter.write(client);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                    return flux;
                });
                // É possível que eu não retorne um Response, pois assim poderia usar dos Query Params.
                // Também é possível q no body eu devolva um CSV dependendo da regra de negócio.


    }
    // GET
    @SuppressWarnings("unchecked")
    @JsonProperty("brand")
    public Page<ClienteResponse> getClientes(Pageable pageable) throws IOException {
        Mono<String> record = client.get()
                .uri("/b/63fa39efc0e7653a057e6fa7")
                .retrieve()
                .bodyToMono(String.class);

        String JSON_SOURCE = record.block();

        JsonNode jsonNode = objectMapper.readTree(JSON_SOURCE);
        JsonNode clientesNode = jsonNode.get("record");
        ClienteResponseWrapper clientes = objectMapper.readValue(clientesNode.traverse(), new TypeReference<ClienteResponseWrapper>(){});
        List<ClienteResponse> clienteList = clientes.getRecord().stream().sorted(Comparator.comparing(ClienteResponse::getNome)).collect(Collectors.toList());
        AniversarioParaDNConversor conversor = new AniversarioParaDNConversor();
        clienteList.forEach(element -> conversor.formatarAniversarioParaDataNascimento(element));

        return paginarLista(clienteList, pageable);
    }



    @SuppressWarnings("unchecked")
    @JsonProperty("brand")
    public Flux<Page<ClienteResponseV2>> getClientesV2(Pageable pageable) {
        Flux<String> record = client.get()
                .uri("/b/63fa39efc0e7653a057e6fa7")
                .retrieve()
                .bodyToFlux(String.class);

        return record.map(this::parseJson)
                .map(this::extrairNodeClientes)
                .map(this::mapearParaListaDeClientes)
                .map(ClienteResponseWrapperV2::getClientesResponseV2List)
                .flatMap(clientes -> {

                    Flux<ClienteResponseV2> flux = Flux.fromIterable(clientes)
                            .map(AniversarioParaDNConversor::formatarAniversarioParaDataNascimento)
                            .sort(Comparator.comparing(ClienteResponseV2::getNome))
                            .zipWith(Flux.range(1, clientes.size()),
                                    (clienteResponse, id) -> {
                                        clienteResponse.setId(id);
                                        return clienteResponse;
                                    });
                    return paginarListaV2(flux, pageable);
                });

    }

    private Page<ClienteResponse> paginarLista(List<ClienteResponse> lista, Pageable pageable){
        int inicio, fim;
        inicio = (int) pageable.getOffset();
        fim = (inicio + pageable.getPageSize()) > lista.size() ? lista.size() : (inicio + pageable.getPageSize());
        Page<ClienteResponse> paginacao = new PageImpl<ClienteResponse>(lista.stream().collect(Collectors.toList()).subList(inicio, fim), pageable, lista.size());
        return paginacao;
    }

    private Page<ClienteResponseV2> paginarListaCSV(List<ClienteResponseV2> lista, Pageable pageable){
        int inicio, fim;
        inicio = (int) pageable.getOffset();
        fim = (inicio + pageable.getPageSize()) > lista.size() ? lista.size() : (inicio + pageable.getPageSize());
        Page<ClienteResponseV2> paginacao = new PageImpl<ClienteResponseV2>(lista.stream().collect(Collectors.toList()).subList(inicio, fim), pageable, lista.size());
        return paginacao;
    }

    public Flux<PageImpl<ClienteResponseV2>> paginarListaV2(Flux<ClienteResponseV2> flux, Pageable pageable) {
        return flux.collectList()
                .map(lista -> {
                    int total = lista.size();
                    int from = (int) pageable.getOffset();
                    int to = Math.min(from + pageable.getPageSize(), total);

                    List<ClienteResponseV2> sublist = lista.subList(from, to);
                    return new PageImpl<>(sublist, pageable, total);
                })
                .flux();
    }

    private JsonNode parseJson(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonNode extrairNodeClientes(JsonNode jsonNode) {
        return jsonNode.get("record");
    }

    private ClienteResponseWrapperV2 mapearParaListaDeClientes(JsonNode clientesNode) {
        try {
            return objectMapper.readValue(clientesNode.traverse(), new TypeReference<ClienteResponseWrapperV2>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

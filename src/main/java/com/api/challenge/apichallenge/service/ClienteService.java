package com.api.challenge.apichallenge.service;

import com.api.challenge.apichallenge.config.HeadersDefault;
import com.api.challenge.apichallenge.dto.v1.ClienteResponseWrapperDTO;
import com.api.challenge.apichallenge.response.v1.ClienteWrapper;
import com.api.challenge.apichallenge.response.v2.ClienteWrapperV2;
import com.api.challenge.apichallenge.pagination.CustomPageImpl;
import com.api.challenge.apichallenge.pagination.CustomPageable;
import com.api.challenge.apichallenge.request.ClienteRequest;
import com.api.challenge.apichallenge.response.NewClienteWrapper;
import com.api.challenge.apichallenge.response.MetaData;
import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.util.csv.ClienteCSVHandler;
import com.api.challenge.apichallenge.util.dateutil.AniversarioParaDNConversor;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.util.jsonparser.ClienteJsonParser;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
// NAO TA PAGINADO O GET V2
// TEM Q ARRUMAR O FILTRO
// NAO DEU PRA USAR DAQUELA FORMA O JSONPARSER
@Service
public class ClienteService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WebClient client;
    @Autowired
    ClienteCSVHandler clienteCSVHandler;
    @Autowired
    ClienteJsonParser clienteJsonParser;

    public ClienteRequest escreverNovaLinhaCSV(ClienteRequest clienteRequest) throws IOException {
        return clienteCSVHandler.writeNewLine(clienteRequest);
    }

    public ClienteRequest updateCSV(ClienteRequest cliente) throws IOException {
        return clienteCSVHandler.updateCSV(cliente);
    }

    public void deleteCSVFile(Integer id) throws IOException {
        clienteCSVHandler.deleteCSVLine(id);
    }

    public Page<ClienteResponseV2> readCSV(CustomPageable pageable) throws FileNotFoundException {
        return paginarListaV2(clienteCSVHandler.read(), pageable);
    }
    // POST
    @SuppressWarnings("unchecked")
    @JsonProperty("brand")
    public Flux<Object> postCSV() {
        return client.get()
                .uri("/b/63fa39efc0e7653a057e6fa7")
                .retrieve()
                .bodyToFlux(String.class)
                .map(ClienteJsonParser::pegarJsonNode)
                .map(ClienteJsonParser::extrairNodeClientes)
                .map(ClienteJsonParser::mapearParaListaDeClientes)
                .flatMap(clientes -> {

                    Flux<ClienteResponseV2> flux = Flux.fromIterable(clientes.getClienteResponses())
                            .map(AniversarioParaDNConversor::formatarAniversarioParaDataNascimento)
                            .sort(Comparator.comparing(ClienteResponseV2::getNome))
                            .zipWith(Flux.range(1, clientes.getClienteResponses().size()),
                                    (clienteResponse, id) -> {
                                        clienteResponse.setId(id);
                                        return clienteResponse;
                                    }).doOnNext(client -> {
                                try {
                                    clienteCSVHandler.consumesApiToCSV(client);
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
    public ClienteWrapper getClientes(CustomPageable pageable) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity entity = new HttpEntity(null, headers);
        headers.set(HeadersDefault.AUTHORIZATION_KEY, HeadersDefault.AUTHORIZATION_VALUE);
        ResponseEntity<String> clientesJson = restTemplate.exchange("https://api.jsonbin.io/v3/b/63fa39efc0e7653a057e6fa7", HttpMethod.GET, entity, String.class);

        String JSON_SOURCE = clientesJson.getBody();
        JsonNode jsonNode = objectMapper.readTree(JSON_SOURCE);
        JsonNode clientesNode = jsonNode.get("record");
        ClienteResponseWrapperDTO clientes = objectMapper.readValue(clientesNode.traverse(), new TypeReference<ClienteResponseWrapperDTO>(){});
        List<ClienteResponse> clienteList = clientes.getClientesResponseV2List().stream().sorted(Comparator.comparing(ClienteResponse::getNome)).collect(Collectors.toList());
        AniversarioParaDNConversor conversor = new AniversarioParaDNConversor();
        clienteList.forEach(element -> conversor.formatarAniversarioParaDataNascimento(element));
        ClienteWrapper clienteWrapper = new ClienteWrapper();
        clienteWrapper.setClienteResponses(paginarLista(clienteList, pageable));
        clienteWrapper.setMetaData(new MetaData(clienteList.size()));
        return clienteWrapper;
    }



    @SuppressWarnings("unchecked")
    @JsonProperty("brand")
    public Flux<ClienteWrapperV2> getClientesV2(CustomPageable pageable) {
        Flux<String> record = client.get()
                .uri("/b/63fa39efc0e7653a057e6fa7")
                .retrieve()
                .bodyToFlux(String.class);

        return record.map(ClienteJsonParser::pegarJsonNode)
                .map(ClienteJsonParser::extrairNodeClientes)
                .map(ClienteJsonParser::mapearParaListaDeClientes)
                .flatMap(clientes -> {
                    System.out.println(clientes.getClienteResponses());
                    Flux<ClienteResponseV2> flux = Flux.fromIterable(clientes.getClienteResponses())
                            .map(AniversarioParaDNConversor::formatarAniversarioParaDataNascimento)
                            .sort(Comparator.comparing(ClienteResponseV2::getNome))
                            .zipWith(Flux.range(1, clientes.getClienteResponses().size()),
                                    (clienteResponse, id) -> {
                                        clienteResponse.setId(id);
                                        return clienteResponse;
                                    });

                    return flux
                            .collectList()
                            .map(clientesPaginados -> {
                                Page<ClienteResponseV2> clienteResponseV2Page = paginarListaV2(clientesPaginados, pageable);
                                MetaData metaData = new MetaData(clientesPaginados.size());
                                ClienteWrapperV2 clienteWrapperV2 = new ClienteWrapperV2(clienteResponseV2Page, metaData);
                                clienteWrapperV2.setMetaData(new MetaData(clientes.getClienteResponses().size()));

                                return clienteWrapperV2;
                            });
                });
    }

    private Page<ClienteResponseV2> paginarListaV2(List<ClienteResponseV2> lista, CustomPageable pageable){
        int inicio, fim;
        inicio = (int) pageable.getOffset();
        fim = (inicio + pageable.getPageSize()) > lista.size() ? lista.size() : (inicio + pageable.getPageSize());
        Page<ClienteResponseV2> paginacao = new CustomPageImpl<>(lista.stream().collect(Collectors.toList()).subList(inicio, fim), pageable, lista.size());
        return paginacao;
    }

    private Page<ClienteResponse> paginarLista(List<ClienteResponse> lista, CustomPageable pageable){
        int inicio, fim;
        inicio = (int) pageable.getOffset();
        fim = (inicio + pageable.getPageSize()) > lista.size() ? lista.size() : (inicio + pageable.getPageSize());
        Page<ClienteResponse> paginacao = new CustomPageImpl<>(lista.stream().collect(Collectors.toList()).subList(inicio, fim), pageable, lista.size());
        return paginacao;
    }
}

package com.api.challenge.apichallenge.service;

import com.api.challenge.apichallenge.config.HeadersDefault;
import com.api.challenge.apichallenge.dao.ClienteDAO;
import com.api.challenge.apichallenge.dto.v1.ClienteResponseWrapperDTO;
import com.api.challenge.apichallenge.exception.ClienteInCSVNotFoundException;
import com.api.challenge.apichallenge.exception.InvalidDateOfBirth;
import com.api.challenge.apichallenge.exception.InvalidURIException;
import com.api.challenge.apichallenge.response.v1.ClienteWrapper;
import com.api.challenge.apichallenge.response.v2.ClienteWrapperV2;
import com.api.challenge.apichallenge.pagination.CustomPageImpl;
import com.api.challenge.apichallenge.pagination.CustomPageable;
import com.api.challenge.apichallenge.request.ClienteRequest;
import com.api.challenge.apichallenge.response.MetaData;
import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.util.csv.ClienteCSVHandler;
import com.api.challenge.apichallenge.util.dateutil.AniversarioParaDNConversor;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.util.jsonparser.ClienteJsonParser;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ClienteCSVHandler clienteCSVHandler;
    @Autowired
    ClienteJsonParser clienteJsonParser;
    @Autowired
    ClienteDAO clienteDAO;

    public ClienteRequest escreverNovaLinhaCSV(ClienteRequest clienteRequest) throws IOException, InvalidDateOfBirth {
        return clienteCSVHandler.writeNewLine(clienteRequest);
    }

    public ClienteRequest updateCSV(ClienteRequest cliente) throws IOException, ClienteInCSVNotFoundException, InvalidDateOfBirth {
        return clienteCSVHandler.updateCSV(cliente);
    }

    public void deleteCSVFile(Integer id) throws IOException, ClienteInCSVNotFoundException {
        clienteCSVHandler.deleteCSVLine(id);
    }

    public ClienteWrapperV2 readCSV(CustomPageable pageable) throws FileNotFoundException {
        return new ClienteWrapperV2(paginarListaV2(clienteCSVHandler.read(), pageable),
                new MetaData(clienteCSVHandler.read().size()));
    }
    // POST
    @SuppressWarnings("unchecked")
    @JsonProperty("brand")
    public Flux<ClienteResponseV2> postCSV() {
        return clienteDAO.postCSV()
                .map(ClienteJsonParser::pegarJsonNode)
                .map(ClienteJsonParser::extrairNodeClientes)
                .map(ClienteJsonParser::mapearParaClientesWrapperDTO)
                .flatMap(clientes -> Flux.fromIterable(clientes.getClientesResponseV2List())
                        .map(AniversarioParaDNConversor::formatarAniversarioParaDataNascimento)
                        .sort(Comparator.comparing(ClienteResponseV2::getNome))
                        .zipWith(Flux.range(1, clientes.getClientesResponseV2List().size()),
                                (clienteResponse, id) -> {
                                    clienteResponse.setId(id);
                                    return clienteResponse;
                                }).doOnNext(client1 -> {
                            try {
                                clienteCSVHandler.consumesApiToCSV(client1);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }));
    }
    // GET
    @SuppressWarnings("unchecked")
    @JsonProperty("brand")
    public ClienteWrapper getClientes(CustomPageable pageable) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(clienteDAO.getClientes());
        JsonNode clientesNode = jsonNode.get("record");
        ClienteResponseWrapperDTO clientes = objectMapper.readValue(clientesNode.traverse(), new TypeReference<ClienteResponseWrapperDTO>(){});
        List<ClienteResponse> clienteList = clientes.getClientesResponseV2List().stream().sorted(Comparator.comparing(ClienteResponse::getNome)).collect(Collectors.toList());
        clienteList.forEach(AniversarioParaDNConversor::formatarAniversarioParaDataNascimento);
        ClienteWrapper clienteWrapper = new ClienteWrapper();
        clienteWrapper.setClienteResponses(paginarLista(clienteList, pageable));
        clienteWrapper.setMetaData(new MetaData(clienteList.size()));
        return clienteWrapper;
    }



    @SuppressWarnings("unchecked")
    @JsonProperty("brand")
    public Flux<ClienteWrapperV2> getClientesV2(CustomPageable pageable) {

        return clienteDAO.getClientesV2().map(ClienteJsonParser::pegarJsonNode)
                .map(ClienteJsonParser::extrairNodeClientes)
                .map(ClienteJsonParser::mapearParaClientesWrapperDTO)
                .flatMap(clientes -> {
                    Flux<ClienteResponseV2> flux = Flux.fromIterable(clientes.getClientesResponseV2List())
                            .map(AniversarioParaDNConversor::formatarAniversarioParaDataNascimento)
                            .sort(Comparator.comparing(ClienteResponseV2::getNome))
                            .zipWith(Flux.range(1, clientes.getClientesResponseV2List().size()),
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
                                clienteWrapperV2.setMetaData(new MetaData(clientes.getClientesResponseV2List().size()));

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
        return new CustomPageImpl<>(lista.stream().collect(Collectors.toList()).subList(inicio, fim), pageable, lista.size());
    }
}

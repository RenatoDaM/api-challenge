package com.api.challenge.apichallenge.service;

import com.api.challenge.apichallenge.dao.ClienteDAO;
import com.api.challenge.apichallenge.dto.v1.ClienteResponseWrapperDTO;
import com.api.challenge.apichallenge.dto.v2.ClienteResponseWrapperDTOV2;
import com.api.challenge.apichallenge.exception.ClienteInCSVNotFoundException;
import com.api.challenge.apichallenge.exception.InvalidDateOfBirth;
import com.api.challenge.apichallenge.exception.MissingClienteParametersException;
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

    public ClienteRequest escreverNovaLinhaCSV(ClienteRequest clienteRequest) throws IOException, InvalidDateOfBirth, MissingClienteParametersException {
        if (clienteRequest.getDataNascimento() == null || clienteRequest.getIdade() == null || clienteRequest.getSexo() == null || clienteRequest.getNome() == null) {
            throw new MissingClienteParametersException("Parâmetro(s) obrigatório(s) não preenchido(s). Favor preencher corretamente.");
        }
        return clienteCSVHandler.writeNewLine(clienteRequest);
    }

    public ClienteRequest updateCSV(ClienteRequest clienteRequest) throws IOException, ClienteInCSVNotFoundException, InvalidDateOfBirth, MissingClienteParametersException {
        if (clienteRequest.getDataNascimento() == null || clienteRequest.getIdade() == null || clienteRequest.getSexo() == null || clienteRequest.getNome() == null) {
            throw new MissingClienteParametersException("Parâmetro(s) obrigatório(s) não preenchido(s). Favor preencher corretamente.");
        }
        return clienteCSVHandler.updateCSV(clienteRequest);
    }

    public void deleteCSVFile(Integer id) throws IOException, ClienteInCSVNotFoundException {
        clienteCSVHandler.deleteCSVLine(id);
    }

    public ClienteResponseWrapperDTOV2 readCSV() throws FileNotFoundException {
        return new ClienteResponseWrapperDTOV2(clienteCSVHandler.read(), new MetaData(clienteCSVHandler.read().size()));
    }
    // POST
    @SuppressWarnings("unchecked")
    @JsonProperty("brand")
    public Flux<ClienteResponseV2> criarArquivoCSV() {
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
    public Flux<List<ClienteResponseV2>> getClientesV2() {

        return clienteDAO.getClientesV2()
                .map(ClienteJsonParser::pegarJsonNode)
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

                    return flux.collectList();
                });
    }

    private Page<ClienteResponse> paginarLista(List<ClienteResponse> lista, CustomPageable pageable){
        int inicio, fim;
        inicio = (int) pageable.getOffset();
        fim = (inicio + pageable.getPageSize()) > lista.size() ? lista.size() : (inicio + pageable.getPageSize());
        return new CustomPageImpl<>(lista.stream().collect(Collectors.toList()).subList(inicio, fim), pageable, lista.size());
    }
}

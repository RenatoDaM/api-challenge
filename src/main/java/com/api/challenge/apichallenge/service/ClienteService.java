package com.api.challenge.apichallenge.service;

import com.api.challenge.apichallenge.dao.ClienteDAO;
import com.api.challenge.apichallenge.dto.v1.ClienteResponseWrapperDTO;
import com.api.challenge.apichallenge.exception.ClienteInCSVNotFoundException;
import com.api.challenge.apichallenge.exception.CorruptedDataOnCSVFileException;
import com.api.challenge.apichallenge.exception.InvalidDateOfBirth;
import com.api.challenge.apichallenge.exception.MissingClienteParametersException;
import com.api.challenge.apichallenge.response.v1.ClienteWrapper;
import com.api.challenge.apichallenge.pagination.CustomPageImpl;
import com.api.challenge.apichallenge.pagination.CustomPageable;
import com.api.challenge.apichallenge.request.ClienteRequest;
import com.api.challenge.apichallenge.response.MetaData;
import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v2.ClienteWrapperV2;
import com.api.challenge.apichallenge.search.ClienteRequestParam;
import com.api.challenge.apichallenge.search.filter.ClienteFilter;
import com.api.challenge.apichallenge.util.csv.ClienteCSVHandler;
import com.api.challenge.apichallenge.util.dateutil.BirthdayToDateOfBirth;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.util.jsonparser.ClienteJsonParser;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
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
    ObjectMapper objectMapper;
    @Autowired
    ClienteCSVHandler clienteCSVHandler;
    @Autowired
    ClienteJsonParser clienteJsonParser;
    @Autowired
    ClienteDAO clienteDAO;

    public ClienteRequest escreverNovaLinhaCSV(ClienteRequest clienteRequest) throws IOException, InvalidDateOfBirth, MissingClienteParametersException, CsvException, CorruptedDataOnCSVFileException {
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

    public void deleteCSVFile(Integer id) throws IOException, ClienteInCSVNotFoundException, CsvException, CorruptedDataOnCSVFileException {
        clienteCSVHandler.deleteCSVLine(id);
    }

    public ClienteWrapperV2 readCSV(ClienteRequestParam clienteRequestParam, CustomPageable customPageable) throws IOException, CsvException, CorruptedDataOnCSVFileException {
        List<ClienteResponseV2> clienteResponseV2List = ClienteFilter.filterClienteCSV(clienteCSVHandler.read(), clienteRequestParam);
        Page<ClienteResponseV2> clienteResponseV2Page = paginarCSV(clienteResponseV2List, customPageable);
        return new ClienteWrapperV2(clienteResponseV2Page, new MetaData(clienteResponseV2List.size()));
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
                        .map(BirthdayToDateOfBirth::convertBirthdayToDateOfBirthV2)
                        .sort(Comparator.comparing(ClienteResponseV2::getNome))
                        .zipWith(Flux.range(1, clientes.getClientesResponseV2List().size()),
                                (clienteResponse, id) -> {
                                    clienteResponse.setId(id);
                                    return clienteResponse;
                                }).doOnNext(client1 -> {
                            try {
                                clienteCSVHandler.consumesApiToCSV(client1);
                                System.out.println(client1.getNome());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }));
    }
    // GET
    @SuppressWarnings("unchecked")
    @JsonProperty("brand")
    public ClienteWrapper getClientes(ClienteRequestParam clienteRequestParam, CustomPageable pageable) throws IOException {
        JsonNode jsonNode = objectMapper.readTree(clienteDAO.getClientes());
        JsonNode clientesNode = jsonNode.get("record");
        ClienteResponseWrapperDTO clientes = objectMapper.readValue(clientesNode.traverse(), new TypeReference<ClienteResponseWrapperDTO>(){});
        List<ClienteResponse> clienteList = clientes.getClientesResponseV2List().stream().sorted(Comparator.comparing(ClienteResponse::getNome)).collect(Collectors.toList());
        clienteList.forEach(BirthdayToDateOfBirth::convertBirthdayToDateOfBirth);
        List<ClienteResponse> filteredList = ClienteFilter.filterCliente(clienteList, clienteRequestParam);
        ClienteWrapper clienteWrapper = new ClienteWrapper(paginarLista(filteredList, pageable), new MetaData(filteredList.size()));
        return clienteWrapper;
    }



    @SuppressWarnings("unchecked")
    @JsonProperty("brand")
    public Flux<ClienteWrapperV2> getClientesV2(ClienteRequestParam clienteRequestParam, CustomPageable customPageable) {
            // ESSE CÓDIGO PRECISA SER MELHORADO. ESTÁ EXTREMAMENTE CONFUSO REALIZANDO OPERAÇOES ESTRANHAS
            // E DESNECESSÁRIAS
        return clienteDAO.getClientesV2()
                .map(ClienteJsonParser::pegarJsonNode)
                .map(ClienteJsonParser::extrairNodeClientes)
                .map(ClienteJsonParser::mapearParaClientesWrapperDTO)
                .flatMap(clientes -> {
                    Flux<ClienteResponseV2> flux = Flux.fromIterable(clientes.getClientesResponseV2List())
                            .sort(Comparator.comparing(ClienteResponseV2::getNome))
                            .zipWith(Flux.range(1, clientes.getClientesResponseV2List().size()),
                                    (clienteResponse, id) -> {
                                        clienteResponse.setId(id);
                                        return clienteResponse;
                                    });

                    Flux<ClienteResponseV2> sortedFlux = flux.zipWith(Flux.range(1, clientes.getClientesResponseV2List().size()),
                            (clienteResponse, id) -> {
                                clienteResponse.setId(id);
                                return clienteResponse;
                            });

                    Flux<ClienteResponseV2> fluxClientesComDataDeNascimento = sortedFlux
                            .map(BirthdayToDateOfBirth::convertBirthdayToDateOfBirthV2);

                    Mono<Integer> sizeMono = ClienteFilter.filterClienteV2(sortedFlux.collectList().flux(), clienteRequestParam).reduce(0, (total, list) -> total + list.size());

                    return sizeMono.flux().flatMap(size -> {
                        return paginarFluxoDeLista(ClienteFilter.filterClienteV2(fluxClientesComDataDeNascimento.collectList().flux(), clienteRequestParam), customPageable)
                                .map(pagina -> new ClienteWrapperV2(pagina, new MetaData(size)));
                    });
                });
    }

    private Page<ClienteResponse> paginarLista(List<ClienteResponse> lista, CustomPageable pageable){
        int inicio, fim;
        inicio = (int) pageable.getOffset();
        fim = (inicio + pageable.getPageSize()) > lista.size() ? lista.size() : (inicio + pageable.getPageSize());
        return new CustomPageImpl<>(lista.stream().collect(Collectors.toList()).subList(inicio, fim), pageable, lista.size());
    }

    private static Flux<Page<ClienteResponseV2>> paginarFluxoDeLista(Flux<List<ClienteResponseV2>> lista, CustomPageable pageable) {
        int inicio = (int) pageable.getOffset();
        int fim = Math.min(inicio + pageable.getPageSize(), Integer.MAX_VALUE);

        return lista
                .concatMap(Flux::fromIterable)
                .buffer(fim)
                .map(elementos -> {
                    int total = elementos.size();
                    List<ClienteResponseV2> pagina = elementos.subList(inicio, Math.min(total, inicio + pageable.getPageSize()));
                    return new CustomPageImpl<>(pagina, pageable, total);
                });
    }

    public Page<ClienteResponseV2> paginarCSV(List<ClienteResponseV2> clienteResponseV2List, CustomPageable customPageable) {
        int inicio, fim;
        inicio = (int) customPageable.getOffset();
        fim = (inicio + customPageable.getPageSize()) > clienteResponseV2List.size() ? clienteResponseV2List.size() : (inicio + customPageable.getPageSize());
        return new CustomPageImpl<>(clienteResponseV2List.stream().collect(Collectors.toList()).subList(inicio, fim), customPageable, clienteResponseV2List.size());
    }
}

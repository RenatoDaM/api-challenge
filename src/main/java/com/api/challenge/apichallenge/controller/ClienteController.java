package com.api.challenge.apichallenge.controller;

import com.api.challenge.apichallenge.exception.ClienteInCSVNotFoundException;
import com.api.challenge.apichallenge.exception.CorruptedDataOnCSVFileException;
import com.api.challenge.apichallenge.exception.InvalidCsvParams;
import com.api.challenge.apichallenge.exception.MissingClienteParametersException;
import com.api.challenge.apichallenge.response.Response;
import com.api.challenge.apichallenge.response.v1.ClienteWrapper;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.response.v2.ClienteWrapperV2;
import com.api.challenge.apichallenge.pagination.CustomPageable;
import com.api.challenge.apichallenge.controller.openapi.ClienteOpenApiImpl;
import com.api.challenge.apichallenge.request.ClienteRequest;
import com.api.challenge.apichallenge.search.ClienteRequestParam;
import com.api.challenge.apichallenge.service.ClienteService;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("api-challenge/cliente")
@Validated
public class ClienteController implements ClienteOpenApiImpl {
    @Autowired
    ClienteService clienteService;

    @RequestMapping(value = "/v1/buscarClientes", method = RequestMethod.GET)
    public ResponseEntity<ClienteWrapper> getClientes(
            @PageableDefault(size = 10, page = 0) CustomPageable customPageable,
            @RequestParam(value = "idade_min", required = false) Integer idadeMin,
            @RequestParam(value = "idade_max", required = false) Integer idadeMax,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "mes", required = false) String mes,
            @RequestParam(value = "dia", required = false) String dia,
            @RequestParam(value = "data_nasc_min", required = false) String dataNascMin,
            @RequestParam(value = "data_nasc_max", required = false) String dataNascMax) throws IOException {
        ClienteRequestParam clienteRequestParam = new ClienteRequestParam(idadeMin, idadeMax, sexo, mes, dia, dataNascMin, dataNascMax);
        return ResponseEntity.ok().body(clienteService.getClientes(clienteRequestParam, customPageable));
    }

    @RequestMapping(value = "/v2/buscarClientes", method = RequestMethod.GET)
    public ResponseEntity<Flux<ClienteWrapperV2>> getClientesV2(
            @PageableDefault(size = 10, page = 0) CustomPageable customPageable,
            @RequestParam(value = "idade_min", required = false) Integer idadeMin,
            @RequestParam(value = "idade_max", required = false) Integer idadeMax,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "mes", required = false) String mes,
            @RequestParam(value = "dia", required = false) String dia,
            @RequestParam(value = "data_nasc_min", required = false) String dataNascMin,
            @RequestParam(value = "data_nasc_max", required = false) String dataNascMax) {
        ClienteRequestParam clienteRequestParam = new ClienteRequestParam(idadeMin, idadeMax, sexo, mes, dia, dataNascMin, dataNascMax);
        return ResponseEntity.ok().body(clienteService.getClientesV2(clienteRequestParam, customPageable));
    }

    @PostMapping("/v2/criarCSV")
    public ResponseEntity<Flux<ClienteResponseV2>> postarCSV() throws IOException, CorruptedDataOnCSVFileException, CsvException {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.criarArquivoCSV());
    }

    @PostMapping("/v2/adicionarPessoaCSV")
    public ResponseEntity<ClienteRequest> adicionarPessoaCSV(@Valid @RequestBody ClienteRequest clienteRequest) throws IOException, InvalidCsvParams, MissingClienteParametersException, CsvException, CorruptedDataOnCSVFileException {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.escreverNovaLinhaCSV(clienteRequest));
    }

    @GetMapping("/v2/lerCSV")
    public ResponseEntity<ClienteWrapperV2> lerCSV(
            @PageableDefault(page = 0, size = 10) CustomPageable pageable,
            @RequestParam(value = "idade_min", required = false) Integer idadeMin,
            @RequestParam(value = "idade_max", required = false) Integer idadeMax,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "mes", required = false) String mes,
            @RequestParam(value = "dia", required = false) String dia,
            @RequestParam(value = "data_nasc_min", required = false) String dataNascMin,
            @RequestParam(value = "data_nasc_max", required = false) String dataNascMax) throws IOException, CsvException, CorruptedDataOnCSVFileException {
        ClienteRequestParam clienteRequestParam = new ClienteRequestParam(idadeMin, idadeMax, sexo, mes, dia, dataNascMin, dataNascMax);
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.readCSV(clienteRequestParam, pageable));
    }

    @PutMapping("/v2/atualizarCSV")
    public ResponseEntity<ClienteRequest> atualizarCSV(@Valid @RequestBody ClienteRequest clienteRequest) throws IOException, ClienteInCSVNotFoundException, InvalidCsvParams, MissingClienteParametersException {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.updateCSV(clienteRequest));
    }

    @DeleteMapping("/v2/deletarPessoaCSVPeloId/{id}")
    public ResponseEntity<Response> deleteCSVLineById(@PathVariable Integer id) throws IOException, ClienteInCSVNotFoundException, CsvException, CorruptedDataOnCSVFileException {
        clienteService.deleteCSVFile(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response(204, "Delatado com sucesso"));
    }










}

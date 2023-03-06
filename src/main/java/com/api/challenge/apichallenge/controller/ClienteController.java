package com.api.challenge.apichallenge.controller;

import com.api.challenge.apichallenge.exception.ClienteInCSVNotFoundException;
import com.api.challenge.apichallenge.response.Response;
import com.api.challenge.apichallenge.response.v1.ClienteWrapper;
import com.api.challenge.apichallenge.response.v2.ClienteWrapperV2;
import com.api.challenge.apichallenge.pagination.CustomPageable;
import com.api.challenge.apichallenge.controller.openapi.ClienteOpenApiImpl;
import com.api.challenge.apichallenge.request.ClienteRequest;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import java.io.FileNotFoundException;
import java.io.IOException;
import static com.api.challenge.apichallenge.search.ClienteFilter.*;

@RestController
@RequestMapping("api-challenge/cliente")
public class ClienteController implements ClienteOpenApiImpl {
    @Autowired
    ClienteService clienteService;

    @RequestMapping("/v1/buscarClientes")
    public ResponseEntity<ClienteWrapper> getClientes(
            @PageableDefault(size = 10) CustomPageable customPageable,
            @RequestParam(value = "idade", required = false) Integer idade,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "aniversario", required = false) String aniversario) throws IOException {

        return ResponseEntity.ok().body(filterCliente(clienteService.getClientes(customPageable), idade, sexo, aniversario, customPageable));
    }

    @RequestMapping("/v2/buscarClientes")
    public ResponseEntity<Flux<ClienteWrapperV2>> getClientesV2(
            @PageableDefault(size = 10, page = 0) CustomPageable customPageable,
            @RequestParam(value = "idade_min", required = false) Integer idadeMin,
            @RequestParam(value = "idade_max", required = false) Integer idadeMax,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "mes", required = false) String mes,
            @RequestParam(value = "dia", required = false) String dia,
            @RequestParam(value = "data_nasc_min", required = false) String dataNascMin,
            @RequestParam(value = "data_nasc_max", required = false) String dataNascMax) {
        return ResponseEntity.ok().body(filterClienteV2(clienteService.getClientesV2(customPageable), idadeMin, idadeMax, sexo, dataNascMin, dataNascMax, mes, dia, customPageable));
    }

    @PostMapping("/v2/criarCSV")
    public ResponseEntity<Flux<ClienteResponseV2>> postarCSV() {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.postCSV());
    }
    // NECESSÁRIO USO DE REQUEST POR CONTA DA FORMA QUE IMPLEMENTEI O CONVERSOR DE ANIVERSÁRIO PARA DATA DE
    // NASCIMENTO. O setDataNascimento usa a propriedade json aniversario, ja o get usa o próprio atributo
    // dataNascimento.
    @PostMapping("/v2/adicionarPessoaCSV")
    public ResponseEntity<ClienteRequest> adicionarPessoaCSV(@RequestBody ClienteRequest clienteRequest) throws IOException {
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
            @RequestParam(value = "data_nasc_max", required = false) String dataNascMax) throws  FileNotFoundException {

        ClienteWrapperV2 clienteResponseV2s = filterClienteCSV(clienteService.readCSV(pageable), idadeMin, idadeMax, sexo, dataNascMin, dataNascMax, mes, dia, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(clienteResponseV2s);
    }

    @PutMapping("/v2/atualizarCSV")
    public ResponseEntity<ClienteRequest> atualizarCSV(@RequestBody ClienteRequest clienteRequest) throws IOException, ClienteInCSVNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.updateCSV(clienteRequest));
    }

    @DeleteMapping("/v2/deletarCSVLine/{id}")
    public ResponseEntity<Response> deleteCSVFile(@PathVariable Integer id) throws IOException, ClienteInCSVNotFoundException {
        clienteService.deleteCSVFile(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Response(204, "Deleted successfully"));
    }










}

package com.api.challenge.apichallenge.controller;

import com.api.challenge.apichallenge.filter.DataNascimentoFilter;
import com.api.challenge.apichallenge.filter.IdadeFilter;
import com.api.challenge.apichallenge.request.ClienteRequest;
import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.api.challenge.apichallenge.filter.ClienteFilter.*;

@RestController
@RequestMapping("api-challenge/cliente")
public class ClienteController {
    @Autowired
    ClienteService clienteService;

    @DeleteMapping("/v2/deletarCSVLine/{id}")
    public ResponseEntity deleteCSVFile(@PathVariable Integer id) throws IOException {
        clienteService.deleteCSVFile(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted");
    }

    @PutMapping("/v2/atualizarCSV")
    public ResponseEntity<ClienteRequest> atualizarCSV(@RequestBody ClienteRequest clienteRequest) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.updateCSV(clienteRequest));
    }

    @GetMapping("/v2/lerCSV")
    public ResponseEntity<Page<ClienteResponseV2>> lerCSV(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(value = "idade_min", required = false) Integer idadeMin,
            @RequestParam(value = "idade_max", required = false) Integer idadeMax,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "mes", required = false) String mes,
            @RequestParam(value = "dia", required = false) String dia,
            @RequestParam(value = "data_nasc_min", required = false) String dataNascMin,
            @RequestParam(value = "data_nasc_max", required = false) String dataNascMax) throws  FileNotFoundException {
        Page<ClienteResponseV2> clienteResponseV2s = filterClienteCSV(clienteService.readCSV(pageable), idadeMin, idadeMax, sexo, dataNascMin, dataNascMax, mes, dia);
        return ResponseEntity.status(HttpStatus.OK).body(clienteResponseV2s);
    }

    @PostMapping("/v2/criarCSV")
    public ResponseEntity<Flux<Object>> postarCSV() {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.postCSV());
    }

    @RequestMapping("/v1/buscarClientes")
    public ResponseEntity<Page<ClienteResponse>> getClientes(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(value = "idade", required = false) Integer idade,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "aniversario", required = false) String aniversario) throws IOException {

        //FiltroCliente
        return ResponseEntity.ok().body(filterCliente(clienteService.getClientes(pageable), idade, sexo, aniversario));
    }

    @RequestMapping("/v2/buscarClientes")
    public ResponseEntity<Flux<Page<ClienteResponseV2>>> getClientesV2(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(value = "idade_min", required = false) Integer idadeMin,
            @RequestParam(value = "idade_max", required = false) Integer idadeMax,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "mes", required = false) String mes,
            @RequestParam(value = "dia", required = false) String dia,
            @RequestParam(value = "data_nasc_min", required = false) String dataNascMin,
            @RequestParam(value = "data_nasc_max", required = false) String dataNascMax) throws IOException {

        Flux<Page<ClienteResponseV2>> clientesFlux = filterClienteV2(clienteService.getClientesV2(pageable), idadeMin, idadeMax, sexo, dataNascMin, dataNascMax, mes, dia);
        return ResponseEntity.ok().body(clientesFlux);
    }
}

package com.api.challenge.apichallenge.controller;

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

    @PutMapping("/v2/atualizarCSV")
    public ResponseEntity<ClienteRequest> atualizarCSV(@RequestBody ClienteRequest clienteRequest) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.updateCSV(clienteRequest));
    }

    @GetMapping("/v2/lerCSV")
    public ResponseEntity<Page<ClienteResponseV2>> lerCSV(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(value = "idade", required = false) Integer idade,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "aniversario", required = false) String aniversario) throws  FileNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(filterClienteCSV(clienteService.readCSV(pageable), idade, sexo, aniversario));
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
            @RequestParam(value = "idade", required = false) Integer idade,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "aniversario", required = false) String aniversario) throws IOException {

        Flux<Page<ClienteResponseV2>> clientesFlux = filterClienteV2(clienteService.getClientesV2(pageable), idade, sexo, aniversario);
        return ResponseEntity.ok().body(clientesFlux);
    }
}

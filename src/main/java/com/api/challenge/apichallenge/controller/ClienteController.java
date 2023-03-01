package com.api.challenge.apichallenge.controller;

import static com.api.challenge.apichallenge.filter.ClienteFilter.filterCliente;
import static com.api.challenge.apichallenge.filter.ClienteFilter.filterClienteV2;

import com.api.challenge.apichallenge.response.Response;
import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("api-challenge/cliente")
public class ClienteController {
    @Autowired
    ClienteService clienteService;

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

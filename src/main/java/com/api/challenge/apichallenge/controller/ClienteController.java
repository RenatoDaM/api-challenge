package com.api.challenge.apichallenge.controller;

import static com.api.challenge.apichallenge.filter.ClienteFilter.filterCliente;
import static com.api.challenge.apichallenge.filter.ClienteFilter.filterClienteV2;
import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("api-challenge/cliente")
public class ClienteController {
    @Autowired
    ClienteService clienteService;

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
    public Mono<ResponseEntity<Page<ClienteResponseV2>>> getClientesV2(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(value = "idade", required = false) Integer idade,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "aniversario", required = false) String aniversario) throws IOException {

        //FiltroCliente
        return filterClienteV2(clienteService.getClientesV2(pageable), idade, sexo, aniversario)
                .map(clientes -> ResponseEntity.ok(clientes))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

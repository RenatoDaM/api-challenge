package com.api.challenge.apichallenge.controller;

import static com.api.challenge.apichallenge.filter.ClienteFilter.filterCliente;
import com.api.challenge.apichallenge.response.ClienteResponse;
import com.api.challenge.apichallenge.service.ClienteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api-challenge/cliente/v1")
public class ClienteController {
    @Autowired
    ClienteService clienteService;

    @RequestMapping("/buscarClientes")
    public Page<ClienteResponse> getClientes(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(value = "idade", required = false) Integer idade,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "aniversario", required = false) String aniversario) throws IOException {

        //FiltroCliente
        return filterCliente(clienteService.getClientes(pageable), idade, sexo, aniversario);
    }
}

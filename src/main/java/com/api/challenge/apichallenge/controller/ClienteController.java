package com.api.challenge.apichallenge.controller;

import com.api.challenge.apichallenge.filter.ClienteFilter;
import com.api.challenge.apichallenge.model.Cliente;
import com.api.challenge.apichallenge.service.ClienteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api-challenge/cliente/v1")
public class ClienteController {
    @Autowired
    ClienteService clienteService;

    @RequestMapping("/buscarClientes")
    public Page<Cliente> getClientes(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(value = "idade", required = false) Integer idade,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "aniversario", required = false) String aniversario) throws JsonProcessingException {

        //FiltroCliente
        return ClienteFilter.filterCliente(clienteService.getClientes(pageable), idade, sexo, aniversario);
    }
}

package com.api.challenge.apichallenge.filter;

import com.api.challenge.apichallenge.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteFilter {
    public static Page<Cliente> filterCliente(Page<Cliente> clientes, Integer idade, String sexo, String aniversario) {
        if (sexo == null && idade == null && aniversario == null) {
            return clientes;
        }

        List<Cliente> newClienteList = new ArrayList<>();

        if (idade != null) {
            newClienteList = clientes.stream().filter(cliente -> cliente.getIdade() == idade).collect(Collectors.toList());
        }
        if (sexo != null) {
            newClienteList = newClienteList.stream().filter(cliente -> cliente.getSexo() == sexo).collect(Collectors.toList());
        }
        if (aniversario != null) {
            newClienteList = newClienteList.stream().filter(cliente -> cliente.getAniversario() == aniversario).collect(Collectors.toList());
        }
        Page<Cliente> clientePage = new PageImpl<>(newClienteList);

        return  clientePage;
    }
}

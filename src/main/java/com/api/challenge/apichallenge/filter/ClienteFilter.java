package com.api.challenge.apichallenge.filter;

import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteFilter {
    public static Page<ClienteResponse> filterCliente(Page<ClienteResponse> clientes, Integer idade, String sexo, String aniversario) {
        if (sexo == null && idade == null && aniversario == null) {
            return clientes;
        }

        List<ClienteResponse> newClienteListResponse = new ArrayList<>();

        if (idade != null) {
            newClienteListResponse = clientes.stream().filter(cliente -> cliente.getIdade() == idade).collect(Collectors.toList());
        }
        if (sexo != null) {
            newClienteListResponse = newClienteListResponse.stream().filter(cliente -> cliente.getSexo() == sexo).collect(Collectors.toList());
        }
        if (aniversario != null) {
            newClienteListResponse = newClienteListResponse.stream().filter(cliente -> cliente.getDataNascimento().toString() == aniversario).collect(Collectors.toList());
        }
        Page<ClienteResponse> clientePage = new PageImpl<>(newClienteListResponse);

        return  clientePage;
    }

    public static Page<ClienteResponseV2> filterClienteV2(Page<ClienteResponseV2> clientes, Integer idade, String sexo, String aniversario) {
        if (sexo == null && idade == null && aniversario == null) {
            return clientes;
        }

        List<ClienteResponseV2> newClienteListResponse = new ArrayList<>();

        if (idade != null) {
            newClienteListResponse = clientes.stream().filter(cliente -> cliente.getIdade() == idade).collect(Collectors.toList());
        }
        if (sexo != null) {
            newClienteListResponse = newClienteListResponse.stream().filter(cliente -> cliente.getSexo() == sexo).collect(Collectors.toList());
        }
        if (aniversario != null) {
            newClienteListResponse = newClienteListResponse.stream().filter(cliente -> cliente.getDataNascimento().toString() == aniversario).collect(Collectors.toList());
        }
        Page<ClienteResponseV2> clientePage = new PageImpl<>(newClienteListResponse);

        return  clientePage;
    }
}

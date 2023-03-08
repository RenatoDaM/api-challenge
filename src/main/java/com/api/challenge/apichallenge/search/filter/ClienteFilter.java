package com.api.challenge.apichallenge.search.filter;

import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.search.ClienteRequestParam;
import reactor.core.publisher.Flux;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ClienteFilter {
    public static List<ClienteResponseV2> filterClienteCSV(List<ClienteResponseV2> clienteResponseV2List, ClienteRequestParam clienteRequestParam) {

        List<ClienteResponseV2> newClienteListResponse = aplicarFiltrosV2(clienteResponseV2List, clienteRequestParam);
        return newClienteListResponse;
    }

    public static List<ClienteResponse> filterCliente(List<ClienteResponse> clienteResponseList, ClienteRequestParam clienteRequestParam) {

        List<ClienteResponse> newClienteListResponse = aplicarFiltros(clienteResponseList, clienteRequestParam);
        return newClienteListResponse;
    }

    public static Flux<List<ClienteResponseV2>> filterClienteV2(Flux<List<ClienteResponseV2>> clientesFlux, ClienteRequestParam clienteRequestParam) {
        return clientesFlux.map(clientes -> {

            List<ClienteResponseV2> newClienteListResponse = aplicarFiltrosV2(clientes, clienteRequestParam);

            return newClienteListResponse;
        });
    }

    public static List<ClienteResponse> aplicarFiltros(List<ClienteResponse> clientes, ClienteRequestParam clienteRequestParam) {
        if (clienteRequestParam.getSexo() == null && clienteRequestParam.getIdadeMin() == null && clienteRequestParam.getIdadeMax() == null && clienteRequestParam.getDataNascMin() == null && clienteRequestParam.getDataNascMax() == null && clienteRequestParam.getMes() == null && clienteRequestParam.getDia() == null) {
            return clientes;
        }

        List<ClienteResponse> newClienteListResponse2 = clientes.stream()
                .filter(cliente -> clienteRequestParam.getIdadeMin() == null || cliente.getIdade() >= clienteRequestParam.getIdadeMin())
                .filter(cliente -> clienteRequestParam.getIdadeMax() == null || cliente.getIdade() <= clienteRequestParam.getIdadeMax())
                .filter(cliente -> clienteRequestParam.getSexo() == null || cliente.getSexo().equalsIgnoreCase(clienteRequestParam.getSexo()))
                .filter(cliente -> clienteRequestParam.getDataNascMin() == null || stringParaDataNascimento(cliente.getDataNascimento()).isAfter(stringParaDataNascimento(clienteRequestParam.getDataNascMin())))
                .filter(cliente -> clienteRequestParam.getDataNascMax() == null || stringParaDataNascimento(cliente.getDataNascimento()).isBefore(stringParaDataNascimento(clienteRequestParam.getDataNascMax())))
                .filter(cliente -> clienteRequestParam.getMes() == null || stringParaDataNascimento(cliente.getDataNascimento()).getMonthValue() == (Integer.valueOf(clienteRequestParam.getMes())))
                .filter(cliente -> clienteRequestParam.getDia() == null || stringParaDataNascimento(cliente.getDataNascimento()).getDayOfMonth() == (Integer.valueOf(clienteRequestParam.getDia())))
                .collect(Collectors.toList());

        return newClienteListResponse2;
    }

    public static List<ClienteResponseV2> aplicarFiltrosV2(List<ClienteResponseV2> clientes, ClienteRequestParam clienteRequestParam) {
        if (clienteRequestParam.getSexo() == null && clienteRequestParam.getIdadeMin() == null && clienteRequestParam.getIdadeMax() == null && clienteRequestParam.getDataNascMin() == null && clienteRequestParam.getDataNascMax() == null && clienteRequestParam.getMes() == null && clienteRequestParam.getDia() == null) {
            return clientes;
        }

        List<ClienteResponseV2> newClienteListResponse2 = clientes.stream()
                .filter(cliente -> clienteRequestParam.getIdadeMin() == null || cliente.getIdade() >= clienteRequestParam.getIdadeMin())
                .filter(cliente -> clienteRequestParam.getIdadeMax() == null || cliente.getIdade() <= clienteRequestParam.getIdadeMax())
                .filter(cliente -> clienteRequestParam.getSexo() == null || cliente.getSexo().equalsIgnoreCase(clienteRequestParam.getSexo()))
                .filter(cliente -> clienteRequestParam.getDataNascMin() == null || stringParaDataNascimento(cliente.getDataNascimento()).isAfter(stringParaDataNascimento(clienteRequestParam.getDataNascMin())))
                .filter(cliente -> clienteRequestParam.getDataNascMax() == null || stringParaDataNascimento(cliente.getDataNascimento()).isBefore(stringParaDataNascimento(clienteRequestParam.getDataNascMax())))
                .filter(cliente -> clienteRequestParam.getMes() == null || stringParaDataNascimento(cliente.getDataNascimento()).getMonthValue() == (Integer.valueOf(clienteRequestParam.getMes())))
                .filter(cliente -> clienteRequestParam.getDia() == null || stringParaDataNascimento(cliente.getDataNascimento()).getDayOfMonth() == (Integer.valueOf(clienteRequestParam.getDia())))
                .collect(Collectors.toList());
        return newClienteListResponse2;
    }

    public static LocalDate stringParaDataNascimento(String string) {
        Locale LOCALE_BRAZIL = new Locale("pt", "BR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", LOCALE_BRAZIL);
        LocalDate data = LocalDate.parse(string, formatter);
        return data;
    }
}

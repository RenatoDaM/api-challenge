package com.api.challenge.apichallenge.search.filter;

import com.api.challenge.apichallenge.response.v1.ClienteWrapper;
import com.api.challenge.apichallenge.response.v2.ClienteWrapperV2;
import com.api.challenge.apichallenge.pagination.CustomPageImpl;
import com.api.challenge.apichallenge.pagination.CustomPageable;
import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.response.MetaData;
import com.api.challenge.apichallenge.search.ClienteRequestParam;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Flux;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ClienteFilter {
    public static ClienteWrapperV2 filterClienteCSV(List<ClienteResponseV2> clienteResponseV2List, ClienteRequestParam clienteRequestParam, CustomPageable customPageable) {

        List<ClienteResponseV2> newClienteListResponse2 = aplicarFiltrosV2(clienteResponseV2List, clienteRequestParam);
        return new ClienteWrapperV2(paginarV2(newClienteListResponse2, customPageable), new MetaData(newClienteListResponse2.size()));
    }

    public static List<ClienteResponse> filterCliente(List<ClienteResponse> clienteResponseList, ClienteRequestParam clienteRequestParam) {

        List<ClienteResponse> newClienteListResponse = aplicarFiltros(clienteResponseList, clienteRequestParam);
        return newClienteListResponse;
    }

    public static Flux<ClienteWrapperV2> filterClienteV2(Flux<List<ClienteResponseV2>> clientesFlux, ClienteRequestParam clienteRequestParam, CustomPageable customPageable) {
        return clientesFlux.flatMap(clientes -> {

            List<ClienteResponseV2> newClienteListResponse = aplicarFiltrosV2(clientes, clienteRequestParam);
            MetaData metaData = new MetaData(newClienteListResponse.size());
            return Flux.just(new ClienteWrapperV2(paginarV2(newClienteListResponse, customPageable), metaData));
        });
    }

    public static LocalDate stringParaDataNascimento(String string) {
        Locale LOCALE_BRAZIL = new Locale("pt", "BR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", LOCALE_BRAZIL);
        LocalDate data = LocalDate.parse(string, formatter);
        return data;
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

    public static Page<ClienteResponse> paginar(List<ClienteResponse> lista, CustomPageable pageable){

        int inicio, fim;
        inicio = (int) pageable.getOffset();
        fim = (inicio + pageable.getPageSize()) > lista.size() ? lista.size() : (inicio + pageable.getPageSize());
        System.out.println(inicio);
        System.out.println(fim);
        System.out.println(lista.size());
        // if inicio > que fim, vc colocou pagina 2 sendo q so tem até 1

        Page<ClienteResponse> paginacao = new CustomPageImpl<>(lista.stream().collect(Collectors.toList()).subList(inicio, fim), pageable, lista.size());
        return paginacao;
    }

    public static Page<ClienteResponseV2> paginarV2(List<ClienteResponseV2> lista, CustomPageable pageable){
        int inicio, fim;
        inicio = (int) pageable.getOffset();
        fim = (inicio + pageable.getPageSize()) > lista.size() ? lista.size() : (inicio + pageable.getPageSize());
        System.out.println(inicio);
        System.out.println(fim);
        System.out.println(lista.size());
        // if inicio > que fim, vc colocou pagina 2 sendo q so tem até 1

        Page<ClienteResponseV2> paginacao = new CustomPageImpl<>(lista.stream().collect(Collectors.toList()).subList(inicio, fim), pageable, lista.size());
        return paginacao;
    }
}

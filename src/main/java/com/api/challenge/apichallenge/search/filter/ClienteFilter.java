package com.api.challenge.apichallenge.search.filter;

import com.api.challenge.apichallenge.dto.v2.ClienteResponseWrapperDTOV2;
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

        List<ClienteResponseV2> newClienteListResponse2 = aplicarFiltros(clienteResponseV2List, clienteRequestParam);
        return new ClienteWrapperV2(paginarV2(newClienteListResponse2, customPageable), new MetaData(newClienteListResponse2.size()));
    }

    public static ClienteWrapper filterCliente(ClienteWrapper wrapper, Integer idade, String sexo, String aniversario, CustomPageable customPageable) {

        if (sexo == null && idade == null && aniversario == null) {
            return wrapper;
        }

        List<ClienteResponse> newClienteListResponse = new ArrayList<>();
        List<ClienteResponse> clientes = wrapper.getClienteResponses().toList();

        if (idade != null) {
            newClienteListResponse = clientes.stream().filter(cliente -> cliente.getIdade() == idade).collect(Collectors.toList());
        }
        if (sexo != null) {
            newClienteListResponse = clientes.stream().filter(cliente -> cliente.getSexo().equalsIgnoreCase(sexo)).collect(Collectors.toList());
        }
        if (aniversario != null) {
            newClienteListResponse = clientes.stream().filter(cliente -> cliente.getDataNascimento().equals(aniversario)).collect(Collectors.toList());
        }

        wrapper.setMetaData(new MetaData(newClienteListResponse.size()));
        wrapper.setClienteResponses(paginar(newClienteListResponse, customPageable));
        return  wrapper;
    }

    public static Flux<ClienteWrapperV2> filterClienteV2(Flux<List<ClienteResponseV2>> clientesFlux, ClienteRequestParam clienteRequestParam, CustomPageable customPageable) {
        return clientesFlux.flatMap(clientes -> {

            List<ClienteResponseV2> newClienteListResponse = aplicarFiltros(clientes, clienteRequestParam);

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

    public static List<ClienteResponseV2> aplicarFiltros(List<ClienteResponseV2> clientes, ClienteRequestParam clienteRequestParam) {
        if (clienteRequestParam.getSexo() == null && clienteRequestParam.getIdadeMin() == null && clienteRequestParam.getIdadeMax() == null && clienteRequestParam.getDataNascMin() == null && clienteRequestParam.getDataNascMax() == null && clienteRequestParam.getMes() == null && clienteRequestParam.getDia() == null) {
            return clientes;
        }

        List<ClienteResponseV2> newClienteListResponse2 = new ArrayList<>();

        if (clienteRequestParam.getIdadeMin() != null) {
            newClienteListResponse2.addAll(clientes.stream().filter(cliente -> cliente.getIdade() >= clienteRequestParam.getIdadeMin()).collect(Collectors.toList()));
        }

        if (clienteRequestParam.getIdadeMax() != null) {
            newClienteListResponse2.addAll(clientes.stream().filter(cliente -> cliente.getIdade() <= clienteRequestParam.getIdadeMax()).collect(Collectors.toList()));
        }

        if (clienteRequestParam.getSexo() != null) {
            newClienteListResponse2.addAll(clientes.stream().filter(cliente -> cliente.getSexo().equalsIgnoreCase(clienteRequestParam.getSexo())).collect(Collectors.toList()));
        }

        if (clienteRequestParam.getDataNascMin() != null && clienteRequestParam.getDataNascMax() == null) {
            newClienteListResponse2.addAll(clientes.stream().filter(cliente -> stringParaDataNascimento(cliente.getDataNascimento()).isAfter(stringParaDataNascimento(clienteRequestParam.getDataNascMin()))).collect(Collectors.toList()));
        }

        if (clienteRequestParam.getDataNascMax() != null && clienteRequestParam.getDataNascMin() == null) {
            newClienteListResponse2.addAll(clientes.stream().filter(cliente -> stringParaDataNascimento(cliente.getDataNascimento()).isBefore(stringParaDataNascimento(clienteRequestParam.getDataNascMax()))).collect(Collectors.toList()));
        }

        if (clienteRequestParam.getMes() != null) {
            newClienteListResponse2.addAll(clientes.stream().filter(cliente -> stringParaDataNascimento(cliente.getDataNascimento()).getMonthValue() == (Integer.valueOf(clienteRequestParam.getMes()))).collect(Collectors.toList()));
        }

        if (clienteRequestParam.getDia() != null) {
            newClienteListResponse2.addAll(clientes.stream().filter(cliente -> stringParaDataNascimento(cliente.getDataNascimento()).getDayOfMonth() == (Integer.valueOf(clienteRequestParam.getDia()))).collect(Collectors.toList()));
        }

        return newClienteListResponse2;
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
}

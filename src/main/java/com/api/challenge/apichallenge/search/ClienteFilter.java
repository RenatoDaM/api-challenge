package com.api.challenge.apichallenge.search;

import com.api.challenge.apichallenge.response.v1.ClienteWrapper;
import com.api.challenge.apichallenge.response.v2.ClienteWrapperV2;
import com.api.challenge.apichallenge.pagination.CustomPageImpl;
import com.api.challenge.apichallenge.pagination.CustomPageable;
import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.response.MetaData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ClienteFilter {
    public static ClienteWrapperV2 filterClienteCSV(ClienteWrapperV2 clientes, Integer idadeMin, Integer idadeMax, String sexo, String dataNascMin, String dataNascMax, String mes, String dia, CustomPageable customPageable) {

        if (sexo == null && idadeMin == null && idadeMax == null && dataNascMin == null && dataNascMax == null && mes == null && dia == null) {
            return clientes;
        }

        List<ClienteResponseV2> newClienteListResponse = clientes.getClienteResponses().toList();

        if (idadeMin != null) {
            newClienteListResponse = newClienteListResponse.stream().filter(cliente -> cliente.getIdade() >= idadeMin).collect(Collectors.toList());
        }

        if (idadeMax != null) {
            newClienteListResponse = newClienteListResponse.stream().filter(cliente -> cliente.getIdade() <= idadeMax).collect(Collectors.toList());
        }

        if (sexo != null) {
            newClienteListResponse = newClienteListResponse.stream().filter(cliente -> cliente.getSexo().equalsIgnoreCase(sexo)).collect(Collectors.toList());
        }

        if (dataNascMin != null && dataNascMax == null) {
            newClienteListResponse = newClienteListResponse.stream().filter(cliente -> stringParaDataNascimento(cliente.getDataNascimento()).isAfter(stringParaDataNascimento(dataNascMin))).collect(Collectors.toList());
        }

        if (dataNascMax != null && dataNascMin == null) {
            newClienteListResponse = newClienteListResponse.stream().filter(cliente -> stringParaDataNascimento(cliente.getDataNascimento()).isBefore(stringParaDataNascimento(dataNascMax))).collect(Collectors.toList());
        }

        if (mes != null) {
            newClienteListResponse = newClienteListResponse.stream().filter(cliente -> stringParaDataNascimento(cliente.getDataNascimento()).getMonthValue() == (Integer.valueOf(mes))).collect(Collectors.toList());
            System.out.println("mes" + mes);
        }

        if (dia != null) {
            newClienteListResponse = newClienteListResponse.stream().filter(cliente -> stringParaDataNascimento(cliente.getDataNascimento()).getDayOfMonth() == (Integer.valueOf(dia))).collect(Collectors.toList());
            System.out.println("dia" + dia);
        }

        clientes.setMetaData(new MetaData(newClienteListResponse.size()));
        clientes.setClienteResponses(paginarV2(newClienteListResponse, customPageable));
        return  clientes;
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

    public static Flux<ClienteWrapperV2> filterClienteV2(Flux<ClienteWrapperV2> clientesFlux, Integer idadeMin, Integer idadeMax, String sexo, String dataNascMin, String dataNascMax, String mes, String dia, CustomPageable customPageable) {
        return clientesFlux.flatMap(clientes -> {

            if (sexo == null && idadeMin == null && idadeMax == null && dataNascMin == null && dataNascMax == null && mes == null && dia == null) {
                return Flux.just(clientes);
            }

            List<ClienteResponseV2> newClienteListResponse = clientes.getClienteResponses().toList();

            if (idadeMin != null) {
                newClienteListResponse = newClienteListResponse.stream().filter(cliente -> cliente.getIdade() >= idadeMin).collect(Collectors.toList());
            }

            if (idadeMax != null) {
                newClienteListResponse = newClienteListResponse.stream().filter(cliente -> cliente.getIdade() <= idadeMax).collect(Collectors.toList());
            }

            if (sexo != null) {
                newClienteListResponse = newClienteListResponse.stream().filter(cliente -> cliente.getSexo().equalsIgnoreCase(sexo)).collect(Collectors.toList());
            }

            if (dataNascMin != null && dataNascMax == null) {
                newClienteListResponse = newClienteListResponse.stream().filter(cliente -> stringParaDataNascimento(cliente.getDataNascimento()).isAfter(stringParaDataNascimento(dataNascMin))).collect(Collectors.toList());
            }

            if (dataNascMax != null && dataNascMin == null) {
                newClienteListResponse = newClienteListResponse.stream().filter(cliente -> stringParaDataNascimento(cliente.getDataNascimento()).isBefore(stringParaDataNascimento(dataNascMax))).collect(Collectors.toList());
            }

            if (mes != null) {
                newClienteListResponse = newClienteListResponse.stream().filter(cliente -> stringParaDataNascimento(cliente.getDataNascimento()).getMonthValue() == (Integer.valueOf(mes))).collect(Collectors.toList());
                System.out.println("mes" + mes);
            }

            if (dia != null) {
                newClienteListResponse = newClienteListResponse.stream().filter(cliente -> stringParaDataNascimento(cliente.getDataNascimento()).getDayOfMonth() == (Integer.valueOf(dia))).collect(Collectors.toList());
                System.out.println("dia" + dia);
            }
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

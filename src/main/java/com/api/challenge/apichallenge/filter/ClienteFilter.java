package com.api.challenge.apichallenge.filter;

import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
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
    public static Page<ClienteResponseV2> filterClienteCSV(Page<ClienteResponseV2> clientes, Integer idadeMin, Integer idadeMax, String sexo, String dataNascMin, String dataNascMax, String mes, String dia) {

        if (sexo == null && idadeMin == null && idadeMax == null && dataNascMin == null && dataNascMax == null && mes == null && dia == null) {
            return clientes;
        }

        List<ClienteResponseV2> newClienteListResponse = clientes.toList();

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


        Page<ClienteResponseV2> clientePage = new PageImpl<>(newClienteListResponse);

        return  clientePage;
    }

    public static Page<ClienteResponse> filterCliente(Page<ClienteResponse> clientes, Integer idade, String sexo, String aniversario) {

        if (sexo == null && idade == null && aniversario == null) {
            return clientes;
        }

        List<ClienteResponse> newClienteListResponse = new ArrayList<>();

        if (idade != null) {
            newClienteListResponse = clientes.stream().filter(cliente -> cliente.getIdade() == idade).collect(Collectors.toList());
        }
        if (sexo != null) {
            newClienteListResponse = clientes.stream().filter(cliente -> cliente.getSexo().equalsIgnoreCase(sexo)).collect(Collectors.toList());
        }
        if (aniversario != null) {
            newClienteListResponse = clientes.stream().filter(cliente -> cliente.getDataNascimento().equals(aniversario)).collect(Collectors.toList());
        }
        Page<ClienteResponse> clientePage = new PageImpl<>(newClienteListResponse);

        return  clientePage;
    }

    public static Flux<Page<ClienteResponseV2>> filterClienteV2(Flux<Page<ClienteResponseV2>> clientesFlux, Integer idadeMin, Integer idadeMax, String sexo, String dataNascMin, String dataNascMax, String mes, String dia) {
        return clientesFlux.flatMap(clientes -> {
            if (sexo == null && idadeMin == null && idadeMax == null && dataNascMin == null && dataNascMax == null && mes == null && dia == null) {
                return Flux.just(clientes);
            }

            List<ClienteResponseV2> newClienteListResponse = clientes.toList();

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

            Page<ClienteResponseV2> clientePage = new PageImpl<>(newClienteListResponse);
            return Flux.just(clientePage);
        });
    }

    public static LocalDate stringParaDataNascimento(String string) {
        Locale LOCALE_BRAZIL = new Locale("pt", "BR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", LOCALE_BRAZIL);
        LocalDate data = LocalDate.parse(string, formatter);
        return data;
    }
}

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
    public static Page<ClienteResponseV2> filterClienteCSV(Page<ClienteResponseV2> clientes, String idadeFilter, String sexo, String dataNascimentoFilter) {

        if (sexo == null && idadeFilter == null &&  dataNascimentoFilter == null) {
            return clientes;
        }

        List<ClienteResponseV2> newClienteListResponse = new ArrayList<>();

        if (idadeFilter != null) {
            IdadeFilter idade = new IdadeFilter();
            String[] idadeUnparsed = idadeFilter.split(":");
            idade.setValor(Integer.valueOf(idadeUnparsed[0]));
            idade.setComparador(IdadeFilter.Comparador.valueOf(idadeUnparsed[1]));
            switch (idade.getComparador()) {
                case MENOR:
                    newClienteListResponse = clientes.stream().filter(cliente -> cliente.getIdade() < idade.valor).collect(Collectors.toList());
                    break;

                case IGUAL:
                    newClienteListResponse = clientes.stream().filter(cliente -> cliente.getIdade() == idade.valor).collect(Collectors.toList());
                    break;

                case MAIOR:
                    newClienteListResponse = clientes.stream().filter(cliente -> cliente.getIdade() > idade.valor).collect(Collectors.toList());
                    break;

            }
        }

        if (sexo != null) {
            newClienteListResponse = clientes.stream().filter(cliente -> cliente.getSexo().equalsIgnoreCase(sexo)).collect(Collectors.toList());
        }

        if (dataNascimentoFilter != null) {
            DataNascimentoFilter aniversario = new DataNascimentoFilter();
            String[] dataUnparsed = dataNascimentoFilter.split(":");
            aniversario.setDataNascimento(dataUnparsed[0]);
            aniversario.setComparador(DataNascimentoFilter.Comparador.valueOf(dataUnparsed[1]));
            switch (aniversario.getComparador()) {
                case MENOR:
                    newClienteListResponse = clientes.stream().filter(cliente -> aniversario.stringParaData(cliente.getDataNascimento()).isBefore(aniversario.stringParaData(aniversario.getDataNascimento()))).collect(Collectors.toList());
                    break;

                case IGUAL:
                    newClienteListResponse = clientes.stream().filter(cliente -> cliente.getDataNascimento().equals(aniversario.getDataNascimento())).collect(Collectors.toList());
                    break;

                case MAIOR:
                    newClienteListResponse = clientes.stream().filter(cliente -> aniversario.stringParaData(cliente.getDataNascimento()).isAfter(aniversario.stringParaData(aniversario.getDataNascimento()))).collect(Collectors.toList());
                    break;
            }
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

    public static Flux<Page<ClienteResponseV2>> filterClienteV2(Flux<Page<ClienteResponseV2>> clientesFlux, Integer idade, String sexo, String aniversario) {
        return clientesFlux.flatMap(clientes -> {
            if (sexo == null && idade == null && aniversario == null) {
                return Flux.just(clientes);
            }
            List<ClienteResponseV2> newClienteListResponse = new ArrayList<>();

            if (idade != null) {
                newClienteListResponse = clientes.stream().filter(cliente -> cliente.getIdade() == idade).collect(Collectors.toList());
            }
            if (sexo != null) {
                System.out.println(clientes.getContent().get(0).getSexo());;

                newClienteListResponse = clientes.stream().filter(cliente -> cliente.getSexo().equalsIgnoreCase(sexo)).collect(Collectors.toList());
            }
            if (aniversario != null) {
                newClienteListResponse = clientes.stream().filter(cliente -> cliente.getDataNascimento().equals(aniversario)).collect(Collectors.toList());
            }

            Page<ClienteResponseV2> clientePage = new PageImpl<>(newClienteListResponse);
            return Flux.just(clientePage);
        });
    }
}

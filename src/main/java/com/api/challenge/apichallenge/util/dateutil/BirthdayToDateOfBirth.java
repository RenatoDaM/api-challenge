package com.api.challenge.apichallenge.util.dateutil;

import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class BirthdayToDateOfBirth {

    public BirthdayToDateOfBirth() {
    }

    public static ClienteResponse convertBirthdayToDateOfBirth(ClienteResponse clienteResponse) {

        Locale LOCALE_BRAZIL = new Locale("pt", "BR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", LOCALE_BRAZIL);
        String concatenarAno = clienteResponse.getDataNascimento() + "-" + Integer.toString(2023 - clienteResponse.getIdade());
        LocalDate localDate = LocalDate.parse(concatenarAno, formatter);

        clienteResponse.setDataNascimento(checkIfhadBirthdayThisYear(localDate).format(formatter));

        return clienteResponse;
    }

    public static ClienteResponseV2 convertBirthdayToDateOfBirthV2(ClienteResponseV2 clienteResponseV2) {
        if (clienteResponseV2.getDataNascimento().matches("^\\d\\d-\\d\\d-\\d\\d\\d\\d$")) {
            System.out.println(clienteResponseV2.getId());

            return clienteResponseV2;
        }
        System.out.println(clienteResponseV2.getId());
        Locale LOCALE_BRAZIL = new Locale("pt", "BR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", LOCALE_BRAZIL);
        String concatenarAno = clienteResponseV2.getDataNascimento() + "-" + Integer.toString(2023 - clienteResponseV2.getIdade());
        LocalDate localDate = LocalDate.parse(concatenarAno, formatter);

        clienteResponseV2.setDataNascimento(checkIfhadBirthdayThisYear(localDate).format(formatter));
        return clienteResponseV2;
    }

    public static LocalDate checkIfhadBirthdayThisYear(LocalDate dataNascimento) {
        LocalDate hoje = LocalDate.now();
        if (dataNascimento.getMonth().compareTo(hoje.getMonth()) > 0) {
            dataNascimento = dataNascimento.minusYears(1);
        } else if (dataNascimento.getMonth() == hoje.getMonth() && dataNascimento.getDayOfMonth() > hoje.getDayOfMonth()) {
            dataNascimento = dataNascimento.minusYears(1);
        }
        return dataNascimento;
    }
}

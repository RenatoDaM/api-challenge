package com.api.challenge.apichallenge.util.dateutil;

import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AniversarioParaDNConversor {

    public AniversarioParaDNConversor() {
    }

    public static ClienteResponse formatarAniversarioParaDataNascimento(ClienteResponse clienteResponse) {

        Locale LOCALE_BRAZIL = new Locale("pt", "BR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", LOCALE_BRAZIL);
        String concatenarAno = clienteResponse.getDataNascimento() + "-" + Integer.toString(2023 - clienteResponse.getIdade());
        LocalDate localDate = LocalDate.parse(concatenarAno, formatter);

        clienteResponse.setDataNascimento(ajustarDataNascimentoSeMesNascMaior(localDate).format(formatter));

        return clienteResponse;
    }

    public static ClienteResponseV2 formatarAniversarioParaDataNascimento(ClienteResponseV2 clienteResponseV2) {

        Locale LOCALE_BRAZIL = new Locale("pt", "BR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", LOCALE_BRAZIL);
        String concatenarAno = clienteResponseV2.getDataNascimento() + "-" + Integer.toString(2023 - clienteResponseV2.getIdade());
        LocalDate localDate = LocalDate.parse(concatenarAno, formatter);

        clienteResponseV2.setDataNascimento(ajustarDataNascimentoSeMesNascMaior(localDate).format(formatter));

        return clienteResponseV2;
    }

    public static LocalDate ajustarDataNascimentoSeMesNascMaior(LocalDate dataNascimento) {
        LocalDate hoje = LocalDate.now();
        if (dataNascimento.getMonth().compareTo(hoje.getMonth()) > 0) {
            dataNascimento = dataNascimento.minusYears(1);
        } else if (dataNascimento.getMonth() == hoje.getMonth() && dataNascimento.getDayOfMonth() > hoje.getDayOfMonth()) {
            dataNascimento = dataNascimento.minusYears(1);
        }
        return dataNascimento;
    }
}

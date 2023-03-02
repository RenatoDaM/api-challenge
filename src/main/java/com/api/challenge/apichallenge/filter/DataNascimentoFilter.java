package com.api.challenge.apichallenge.filter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataNascimentoFilter {
    String dataNascimento;
    Comparador comparador;

    public DataNascimentoFilter() {
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Comparador getComparador() {
        return comparador;
    }

    public void setComparador(Comparador comparador) {
        this.comparador = comparador;
    }

    public LocalDate stringParaData(String string) {
        Locale LOCALE_BRAZIL = new Locale("pt", "BR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", LOCALE_BRAZIL);
        LocalDate data = LocalDate.parse(string, formatter);
        return data;
    }

    public enum Comparador {
        MENOR,
        IGUAL,
        MAIOR
    }
}

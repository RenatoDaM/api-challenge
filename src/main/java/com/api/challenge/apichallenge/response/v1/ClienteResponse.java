package com.api.challenge.apichallenge.response.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ClienteResponse {
    String nome;
    int idade;
    String sexo;

    String dataNascimento;


    public ClienteResponse() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    @JsonProperty("dataNascimento")
    public String getDataNascimento() {

        Locale LOCALE_BRAZIL = new Locale("pt", "BR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", LOCALE_BRAZIL);
        String concatenarAno = this.dataNascimento + "-" + Integer.toString(2023 - this.getIdade());
        LocalDate localDate = LocalDate.parse(concatenarAno, formatter);

        return ajustarDataNascimentoSeMesNascMaior(localDate).format(formatter);
    }
    @JsonProperty("aniversario")
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    private LocalDate ajustarDataNascimentoSeMesNascMaior(LocalDate dataNascimento) {
        LocalDate hoje = LocalDate.now();
        if (dataNascimento.getMonth().compareTo(hoje.getMonth()) > 0) {
            dataNascimento = dataNascimento.minusYears(1);
        } else if (dataNascimento.getMonth() == hoje.getMonth() && dataNascimento.getDayOfMonth() > hoje.getDayOfMonth()) {
            dataNascimento = dataNascimento.minusYears(1);
        }
        return dataNascimento;
    }
}

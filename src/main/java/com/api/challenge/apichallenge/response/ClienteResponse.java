package com.api.challenge.apichallenge.response;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    public LocalDate getDataNascimento() {
        Locale BRAZIL = new Locale("pt", "BR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", BRAZIL);
        String date = "-";
        date = date.concat(Integer.toString(2023 - this.getIdade()));
        String date2 = this.dataNascimento.concat(date);
        LocalDate localDate = LocalDate.parse(date2, formatter);

        return localDate;
    }
    @JsonProperty("aniversario")
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}

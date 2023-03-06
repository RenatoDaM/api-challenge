package com.api.challenge.apichallenge.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByPosition;

public class ClienteRequest {
    @CsvBindByPosition(position = 0)
    Integer id;
    @CsvBindByPosition(position = 1)
    String nome;
    @CsvBindByPosition(position = 2)
    int idade;
    @CsvBindByPosition(position = 3)
    String sexo;
    @CsvBindByPosition(position = 4)
    String dataNascimento;


    public ClienteRequest() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return this.dataNascimento;
    }

    @JsonProperty("dataNascimento")
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
package com.api.challenge.apichallenge.response.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

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
        return this.dataNascimento;
    }
    @JsonProperty("aniversario")
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}

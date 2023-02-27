package com.api.challenge.apichallenge.response.v2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClienteResponseV2 {
    Integer id;
    String nome;
    int idade;
    String sexo;

    String dataNascimento;


    public ClienteResponseV2() {

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
    @JsonProperty("aniversario")
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }


}

package com.api.challenge.apichallenge.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByPosition;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ClienteRequest {
    @CsvBindByPosition(position = 0)
    @Schema(nullable = true, example = "null", description = "Ao criar você não insere o parâmetro e nem valor ID. Apenas deixei visível esse parâmetro pois após criar um novo cliente, irá retornar o valor do ID que ficou salvo no arquivo CSV.")
    Integer id;

    @CsvBindByPosition(position = 1)
    @NotNull
    @NotBlank
    String nome;

    @CsvBindByPosition(position = 2)
    @NotNull
    Integer idade;

    @CsvBindByPosition(position = 3)
    @NotNull
    @NotBlank
    String sexo;

    @Schema(example = "01-01-1997")
    @CsvBindByPosition(position = 4)
    @NotNull
    @NotBlank
    String dataNascimento;


    public ClienteRequest() {

    }

    public ClienteRequest(String nome, Integer idade, String sexo, String dataNascimento) {
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
    }

    public ClienteRequest(Integer id, String nome, Integer idade, String sexo, String dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
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

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
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

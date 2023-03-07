package com.api.challenge.apichallenge.search;

public class ClienteRequestParam {

    Integer idadeMin;
    Integer idadeMax;
    String sexo;
    String mes;
    String dia;
    String dataNascMin;
    String dataNascMax;

    public ClienteRequestParam(Integer idadeMin, Integer idadeMax, String sexo, String mes, String dia, String dataNascMin, String dataNascMax) {
        this.idadeMin = idadeMin;
        this.idadeMax = idadeMax;
        this.sexo = sexo;
        this.mes = mes;
        this.dia = dia;
        this.dataNascMin = dataNascMin;
        this.dataNascMax = dataNascMax;
    }

    public ClienteRequestParam() {
    }

    public Integer getIdadeMin() {
        return idadeMin;
    }

    public void setIdadeMin(Integer idadeMin) {
        this.idadeMin = idadeMin;
    }

    public Integer getIdadeMax() {
        return idadeMax;
    }

    public void setIdadeMax(Integer idadeMax) {
        this.idadeMax = idadeMax;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getDataNascMin() {
        return dataNascMin;
    }

    public void setDataNascMin(String dataNascMin) {
        this.dataNascMin = dataNascMin;
    }

    public String getDataNascMax() {
        return dataNascMax;
    }

    public void setDataNascMax(String dataNascMax) {
        this.dataNascMax = dataNascMax;
    }
}


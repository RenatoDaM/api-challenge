package com.api.challenge.apichallenge.filter;

public class IdadeFilter {

    Integer valor;
    Comparador comparador;

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Comparador getComparador() {
        return comparador;
    }

    public void setComparador(Comparador comparador) {
        this.comparador = comparador;
    }



    public enum Comparador {
        MENOR,
        IGUAL,
        MAIOR
    }
}

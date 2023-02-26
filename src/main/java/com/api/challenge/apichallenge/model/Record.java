package com.api.challenge.apichallenge.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Record {
    @JsonProperty("clientes")
    List<Cliente> clientes;

    public List<Cliente> getRecord() {
        return clientes;
    }

    public void setRecord(List<Cliente> record) {
        this.clientes = record;
    }
}

package com.api.challenge.apichallenge.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Record {
    @JsonProperty("clientes")
    List<ClienteResponse> clienteResponses;

    public List<ClienteResponse> getRecord() {
        return clienteResponses;
    }

    public void setRecord(List<ClienteResponse> record) {
        this.clienteResponses = record;
    }
}

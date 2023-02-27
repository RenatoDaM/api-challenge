package com.api.challenge.apichallenge.response.v2;

import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ClienteResponseWrapperV2 {

    @JsonProperty("clientes")
    List<ClienteResponseV2> clienteResponses;

    public List<ClienteResponseV2> getRecord() {
        return clienteResponses;
    }

    public void setRecord(List<ClienteResponseV2> record) {
        this.clienteResponses = record;
    }
}

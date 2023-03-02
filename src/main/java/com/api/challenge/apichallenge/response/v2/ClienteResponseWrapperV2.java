package com.api.challenge.apichallenge.response.v2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ClienteResponseWrapperV2 {

    @JsonProperty("clientes")
    List<ClienteResponseV2> clienteResponses;

    public List<ClienteResponseV2> getClientesResponseV2List() {
        return clienteResponses;
    }

    public void setClientesResponseV2List(List<ClienteResponseV2> record) {
        this.clienteResponses = record;
    }
}

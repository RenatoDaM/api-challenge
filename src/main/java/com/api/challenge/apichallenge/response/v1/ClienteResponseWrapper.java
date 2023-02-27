package com.api.challenge.apichallenge.response.v1;

import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ClienteResponseWrapper {

    @JsonProperty("clientes")
    List<ClienteResponse> clienteResponses;

    public List<ClienteResponse> getRecord() {
        return clienteResponses;
    }

    public void setRecord(List<ClienteResponse> record) {
        this.clienteResponses = record;
    }
}

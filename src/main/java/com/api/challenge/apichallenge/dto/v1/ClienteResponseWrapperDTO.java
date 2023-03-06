package com.api.challenge.apichallenge.dto.v1;

import com.api.challenge.apichallenge.response.MetaData;
import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ClienteResponseWrapperDTO {
    MetaData metaData;
    @JsonProperty("clientes")
    List<ClienteResponse> clienteResponses;


    public List<ClienteResponse> getClientesResponseV2List() {
        return clienteResponses;
    }

    public void setClientesResponseV2List(List<ClienteResponse> record) {
        this.clienteResponses = record;
    }
}

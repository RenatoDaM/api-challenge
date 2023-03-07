package com.api.challenge.apichallenge.dto.v2;

import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.response.MetaData;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ClienteResponseWrapperDTOV2 {
    MetaData metaData;
    @JsonProperty("clientes")
    List<ClienteResponseV2> clienteResponses;

    public ClienteResponseWrapperDTOV2() {
    }

    public ClienteResponseWrapperDTOV2(List<ClienteResponseV2> clienteResponses, MetaData metaData) {
        this.metaData = metaData;
        this.clienteResponses = clienteResponses;
    }

    public List<ClienteResponseV2> getClientesResponseV2List() {
        return clienteResponses;
    }

    public void setClientesResponseV2List(List<ClienteResponseV2> record) {
        this.clienteResponses = record;
    }
}

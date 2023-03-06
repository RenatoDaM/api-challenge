package com.api.challenge.apichallenge.response.v1;

import com.api.challenge.apichallenge.response.MetaData;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

public class ClienteWrapper {
    MetaData metaData;
    @JsonProperty("clientes")
    Page<ClienteResponse> clienteResponses;

    public ClienteWrapper(Page<ClienteResponse> clienteResponses, MetaData metaData) {
        this.clienteResponses = clienteResponses;
        this.metaData = metaData;
    }

    public ClienteWrapper() {
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public Page<ClienteResponse> getClienteResponses() {
        return clienteResponses;
    }

    public void setClienteResponses(Page<ClienteResponse> clienteResponses) {
        this.clienteResponses = clienteResponses;
    }

}

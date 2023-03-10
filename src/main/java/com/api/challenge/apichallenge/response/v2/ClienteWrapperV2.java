package com.api.challenge.apichallenge.response.v2;

import com.api.challenge.apichallenge.response.MetaData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;
@JsonIgnoreProperties({
        "sort",
        "unpaged",
        "paged"
})
public class ClienteWrapperV2 {
    MetaData metaData;
    @JsonIgnoreProperties(value = {
            "sort",
            "last",
            "first",
            "size",
            "number",
            "empty",
            "totalElements"
    })
    @JsonProperty("clientes")
    Page<ClienteResponseV2> clienteResponses;

    public ClienteWrapperV2(Page<ClienteResponseV2> clienteResponses, MetaData metaData) {
        this.clienteResponses = clienteResponses;
        this.metaData = metaData;
    }

    public ClienteWrapperV2() {
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public Page<ClienteResponseV2> getClienteResponses() {
        return clienteResponses;
    }

    public void setClienteResponses(Page<ClienteResponseV2> clienteResponses) {
        this.clienteResponses = clienteResponses;
    }

}

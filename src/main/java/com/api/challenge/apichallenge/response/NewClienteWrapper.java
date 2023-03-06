package com.api.challenge.apichallenge.response;

import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class NewClienteWrapper {
    MetaData metaData;
    @JsonProperty("clientes")
    List<ClienteResponseV2> clienteResponses;
    int currentPage;
    int pageSize;

    public NewClienteWrapper(List<ClienteResponseV2> clienteResponses, Pageable pageable, MetaData metaData) {
        this.clienteResponses = clienteResponses;
        this.currentPage = pageable.getPageNumber();
        this.pageSize = pageable.getPageSize();
        this.metaData = metaData;
    }

    public NewClienteWrapper() {
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public List<ClienteResponseV2> getClienteResponses() {
        return clienteResponses;
    }

    public void setClienteResponses(List<ClienteResponseV2> clienteResponses) {
        this.clienteResponses = clienteResponses;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


}

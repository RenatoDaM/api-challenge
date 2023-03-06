package com.api.challenge.apichallenge.util.jsonparser;

import com.api.challenge.apichallenge.dto.v2.ClienteResponseWrapperDTOV2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ClienteJsonParser {
    static
    ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode pegarJsonNode(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode extrairNodeClientes(JsonNode jsonNode) {
        return jsonNode.get("record");
    }


    public static ClienteResponseWrapperDTOV2 mapearParaClientesWrapperDTO(JsonNode clientesNode) {
        try {
            System.out.println(clientesNode);
            return objectMapper.treeToValue(clientesNode, ClienteResponseWrapperDTOV2.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

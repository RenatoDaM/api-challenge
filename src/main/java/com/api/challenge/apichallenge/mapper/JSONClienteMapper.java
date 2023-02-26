package com.api.challenge.apichallenge.mapper;

import com.api.challenge.apichallenge.model.Cliente;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class JSONClienteMapper {
    public static List<Cliente> parseClienteJson(String json, List<Cliente> listaClientes) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject o = new JSONObject(json);
        JSONObject obj = o.getJSONObject("record");
        JSONArray jsonArray = obj.getJSONArray("clientes");

        for (int i = 0; i < jsonArray.toList().size(); i++) {
            Cliente cliente = objectMapper.readValue(jsonArray.get(i).toString(), Cliente.class);
            listaClientes.add(cliente);
        }

        return listaClientes;
    }
}

package com.api.challenge.apichallenge.unittests;

import static org.junit.jupiter.api.Assertions.*;
import com.api.challenge.apichallenge.dto.v2.ClienteResponseWrapperDTOV2;
import com.api.challenge.apichallenge.mock.MockClienteJson;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.util.jsonparser.ClienteJsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JsonToClienteResponseTest {
    @Test
    public void testJsonToClienteConverter() {
        String mockedClienteList = MockClienteJson.mockClienteJson();
        JsonNode jsonNode = ClienteJsonParser.pegarJsonNode(mockedClienteList);
        JsonNode jsonNode2 = ClienteJsonParser.extrairNodeClientes(jsonNode);
        ClienteResponseWrapperDTOV2 clienteResponseWrapperDTOV2 = ClienteJsonParser.mapearParaClientesWrapperDTO(jsonNode2);
        List<ClienteResponseV2> clienteList = clienteResponseWrapperDTOV2.getClientesResponseV2List();

        assertEquals("Maria", clienteList.get(0).getNome());
        assertEquals(23, clienteList.get(0).getIdade());
        assertEquals("F", clienteList.get(0).getSexo());
        assertEquals("01-02", clienteList.get(0).getDataNascimento());

        assertEquals("Pedro", clienteList.get(1).getNome());
        assertEquals(42, clienteList.get(1).getIdade());
        assertEquals("M", clienteList.get(1).getSexo());
        assertEquals("20-02", clienteList.get(1).getDataNascimento());

        assertEquals("Carla", clienteList.get(2).getNome());
        assertEquals(54, clienteList.get(2).getIdade());
        assertEquals("F", clienteList.get(2).getSexo());
        assertEquals("01-06", clienteList.get(2).getDataNascimento());

        assertEquals("Yan", clienteList.get(65).getNome());
        assertEquals(11, clienteList.get(65).getIdade());
        assertEquals("M", clienteList.get(65).getSexo());
        assertEquals("28-06", clienteList.get(65).getDataNascimento());

        assertEquals("Sara", clienteList.get(66).getNome());
        assertEquals(31, clienteList.get(66).getIdade());
        assertEquals("F", clienteList.get(66).getSexo());
        assertEquals("01-10", clienteList.get(66).getDataNascimento());

    }
}

package com.api.challenge.apichallenge.mock;

import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;

import java.util.ArrayList;
import java.util.List;

public class MockCliente {

    public static List<ClienteResponseV2> mockClienteV2List() {
        List<ClienteResponseV2> clienteList = new ArrayList<>();
        ClienteResponseV2 cliente1 = new ClienteResponseV2(1, "Renato", 23, "M", "28-12-1999");
        ClienteResponseV2 cliente2 = new ClienteResponseV2(2, "Anderson", 25, "M", "28-12-1997");
        ClienteResponseV2 cliente3 = new ClienteResponseV2(3, "Aline", 27, "F", "28-12-1995");
        ClienteResponseV2 cliente4 = new ClienteResponseV2(4, "Luiza", 30, "F", "01-01-1993");
        clienteList.add(cliente1);
        clienteList.add(cliente2);
        clienteList.add(cliente3);
        clienteList.add(cliente4);

        return clienteList;
    }

    public static ClienteResponseV2 mockCliente(Integer id) {
        ClienteResponseV2 clienteResponseV2 = new ClienteResponseV2();
        clienteResponseV2.setId(id);

        clienteResponseV2.setNome("ClienteTest number " + id);
        if (id % 2 == 0) {
            clienteResponseV2.setSexo("F");
        } else {
            clienteResponseV2.setSexo("M");
        }

        clienteResponseV2.setDataNascimento("28-12-1999");
        clienteResponseV2.setIdade(23);

        return  clienteResponseV2;
    }
}

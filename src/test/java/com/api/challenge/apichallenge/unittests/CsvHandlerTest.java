package com.api.challenge.apichallenge.unittests;

import com.api.challenge.apichallenge.exception.CorruptedDataOnCSVFileException;
import com.api.challenge.apichallenge.mock.MockCliente;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.util.csv.ClienteCSVHandler;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

public class CsvHandlerTest {
    ClienteCSVHandler clienteCSVHandler = new ClienteCSVHandler("C:\\Users\\Renato\\IdeaProjects\\api-challenge\\src\\test\\");

    @Test
    @Order(1)
    public void writeCSVTest() {
        List<ClienteResponseV2> clienteResponseV2List = MockCliente.mockClienteV2List();
        clienteResponseV2List.forEach(element -> {
            try {
                clienteCSVHandler.consumesApiToCSV(element);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (CorruptedDataOnCSVFileException e) {
                throw new RuntimeException(e);
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    @Order(2)
    public void updateTest() {

    }
}

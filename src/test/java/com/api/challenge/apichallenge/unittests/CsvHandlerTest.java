package com.api.challenge.apichallenge.unittests;

import com.api.challenge.apichallenge.exception.ClienteInCSVNotFoundException;
import com.api.challenge.apichallenge.exception.CorruptedDataOnCSVFileException;
import com.api.challenge.apichallenge.exception.InvalidCsvParams;
import com.api.challenge.apichallenge.mock.MockCliente;
import com.api.challenge.apichallenge.request.ClienteRequest;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.util.csv.ClienteCSVHandler;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    public void writeNewLine() throws InvalidCsvParams, IOException, CorruptedDataOnCSVFileException, CsvException {
        ClienteRequest clienteRequest = new ClienteRequest("Write New Line Test", 21, "F", "28-10-2001");
        clienteCSVHandler.writeNewLine(clienteRequest);
    }

    @Test
    @Order(3)
    public void updateTest() throws ClienteInCSVNotFoundException, InvalidCsvParams, IOException {
        ClienteRequest clienteRequest = new ClienteRequest(1, "Update teste", 23, "F", "28-10-1999");
        clienteCSVHandler.updateCSV(clienteRequest);
    }

    @Test
    @Order(4)
    public void readTest() throws IOException, CorruptedDataOnCSVFileException, CsvException {
        List<ClienteResponseV2> clienteResponseV2List = clienteCSVHandler.read();
        assertEquals(clienteResponseV2List.get(0).getNome(), "Update teste");
        assertEquals(clienteResponseV2List.get(1).getNome(), "Anderson");
        assertEquals(clienteResponseV2List.get(2).getNome(), "Aline");
        assertEquals(clienteResponseV2List.get(3).getNome(), "Luiza");
        assertEquals(clienteResponseV2List.get(4).getNome(), "Write New Line Test");
    }

    @Test
    @Order(5)
    public void deleteTest() throws ClienteInCSVNotFoundException, IOException, CorruptedDataOnCSVFileException, CsvException {
        clienteCSVHandler.deleteCSVLineById(1);
        clienteCSVHandler.deleteCSVLineById(2);
        clienteCSVHandler.deleteCSVLineById(3);
        clienteCSVHandler.deleteCSVLineById(4);
        clienteCSVHandler.deleteCSVLineById(5);

        assertTrue(clienteCSVHandler.read().isEmpty());
    }
}

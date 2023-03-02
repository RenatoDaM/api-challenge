package com.api.challenge.apichallenge.util.csv;

import com.api.challenge.apichallenge.request.ClienteRequest;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvChainedException;
import com.opencsv.exceptions.CsvException;
import org.w3c.dom.ls.LSOutput;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ClienteCSVHandler {
    private final String filePath;

    public ClienteCSVHandler(String filePath) {
        this.filePath = filePath;
    }

    public void write(ClienteResponseV2 pessoa) throws IOException {
        FileWriter fileWriter = new FileWriter(filePath + "listaDeClientes.csv", true);

        CSVWriter csvWriter = new CSVWriter(fileWriter, ';', '"', '"', "\n");

                String[] linha = {Integer.toString(pessoa.getId()), pessoa.getNome(),
                        Integer.toString(pessoa.getIdade()), pessoa.getSexo(), pessoa.getDataNascimento()};
                if (linha[0].equals("1")) {
                    String[] header = {"Id", "Nome", "Idade", "Sexo", "DataNascimento"};
                    csvWriter.writeNext(header);
                }
                csvWriter.writeNext(linha);

        csvWriter.close();
    }

    public List<ClienteResponseV2> read() throws FileNotFoundException {

        CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath + "listaDeClientes.csv"))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build();

        List<ClienteResponseV2> clienteList = new CsvToBeanBuilder(csvReader)
                .withType(ClienteResponseV2.class)
                .withFilter(new ClienteCSVFilter())
                .build()
                .parse();

        clienteList.forEach(e -> System.out.println(e.getNome()));
        return clienteList;
    }

    public ClienteRequest updateCSV(ClienteRequest clienteRequest) throws IOException {
        CSVReader csvReader = new CSVReaderBuilder(new FileReader(filePath + "listaDeClientes.csv"))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build();

        List<ClienteRequest> clienteList = new CsvToBeanBuilder(csvReader)
                .withType(ClienteRequest.class)
                .withFilter(new ClienteCSVFilter())
                .build()
                .parse();

        for (ClienteRequest cliente : clienteList) {
            if (clienteRequest.getId() == clienteRequest.getId()) {
                clienteList.set(clienteList.indexOf(cliente), clienteRequest);
                break;
            }
        }

        FileWriter fileWriter = new FileWriter(filePath + "listaDeClientes.csv");
        CSVWriter csvWriter = new CSVWriter(fileWriter, ';', '"', '"', "\n");
        String[] header = {"Id", "Nome", "Idade", "Sexo", "DataNascimento"};
        csvWriter.writeNext(header);

        clienteList.forEach(cliente ->  {
            String[] array = {Integer.toString(cliente.getId()), cliente.getNome(),
                    Integer.toString(cliente.getIdade()), cliente.getSexo(), cliente.getDataNascimento()};
            csvWriter.writeNext(array);
        });

        csvWriter.close();

        List<ClienteResponseV2> clienteListUpdated = new CsvToBeanBuilder(csvReader)
                .withType(ClienteResponseV2.class)
                .withFilter(new ClienteCSVFilter())
                .build()
                .parse();

        if (clienteListUpdated.contains(clienteRequest)) {
            return clienteRequest;
        }




return null;
    }


}

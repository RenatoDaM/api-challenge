package com.api.challenge.apichallenge.util.csv;

import com.api.challenge.apichallenge.request.ClienteRequest;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.context.annotation.Bean;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClienteCSVHandler {

    public Integer lastIndex;
    private final String FILE_PATH;
    private static final String CSV_FILE_NAME = "listaDeClientes.csv";
    private static final String[] CSV_HEADERS = {"Id", "Nome", "Idade", "Sexo", "DataNascimento"};

    public ClienteCSVHandler(String FILE_PATH) {
        this.FILE_PATH = FILE_PATH;
    }


    public ClienteRequest writeNewLine(ClienteRequest pessoa) throws IOException {
        FileWriter fileWriter = new FileWriter(FILE_PATH + CSV_FILE_NAME, true);
        CSVWriter csvWriter = new CSVWriter(fileWriter, ';', '"', '"', "\n");
        String[] linha = {pessoa.getNome(),
                Integer.toString(pessoa.getIdade()), pessoa.getSexo(), pessoa.getDataNascimento()};
        csvWriter.writeNext(linha);
        csvWriter.close();
        return pessoa;
    }

    public void consumesApiToCSV(ClienteResponseV2 pessoa) throws IOException {
        FileWriter fileWriter = new FileWriter(FILE_PATH + CSV_FILE_NAME, true);

        CSVWriter csvWriter = new CSVWriter(fileWriter, ';', '"', '"', "\n");

                String[] linha = {Integer.toString(pessoa.getId()), pessoa.getNome(),
                        Integer.toString(pessoa.getIdade()), pessoa.getSexo(), pessoa.getDataNascimento()};
                if (linha[0].equals("1")) {
                    csvWriter.writeNext(CSV_HEADERS);
                }
                csvWriter.writeNext(linha);

        csvWriter.close();
    }

    public List<ClienteResponseV2> read() throws FileNotFoundException {

        CSVReader csvReader = new CSVReaderBuilder(new FileReader(FILE_PATH + CSV_FILE_NAME))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build();

        return (List<ClienteResponseV2>) new CsvToBeanBuilder(csvReader)
                .withType(ClienteResponseV2.class)
                .withFilter(new ClienteCSVFilter())
                .build()
                .parse();
    }

    public ClienteRequest updateCSV(ClienteRequest clienteRequest) throws IOException {
        CSVReader csvReader = new CSVReaderBuilder(new FileReader(FILE_PATH + CSV_FILE_NAME))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build();

        List<ClienteRequest> clienteList = new CsvToBeanBuilder(csvReader)
                .withType(ClienteRequest.class)
                .withFilter(new ClienteCSVFilter())
                .build()
                .parse();

        for (ClienteRequest cliente : clienteList) {
            if (clienteRequest.getId().equals(cliente.getId())) {
                int index = clienteList.indexOf(cliente);
                clienteList.set(index, clienteRequest);
                break;
            }
        }

        reescreverCSVRequest(clienteList);
        return clienteRequest;
    }

    public void deleteCSVLine(Integer id) throws IOException {
        List<ClienteResponseV2> clienteList = read();
        List<ClienteResponseV2> novaLista = new ArrayList<>();

        for (ClienteResponseV2 cliente : clienteList) {
            if (!cliente.getId().equals(id)) {
                novaLista.add(cliente);
            } else {
                System.out.println("ACHAMOOO");
            }
        }
        reescreverCSV(novaLista);
    }

    public void reescreverCSV(List<ClienteResponseV2> novaLista) throws IOException {
        FileWriter fileWriter = new FileWriter(FILE_PATH + CSV_FILE_NAME);
        CSVWriter csvWriter = new CSVWriter(fileWriter, ';', '"', '"', "\n");
        csvWriter.writeNext(CSV_HEADERS);

        novaLista.forEach(cliente ->  {
            String[] array = {Integer.toString(cliente.getId()), cliente.getNome(),
                    Integer.toString(cliente.getIdade()), cliente.getSexo(), cliente.getDataNascimento()};
            csvWriter.writeNext(array);
        });

        csvWriter.close();
    }

    public void reescreverCSVRequest(List<ClienteRequest> novaLista) throws IOException {
        FileWriter fileWriter = new FileWriter(FILE_PATH + CSV_FILE_NAME);
        CSVWriter csvWriter = new CSVWriter(fileWriter, ';', '"', '"', "\n");
        csvWriter.writeNext(CSV_HEADERS);

        novaLista.forEach(cliente ->  {
            String[] array = {Integer.toString(cliente.getId()), cliente.getNome(),
                    Integer.toString(cliente.getIdade()), cliente.getSexo(), cliente.getDataNascimento()};
            csvWriter.writeNext(array);
        });

        csvWriter.close();
    }
}

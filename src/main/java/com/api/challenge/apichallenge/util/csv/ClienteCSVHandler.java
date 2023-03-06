package com.api.challenge.apichallenge.util.csv;

import com.api.challenge.apichallenge.exception.ClienteInCSVNotFoundException;
import com.api.challenge.apichallenge.request.ClienteRequest;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClienteCSVHandler {
    private final String FILE_PATH;
    private static final String CSV_FILE_NAME = "listaDeClientes.csv";
    private static final String[] CSV_HEADERS = {"Id", "Nome", "Idade", "Sexo", "DataNascimento"};

    public ClienteCSVHandler(String FILE_PATH) {
        this.FILE_PATH = FILE_PATH;
    }


    public ClienteRequest writeNewLine(ClienteRequest pessoa) throws IOException {
        FileWriter fileWriter = new FileWriter(FILE_PATH + CSV_FILE_NAME, true);
        CSVWriter csvWriter = new CSVWriter(fileWriter, ';', '"', '"', "\n");
        List<ClienteResponseV2> clientesList = read();
        ClienteResponseV2 clienteResponseV2 = clientesList.get(clientesList.size()-1);
        // Eu tinha implementado uma lógica antes aonde essa classe era um Bean, aonde inicializava
        // já com uma variável lastIndex que recebia o ultimo ID da lista no momento da inicialização (lia o
        // arquivo ao inicializar a aplicação), e esse atributo
        // era usado para as lógicas, com o objetivo de não ter que ficar lendo o arquivo toda hora.
        // Porém, pensando pelo lado que alguém pode abrir o arquivo e apagar, achei mais safe ler sim
        // várias vezes. Porém, dependendo da regra de negócio seria sim possível, já que ID's (dependendo
        // da regra) são apenas para identificação, nao possuem um valor além desse.
        String[] linha = {Integer.toString(clienteResponseV2.getId()+1), pessoa.getNome(),
                Integer.toString(pessoa.getIdade()), pessoa.getSexo(), pessoa.getDataNascimento()};
        csvWriter.writeNext(linha);
        csvWriter.close();
        pessoa.setId(clienteResponseV2.getId()+1);
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

    public ClienteRequest updateCSV(ClienteRequest clienteRequest) throws IOException, ClienteInCSVNotFoundException {
        CSVReader csvReader = new CSVReaderBuilder(new FileReader(FILE_PATH + CSV_FILE_NAME))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build();

        List<ClienteRequest> clienteList = new CsvToBeanBuilder(csvReader)
                .withType(ClienteRequest.class)
                .withFilter(new ClienteCSVFilter())
                .build()
                .parse();
        boolean clienteFound = false;
        for (ClienteRequest cliente : clienteList) {
            if (clienteRequest.getId().equals(cliente.getId())) {
                int index = clienteList.indexOf(cliente);
                clienteList.set(index, clienteRequest);
                clienteFound = true;
                break;
            }
        }
        if (!clienteFound) throw new ClienteInCSVNotFoundException("OPERAÇÃO UPDATE FALHOU. Não foi possível achar um usuário com o ID: " + clienteRequest.getId());
        reescreverCSVRequest(clienteList);
        return clienteRequest;
    }

    public void deleteCSVLine(Integer id) throws IOException, ClienteInCSVNotFoundException {
        List<ClienteResponseV2> clienteList = read();
        List<ClienteResponseV2> novaLista = new ArrayList<>();
        boolean idEncontrado = false;
        for (int i = 0; i < clienteList.size(); i++) {
            ClienteResponseV2 cliente = clienteList.get(i);
            if (cliente.getId().equals(id)) {
                idEncontrado = true;
            } else {
                novaLista.add(cliente);
            }
            System.out.println(cliente.getNome());
        }

        if (!idEncontrado) throw new ClienteInCSVNotFoundException("OPERAÇÃO DELETAR FALHOU. Não foi encontrado um cliente com o ID: " + id);

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

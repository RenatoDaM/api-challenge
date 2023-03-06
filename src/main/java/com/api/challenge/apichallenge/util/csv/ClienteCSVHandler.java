package com.api.challenge.apichallenge.util.csv;

import com.api.challenge.apichallenge.exception.ClienteInCSVNotFoundException;
import com.api.challenge.apichallenge.exception.InvalidDateOfBirth;
import com.api.challenge.apichallenge.request.ClienteRequest;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import org.checkerframework.checker.regex.qual.Regex;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClienteCSVHandler {
    private final String FILE_PATH;
    private static final String CSV_FILE_NAME = "listaDeClientes.csv";
    private static final String[] CSV_HEADERS = {"Id", "Nome", "Idade", "Sexo", "DataNascimento"};

    public ClienteCSVHandler(String FILE_PATH) {
        this.FILE_PATH = FILE_PATH;
    }


    public ClienteRequest writeNewLine(ClienteRequest pessoa) throws IOException, InvalidDateOfBirth {
        FileWriter fileWriter = new FileWriter(FILE_PATH + CSV_FILE_NAME, true);
        CSVWriter csvWriter = new CSVWriter(fileWriter, ';', '"', '"', "\n");
        List<ClienteResponseV2> clientesList = read();
        String regex = "\\d\\d-\\d\\d-\\d\\d\\d\\d";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pessoa.getDataNascimento());
        if (matcher.find()) {
            System.out.println(clientesList.size());
            System.out.println(clientesList.get(clientesList.size()-3).getNome());

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
        } else {
            throw new InvalidDateOfBirth("Data de nascimento inválida. Exemplo de formato correto: 01-01-1997.");
        }

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

    public ClienteRequest updateCSV(ClienteRequest clienteRequest) throws IOException, ClienteInCSVNotFoundException, InvalidDateOfBirth {
        CSVReader csvReader = new CSVReaderBuilder(new FileReader(FILE_PATH + CSV_FILE_NAME))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build();

        List<ClienteRequest> clienteList = new CsvToBeanBuilder(csvReader)
                .withType(ClienteRequest.class)
                .withFilter(new ClienteCSVFilter())
                .build()
                .parse();

        String regex = "\\d\\d-\\d\\d-\\d\\d\\d\\d";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(clienteRequest.getDataNascimento());
        if (matcher.find()) {
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
        } else {
            throw new InvalidDateOfBirth("Data de nascimento inválida. Exemplo de formato correto: 01-01-1997.");
        }
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

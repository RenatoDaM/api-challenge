package com.api.challenge.apichallenge.util.csv;

import com.api.challenge.apichallenge.exception.ClienteInCSVNotFoundException;
import com.api.challenge.apichallenge.exception.CorruptedDataOnCSVFileException;
import com.api.challenge.apichallenge.exception.InvalidCsvParams;
import com.api.challenge.apichallenge.request.ClienteRequest;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClienteCSVHandler {
    Logger logger = LoggerFactory.getLogger(ClienteCSVHandler.class);
    private final String FILE_PATH;
    public static final String CSV_FILE_NAME = "listaDeClientes.csv";
    private static final String[] CSV_HEADERS = {"Id", "Nome", "Idade", "Sexo", "DataNascimento"};

    public ClienteCSVHandler(String FILE_PATH) {
        this.FILE_PATH = FILE_PATH;
    }


    public ClienteRequest writeNewLine(ClienteRequest pessoa) throws IOException, InvalidCsvParams, CsvException, CorruptedDataOnCSVFileException {
        FileWriter fileWriter = new FileWriter(FILE_PATH + CSV_FILE_NAME, true);
        CSVWriter csvWriter = new CSVWriter(fileWriter, ';', '"', '"', "\n");
        List<ClienteResponseV2> clientesList = read();

        String regex = "^\\d\\d-\\d\\d-\\d\\d\\d\\d$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pessoa.getDataNascimento());



        if (matcher.find()) {
            ClienteResponseV2 clienteResponseV2 = clientesList.get(clientesList.size() - 1);
            String[] linha = {Integer.toString(clienteResponseV2.getId() + 1), pessoa.getNome(),
                    Integer.toString(pessoa.getIdade()), pessoa.getSexo(), pessoa.getDataNascimento()};

            if (!linha[2].matches("^[FM]$")) {
                logger.error("Invalid biologic gender");
                throw new InvalidCsvParams("Invalid biologic gender. Please insert a valid biologic gender (F or M)");
            }

            csvWriter.writeNext(linha);
            csvWriter.close();
            pessoa.setId(clienteResponseV2.getId() + 1);

            return pessoa;
        } else {
            logger.error("Invalid date of birth. Example of correct use: 01-01-1997");
            throw new InvalidCsvParams("Data de nascimento inválida. Exemplo de formato correto: 01-01-1997.");
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

    public List<ClienteResponseV2> read() throws IOException, CsvException, CorruptedDataOnCSVFileException {

        validateCSVFile();

        CSVReader csvReader = new CSVReaderBuilder(new FileReader(FILE_PATH + CSV_FILE_NAME))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build();
        List<ClienteResponseV2> clienteList = new CsvToBeanBuilder(csvReader)
                .withType(ClienteResponseV2.class)
                .withFilter(new ClienteCSVIsFirstLine())
                .build()
                .parse();



        return clienteList;
    }

    public ClienteRequest updateCSV(ClienteRequest clienteRequest) throws IOException, ClienteInCSVNotFoundException, InvalidCsvParams {
        CSVReader csvReader = new CSVReaderBuilder(new FileReader(FILE_PATH + CSV_FILE_NAME))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build();

        List<ClienteRequest> clienteList = new CsvToBeanBuilder(csvReader)
                .withType(ClienteRequest.class)
                .withFilter(new ClienteCSVIsFirstLine())
                .build()
                .parse();

        String regex = "^\\d\\d-\\d\\d-\\d\\d\\d\\d$";
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
            throw new InvalidCsvParams("Data de nascimento inválida. Exemplo de formato correto: 01-01-1997.");
        }
    }

    public void deleteCSVLineById(Integer id) throws IOException, ClienteInCSVNotFoundException, CsvException, CorruptedDataOnCSVFileException {

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

    public void validateCSVFile() throws CorruptedDataOnCSVFileException, CsvValidationException, IOException {
        CSVReader csvReader2 = new CSVReaderBuilder(new FileReader(FILE_PATH + CSV_FILE_NAME))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build();

        String[] line;
        int lineNumber = 2;

        //descarta o cabeçalho
        csvReader2.readNext();

        while ((line = csvReader2.readNext()) != null) {
            lineNumber++;
            if (!line[0].matches("^[0-9]+$")) {
                System.out.println(line[0]);
                logger.error("Error on line {}, it is corrupted or some data has been manually inserted incorrectly. Please fix or remove it. Corrupted date value: {}", lineNumber, line[0]);
                throw new CorruptedDataOnCSVFileException("Error on line " + lineNumber + ", it is corrupted or some data has been manually inserted incorrectly. Please fix or remove it.");
            }

            if (!line[2].matches("^[0-9]+$")) {
                logger.error("Error on line {}, it is corrupted or some data has been manually inserted incorrectly. Please fix or remove it. Corrupted date value: {}", lineNumber, line[2]);
                throw new CorruptedDataOnCSVFileException("Error on line " + lineNumber + ", it is corrupted or some data has been manually inserted incorrectly. Please fix or remove it. ");
            }
        }
    }
}

package com.api.challenge.apichallenge.util;

import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class ClienteCSVWriter {
    private final String filePath;

    public ClienteCSVWriter(String filePath) {
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


}

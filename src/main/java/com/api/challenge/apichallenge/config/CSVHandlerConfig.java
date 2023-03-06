package com.api.challenge.apichallenge.config;

import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.util.csv.ClienteCSVHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;

@Configuration
public class CSVHandlerConfig {
    public static final String CSV_DIRECTORY_PATH = "D:\\FACULDADE\\";

    @Bean
    public ClienteCSVHandler clienteCSVHandler() throws FileNotFoundException {
        ClienteCSVHandler clienteCSVHandler = new ClienteCSVHandler(CSV_DIRECTORY_PATH);
        ClienteResponseV2 clienteResponseV2 = clienteCSVHandler.read().get(clienteCSVHandler.read().size() - 2);
        clienteCSVHandler.lastIndex = clienteResponseV2.getId();
        return clienteCSVHandler;
    }
}

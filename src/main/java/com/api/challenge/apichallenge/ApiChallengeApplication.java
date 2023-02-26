package com.api.challenge.apichallenge;

import com.api.challenge.apichallenge.service.ClienteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiChallengeApplication {

    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(ApiChallengeApplication.class, args);
        System.out.println("hello");
        ClienteService clienteService = new ClienteService();

    }

}

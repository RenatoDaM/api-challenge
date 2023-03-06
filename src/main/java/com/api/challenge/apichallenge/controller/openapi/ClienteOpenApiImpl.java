package com.api.challenge.apichallenge.controller.openapi;

import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

public interface ClienteOpenApiImpl {
/*    @Operation(summary = "Consome a API de jsonbin.io e retorna um json tratado com os dados do cliente.")
    public ResponseEntity<Page<ClienteResponse>> getClientes(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(value = "idade", required = false) Integer idade,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "aniversario", required = false) String aniversario) throws IOException;*/
}

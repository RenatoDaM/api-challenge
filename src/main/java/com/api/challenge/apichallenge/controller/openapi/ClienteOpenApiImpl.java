package com.api.challenge.apichallenge.controller.openapi;

import com.api.challenge.apichallenge.pagination.CustomPageable;
import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v1.ClienteWrapper;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

public interface ClienteOpenApiImpl {


    @Operation(summary = "Retorna uma lista de clientes filtrados com base em vários parâmetros de consulta",
            description = "Este endpoint retorna uma lista de clientes filtrados com base nos parâmetros de consulta fornecidos. Os parâmetros de consulta são opcionais e podem ser usados em qualquer combinação.")
    @Parameter(name = "sexo", description = "Pesquisar apenas clientes do sexo definido: F ou M", example = "F")
    @ApiResponse(description = "Retorna um JSON com ClienteWrapper.", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteWrapper.class)))
    @Parameter(required = false, hidden = true, name = "customPageable")
    @PageableAsQueryParam
    ResponseEntity<ClienteWrapper> getClientes(
            @PageableDefault(size = 10) CustomPageable customPageable,
            @RequestParam(value = "idade", required = false) Integer idade,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "aniversario", required = false) String aniversario) throws IOException;
}

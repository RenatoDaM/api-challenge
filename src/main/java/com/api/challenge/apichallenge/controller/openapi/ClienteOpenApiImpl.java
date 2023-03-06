package com.api.challenge.apichallenge.controller.openapi;

import com.api.challenge.apichallenge.pagination.CustomPageable;
import com.api.challenge.apichallenge.response.v1.ClienteResponse;
import com.api.challenge.apichallenge.response.v1.ClienteWrapper;
import com.api.challenge.apichallenge.response.v2.ClienteWrapperV2;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import java.io.IOException;

public interface ClienteOpenApiImpl {


    @Operation(summary = "Retorna uma lista de clientes filtrados com base em vários parâmetros de consulta",
            description = "Este endpoint retorna uma lista de clientes filtrados com base nos parâmetros de consulta fornecidos. Os parâmetros de consulta são opcionais e podem ser usados em qualquer combinação.")
    @Parameters(value = {
            @Parameter(name = "sexo", description = "Pesquisa apenas clientes do sexo definido: F ou M", example = "F"),
            @Parameter(hidden = true, name = "customPageable")
    })
    @ApiResponse(description = "Retorna um JSON com ClienteWrapper.", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteWrapper.class)))
    @PageableAsQueryParam
    ResponseEntity<ClienteWrapper> getClientes(
            @PageableDefault(size = 10) CustomPageable customPageable,
            @RequestParam(value = "idade", required = false) Integer idade,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "aniversario", required = false) String aniversario) throws IOException;

    @Operation(summary = "Retorna uma lista de clientes filtrados com base em vários parâmetros de consulta",
            description = "Este endpoint retorna uma lista de clientes filtrados com base nos parâmetros de consulta fornecidos. Os parâmetros de consulta são opcionais e podem ser usados em qualquer combinação.")
    @Parameters(value = {
            @Parameter(name = "sexo", description = "Pesquisa apenas clientes do sexo definido: F ou M", example = "F"),
            @Parameter(name = "idade_min", description = "Pesquisa apenas clientes com a idade maior ou igual ao valor inserido", example = "33"),
            @Parameter(name = "idade_max", description = "Pesquisa apenas clientes com a idade menor ou igual ao valor inserido", example = "33"),
            @Parameter(name = "mes", description = "Pesquisa clientes que fazem aniversário no mês inserido", example = "33"),
            @Parameter(name = "dia", description = "Pesquisa clientes que fazem aniversário no dia inserido", example = "33"),
            @Parameter(name = "data_nasc_min", description = "Pesquisa clientes que nasceram na data inserida ou após", example = "01-01-1997"),
            @Parameter(name = "data_nasc_max", description = "Pesquisa clientes que nasceram na data inserida ou antes", example = "01-01-1997"),
            @Parameter(hidden = true, name = "customPageable")
    })
    @ApiResponse(description = "Retorna um JSON com ClienteWrapperV2.", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteWrapper.class)))
    public ResponseEntity<Flux<ClienteWrapperV2>> getClientesV2(
            @PageableDefault(size = 10, page = 0) CustomPageable customPageable,
            @RequestParam(value = "idade_min", required = false) Integer idadeMin,
            @RequestParam(value = "idade_max", required = false) Integer idadeMax,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "mes", required = false) String mes,
            @RequestParam(value = "dia", required = false) String dia,
            @RequestParam(value = "data_nasc_min", required = false) String dataNascMin,
            @RequestParam(value = "data_nasc_max", required = false) String dataNascMax);

}

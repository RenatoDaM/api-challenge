package com.api.challenge.apichallenge.controller.openapi;

import com.api.challenge.apichallenge.exception.ClienteInCSVNotFoundException;
import com.api.challenge.apichallenge.exception.InvalidDateOfBirth;
import com.api.challenge.apichallenge.exception.MissingClienteParametersException;
import com.api.challenge.apichallenge.pagination.CustomPageable;
import com.api.challenge.apichallenge.request.ClienteRequest;
import com.api.challenge.apichallenge.response.ErrorResponse;
import com.api.challenge.apichallenge.response.Response;
import com.api.challenge.apichallenge.response.v1.ClienteWrapper;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.response.v2.ClienteWrapperV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import java.io.FileNotFoundException;
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
    @PageableAsQueryParam
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

    @Operation(summary = "Cria um arquivo CSV no diretório definido na API a partir do GET realizado na API mockada.")
    @ApiResponse(description = "Cria um arquivo CSV no diretório padrão definido na aplicação. O arquivo CSV contém a lista recebida através do GET da API mockada. Essa requisição reescreve o arquivo caso já exista.", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)))
    public ResponseEntity<Response> postarCSV();

    @Operation(summary = "Adiciona um cliente ao arquivo CSV no formato padrão separado por ;")
    @ApiResponses(value = {
            @ApiResponse(description = "Adiciona um cliente ao arquivo CSV", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseV2.class))),
            @ApiResponse(description = "Data de nascimento inválida. Exemplo de formato correto: 01-01-1997.", responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ClienteResponseV2> adicionarPessoaCSV(@RequestBody ClienteRequest clienteRequest) throws IOException, InvalidDateOfBirth, MissingClienteParametersException;

    @Operation(summary = "Retorna uma lista de clientes filtrados com base em vários parâmetros de consulta a partir do arquivo CSV",
            description = "Este endpoint retorna uma lista de clientes filtrados a partir do arquivo CSV com base nos parâmetros de consulta fornecidos. Os parâmetros de consulta são opcionais e podem ser usados em qualquer combinação.")
    @Parameters(value = {
            @Parameter(name = "sexo", description = "Pesquisa apenas clientes do sexo definido: F ou M", example = "F"),
            @Parameter(name = "idade_min", description = "Pesquisa apenas clientes com a idade maior ou igual ao valor inserido", example = "33"),
            @Parameter(name = "idade_max", description = "Pesquisa apenas clientes com a idade menor ou igual ao valor inserido", example = "33"),
            @Parameter(name = "mes", description = "Pesquisa clientes que fazem aniversário no mês inserido", example = "33"),
            @Parameter(name = "dia", description = "Pesquisa clientes que fazem aniversário no dia inserido", example = "33"),
            @Parameter(name = "data_nasc_min", description = "Pesquisa clientes que nasceram na data inserida ou após", example = "01-01-1997"),
            @Parameter(name = "data_nasc_max", description = "Pesquisa clientes que nasceram na data inserida ou antes", example = "01-01-1997"),
            @Parameter(hidden = true, name = "pageable")
    })
    @PageableAsQueryParam
    @GetMapping("/v2/lerCSV")
    public ResponseEntity<ClienteWrapperV2> lerCSV(
            @PageableDefault(page = 0, size = 10) CustomPageable pageable,
            @RequestParam(value = "idade_min", required = false) Integer idadeMin,
            @RequestParam(value = "idade_max", required = false) Integer idadeMax,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "mes", required = false) String mes,
            @RequestParam(value = "dia", required = false) String dia,
            @RequestParam(value = "data_nasc_min", required = false) String dataNascMin,
            @RequestParam(value = "data_nasc_max", required = false) String dataNascMax) throws FileNotFoundException;

    @Operation(summary = "Atualiza um cliente dentro do CSV. Lê o objeto no body do JSON e atualiza o objeto com o mesmo ID.")
    @ApiResponses(value = {
            @ApiResponse(description = "Cliente atualizado", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponseV2.class))),
            @ApiResponse(description = "Data de nascimento inválida. Exemplo de formato correto: 01-01-1997.", responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ClienteResponseV2> atualizarCSV(@RequestBody ClienteRequest clienteRequest) throws IOException, ClienteInCSVNotFoundException, InvalidDateOfBirth, MissingClienteParametersException;

    @Operation(summary = "Deleta o cliente dentro do arquivo CSV que possui o ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(description = "Cliente deletado", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))),
            @ApiResponse(description = "Operação delete falhou. Não foi possível encontrar um cliente com o ID fornecido.", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Response> deleteCSVFile(@PathVariable Integer id) throws IOException, ClienteInCSVNotFoundException;
}

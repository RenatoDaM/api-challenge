package com.api.challenge.apichallenge.exception.handler;

import com.api.challenge.apichallenge.exception.*;
import com.api.challenge.apichallenge.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = InvalidURIException.class)
    public ResponseEntity<ErrorResponse> invalidURI(InvalidURIException e) {
        ErrorResponse errorResponse = new ErrorResponse(400, e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = {ClienteInCSVNotFoundException.class})
    public ResponseEntity<ErrorResponse> clienteNotFound(ClienteInCSVNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(404, e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(value = {InvalidCsvParams.class})
    public ResponseEntity<ErrorResponse> invalidAteOfBirth(InvalidCsvParams e) {
        ErrorResponse errorResponse = new ErrorResponse(400, e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = {MissingClienteParametersException.class})
    public ResponseEntity<ErrorResponse> missingClienteParameters(MissingClienteParametersException e) {
        ErrorResponse errorResponse = new ErrorResponse(400, e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = {CorruptedDataOnCSVFileException.class})
    public ResponseEntity<ErrorResponse> missingClienteParameters(CorruptedDataOnCSVFileException e) {
        ErrorResponse errorResponse = new ErrorResponse(500, e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // EXEMPLO DE POSSÍVEL OUTRA FORMA DE IMPLEMENTAÇÃO:
    /*@ExceptionHandler(value = { ConstraintViolationException.class })
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " +
                    violation.getMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(400, errors);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), errorResponse.getStatus(), request);
    }*/

}

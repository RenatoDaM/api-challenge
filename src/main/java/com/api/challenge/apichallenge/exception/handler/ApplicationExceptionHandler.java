package com.api.challenge.apichallenge.exception.handler;

import com.api.challenge.apichallenge.exception.ClienteInCSVNotFoundException;
import com.api.challenge.apichallenge.exception.InvalidURIException;
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

}

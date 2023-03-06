package com.api.challenge.apichallenge.exception.handler;

import com.api.challenge.apichallenge.exception.ClienteInCSVNotFound;
import com.api.challenge.apichallenge.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {




    @ExceptionHandler(value = {ClienteInCSVNotFound.class})
    public ResponseEntity<ErrorResponse> clienteNotFound(ClienteInCSVNotFound e) {
        ErrorResponse errorResponse = new ErrorResponse(404, e.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

}

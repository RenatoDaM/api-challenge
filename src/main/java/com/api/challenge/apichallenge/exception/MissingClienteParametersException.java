package com.api.challenge.apichallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MissingClienteParametersException extends Exception {
    public MissingClienteParametersException() {
        super();
    }

    public MissingClienteParametersException(String msg) {
        super(msg);
    }

    public MissingClienteParametersException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

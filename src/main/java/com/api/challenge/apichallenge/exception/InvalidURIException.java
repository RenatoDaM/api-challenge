package com.api.challenge.apichallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidURIException extends Exception {
    public InvalidURIException() {
        super();
    }

    public InvalidURIException(String msg) {
        super(msg);
    }

    public InvalidURIException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

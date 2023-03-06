package com.api.challenge.apichallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteInCSVNotFound extends Exception {
    public ClienteInCSVNotFound() {
        super();
    }

    public ClienteInCSVNotFound (String msg) {
        super(msg);
    }

    public ClienteInCSVNotFound (String msg, Throwable cause) {
        super(msg, cause);
    }
}

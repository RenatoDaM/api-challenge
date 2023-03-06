package com.api.challenge.apichallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateOfBirth extends Exception {
    public InvalidDateOfBirth() {
        super();
    }

    public InvalidDateOfBirth(String msg) {
        super(msg);
    }

    public InvalidDateOfBirth(String msg, Throwable cause) {
        super(msg, cause);
    }
}

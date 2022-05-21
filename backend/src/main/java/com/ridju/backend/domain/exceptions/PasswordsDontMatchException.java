package com.ridju.backend.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PasswordsDontMatchException extends RuntimeException{

    public PasswordsDontMatchException(String message) {
        super(message);
    }
}

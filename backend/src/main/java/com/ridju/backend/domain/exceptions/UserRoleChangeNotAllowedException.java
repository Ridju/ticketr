package com.ridju.backend.domain.exceptions;

public class UserRoleChangeNotAllowedException extends RuntimeException{

    public UserRoleChangeNotAllowedException(String message) {
        super(message);
    }
}

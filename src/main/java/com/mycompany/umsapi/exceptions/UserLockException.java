package com.mycompany.umsapi.exceptions;

public class UserLockException extends RuntimeException{
    public UserLockException(String message) {
        super(message);
    }
}

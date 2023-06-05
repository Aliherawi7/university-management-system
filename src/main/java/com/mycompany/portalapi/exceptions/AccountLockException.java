package com.mycompany.portalapi.exceptions;

public class AccountLockException extends RuntimeException{
    public AccountLockException(String message) {
        super(message);
    }
}

package com.mycompany.umsapi.exceptions;

public class AccountLockException extends RuntimeException{
    public AccountLockException(String message) {
        super(message);
    }
}

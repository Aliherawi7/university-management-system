package com.mycompany.umsapi.exceptions;

public class MissingHeaderException extends RuntimeException{
    public MissingHeaderException(String message) {
        super(message);
    }
}

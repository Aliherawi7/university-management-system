package com.mycompany.portalapi.exceptions;

public class MissingHeaderException extends RuntimeException{
    public MissingHeaderException(String message) {
        super(message);
    }
}

package com.dane.notevault.exception;

public class MissingJwtException extends RuntimeException {

    public MissingJwtException(String message) {
        super(message);
    }
}


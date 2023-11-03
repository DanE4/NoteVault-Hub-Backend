package com.dane.homework_help.exception;

public class MissingJwtException extends RuntimeException {

    public MissingJwtException(String message) {
        super(message);
    }
}


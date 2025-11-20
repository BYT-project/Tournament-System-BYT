package com.example.tournamentsystembyt.exceptions;

public class InvalidStateException extends RuntimeException {
    public InvalidStateException(String message) {
        super(message);
    }
}
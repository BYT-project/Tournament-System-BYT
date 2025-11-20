package com.example.tournamentsystembyt.exceptions;

public class NullObjectException extends InvalidValueException {
    public NullObjectException(String fieldName) {
        super(fieldName + " cannot be null.");
    }
}
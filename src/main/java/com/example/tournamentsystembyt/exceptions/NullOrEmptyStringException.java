package com.example.tournamentsystembyt.exceptions;

public class NullOrEmptyStringException extends InvalidValueException {
    public NullOrEmptyStringException(String fieldName) {
        super(fieldName + " cannot be null or empty.");
    }
}
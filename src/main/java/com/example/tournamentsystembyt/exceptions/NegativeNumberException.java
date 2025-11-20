package com.example.tournamentsystembyt.exceptions;

public class NegativeNumberException extends InvalidValueException {
    public NegativeNumberException(String fieldName) {
        super(fieldName + " cannot be negative.");
    }

    public NegativeNumberException(String fieldName, double value) {
        super(fieldName + " cannot be negative. Provided value: " + value);
    }

    public NegativeNumberException(String fieldName, int value) {
        super(fieldName + " cannot be negative. Provided value: " + value);
    }
}
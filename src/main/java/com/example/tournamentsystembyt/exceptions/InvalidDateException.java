package com.example.tournamentsystembyt.exceptions;

import java.time.LocalDate;

public class InvalidDateException extends InvalidValueException {
    public InvalidDateException(String message) {
        super(message);
    }

    public InvalidDateException(String fieldName, LocalDate value) {
        super("Invalid date for " + fieldName + ": " + value);
    }
}
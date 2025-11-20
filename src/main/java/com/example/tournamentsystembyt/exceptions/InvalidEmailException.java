package com.example.tournamentsystembyt.exceptions;

public class InvalidEmailException extends InvalidValueException {
    public InvalidEmailException(String email) {
        super("Invalid email address: " + email);
    }
}
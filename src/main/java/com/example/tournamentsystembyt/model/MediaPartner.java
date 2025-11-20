package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.exceptions.InvalidEmailException;

public class MediaPartner {

    private String name;
    private String email;
    private String type;

    public MediaPartner(String name, String email, String type) {
        setName(name);
        setEmail(email);
        setType(type);
    }

    // -------------------------------------
    //              VALIDATION
    // -------------------------------------

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Media partner name");
        }
        String trimmed = name.trim();

        if (trimmed.length() < 2) {
            throw new InvalidValueException("Media partner name must contain at least 2 characters.");
        }

        if (!trimmed.matches("[A-Za-zÀ-ÖØ-öø-ÿ0-9\\-\\s]+")) {
            throw new InvalidValueException("Media partner name contains invalid characters.");
        }

        this.name = trimmed;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Email");
        }

        String trimmed = email.trim();

        // Standard email validation regex
        if (!trimmed.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new InvalidEmailException(trimmed);
        }

        this.email = trimmed;
    }

    public void setType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Media partner type");
        }

        String trimmed = type.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException("Media partner type must contain at least 2 characters.");
        }

        // Optional: restrict to known types (TV, Streaming, Newspaper, Sponsor, etc.)
        // If needed later, we can convert this to an enum or controlled list.

        this.type = trimmed;
    }

    // -------------------------------------
    //               GETTERS
    // -------------------------------------

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }
}
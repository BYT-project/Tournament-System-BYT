package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;

public class MediaPartner {
    private String name;
    private String email;
    private String type;

    public MediaPartner(String name, String email, String type) {
        setName(name);
        setEmail(email);
        setType(type);
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new InvalidValueException("Media partner name cannot be empty.");
        this.name = name;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty())
            throw new InvalidValueException("Email cannot be empty.");
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            throw new InvalidValueException("Invalid email format.");
        this.email = email;
    }

    public void setType(String type) {
        if (type == null || type.trim().isEmpty())
            throw new InvalidValueException("Media partner type cannot be empty.");
        this.type = type;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getType() { return type; }
}

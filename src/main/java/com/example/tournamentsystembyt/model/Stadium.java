package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

public class Stadium {

    private final String name;
    private int capacity;
    private String location;

    public Stadium(String name, int capacity, String location) {
        validateName(name);
        validateCapacity(capacity);
        validateLocation(location);

        this.name = name.trim();
        this.capacity = capacity;
        this.location = location.trim();
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Stadium name");
        }
        if (name.trim().length() < 2) {
            throw new InvalidValueException("Stadium name must contain at least 2 characters.");
        }
        if (!name.matches("[A-Za-zÀ-ÖØ-öø-ÿ0-9\\-\\s]+")) {
            throw new InvalidValueException("Stadium name contains invalid characters.");
        }
    }

    private void validateCapacity(int capacity) {
        if (capacity <= 0) {
            throw new NegativeNumberException("Capacity", capacity);
        }
        if (capacity < 1000) {
            throw new InvalidValueException("Capacity must be at least 1000 seats for a stadium.");
        }
        if (capacity > 150000) {
            throw new InvalidValueException("Capacity exceeds the maximum realistic size for a stadium.");
        }
    }

    private void validateLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Location");
        }
        if (location.trim().length() < 2) {
            throw new InvalidValueException("Location must contain at least 2 characters.");
        }
        if (!location.matches("[A-Za-zÀ-ÖØ-öø-ÿ0-9\\-\\s,]+")) {
            throw new InvalidValueException("Location contains invalid characters.");
        }
    }

    public void setCapacity(int capacity) {
        validateCapacity(capacity);
        this.capacity = capacity;
    }

    public void setLocation(String location) {
        validateLocation(location);
        this.location = location.trim();
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLocation() {
        return location;
    }
}
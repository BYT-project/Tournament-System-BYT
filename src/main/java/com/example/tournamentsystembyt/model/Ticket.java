package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

public class Ticket {

    private String id;
    private double price;
    private String type;
    private String status;

    public static final double TAX_FEE = 0.05; // 5% tax

    public Ticket(String id, double price, String type, String status) {
        setId(id);
        setPrice(price);
        setType(type);
        setStatus(status);
    }

    // --------------------------
    //        VALIDATION
    // --------------------------

    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Ticket ID");
        }
        String trimmed = id.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException("Ticket ID must contain at least 2 characters.");
        }
    }

    private void validateType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Ticket type");
        }
        String trimmed = type.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException("Ticket type must contain at least 2 characters.");
        }
        // Could be: MATCH, TOURNAMENT, etc. We only ensure it's meaningful.
    }

    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Ticket status");
        }
        String trimmed = status.trim();

        // Optional: restrict to a controlled set of statuses
        if (!trimmed.matches("(?i)AVAILABLE|RESERVED|SOLD|CANCELLED")) {
            throw new InvalidValueException("Invalid ticket status: " + trimmed);
        }
    }

    private void validatePrice(double price) {
        if (price < 0) {
            throw new NegativeNumberException("Price", price);
        }
    }

    // --------------------------
    //          SETTERS
    // --------------------------

    public void setId(String id) {
        validateId(id);
        this.id = id.trim();
    }

    public void setPrice(double price) {
        validatePrice(price);
        this.price = price;
    }

    public void setType(String type) {
        validateType(type);
        this.type = type.trim();
    }

    public void setStatus(String status) {
        validateStatus(status);
        this.status = status.trim().toUpperCase();
    }

    // --------------------------
    //          GETTERS
    // --------------------------

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    // --------------------------
    //      BUSINESS LOGIC
    // --------------------------

    // Complex behavior: Calculate total price including tax
    public double calculateTotalPrice() {
        return price + (price * TAX_FEE);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
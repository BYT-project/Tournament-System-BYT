package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidStateException;
import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

import java.time.LocalDate;

public class Payment {

    private final String id;
    private String method;
    private double amount;
    private LocalDate paidAt;

    public Payment(String id, String method, double amount) {
        if (id == null || id.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Payment ID");
        }
        this.id = id.trim();

        setMethod(method);
        setAmount(amount);
    }

    public String getMethod() {
        return method;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getPaidAt() {
        return paidAt;
    }

    // --------------------------
    //       VALIDATION
    // --------------------------

    private void validateMethod(String method) {
        if (method == null || method.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Payment method");
        }
        String trimmed = method.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException("Payment method must contain at least 2 characters.");
        }
        // Could be CARD, CASH, TRANSFER, etc.
    }

    private void validateAmount(double amount) {
        if (amount < 0) {
            throw new NegativeNumberException("Amount", amount);
        }
    }

    // --------------------------
    //          SETTERS
    // --------------------------

    public void setMethod(String method) {
        validateMethod(method);
        this.method = method.trim();
    }

    public void setAmount(double amount) {
        validateAmount(amount);
        this.amount = amount;
    }

    // --------------------------
    //      BUSINESS LOGIC
    // --------------------------

    public void pay() {
        if (paidAt != null) {
            throw new InvalidStateException("Payment has already been made.");
        }
        this.paidAt = LocalDate.now();
    }

    public void refund() {
        if (paidAt == null) {
            throw new InvalidStateException("Payment has not been made yet.");
        }
        this.paidAt = null;
    }
}
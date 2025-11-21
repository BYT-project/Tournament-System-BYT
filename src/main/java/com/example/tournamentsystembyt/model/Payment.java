package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidStateException;
import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Payment {

    private  String id;
    private String method;
    private double amount;
    private LocalDate paidAt;

    private static final List<Payment> extent = new ArrayList<>();

    private static void addPayment(Payment p) {
        if (p == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        extent.add(p);
    }

    public static List<Payment> getExtent() {
        return new ArrayList<>(extent); // defensive copy
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(Payment.class, extent);
    }

    public static void loadExtent() {
        List<Payment> loaded = ExtentPersistence.loadExtent(Payment.class);
        extent.clear();
        extent.addAll(loaded);
    }
    public Payment(String id, String method, double amount) {
        if (id == null || id.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Payment ID");
        }
        this.id = id.trim();

        setMethod(method);
        setAmount(amount);
        addPayment(this);
    }
    public Payment(){
    }

    public String getMethod() {
        return method;
    }

    public double getAmount() {
        return amount;
    }
    public String getId() { return id; }
    public LocalDate getPaidAt() {
        return paidAt;
    }

    private void validateMethod(String method) {
        if (method == null || method.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Payment method");
        }
        String trimmed = method.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException("Payment method must contain at least 2 characters.");
        }
    }

    private void validateAmount(double amount) {
        if (amount < 0) {
            throw new NegativeNumberException("Amount", amount);
        }
    }

    public void setMethod(String method) {
        validateMethod(method);
        this.method = method.trim();
    }

    public void setAmount(double amount) {
        validateAmount(amount);
        this.amount = amount;
    }

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
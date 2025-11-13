package com.example.tournamentsystembyt.model;

import java.time.LocalDate;

public class Payment {
    private final String id;
    private String method;
    private double amount;
    private LocalDate paidAt;
    public Payment(String id, String method, double amount) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment id cannot be empty");
        }
        this.id = id;
        validateMethod(method);
        validateAmount(amount);
        this.method = method;
        this.amount = amount;
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
    private void validateMethod(String method) {
        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("Method cannot be null or empty.");
        }
    }
    public void setMethod(String method) {
        validateMethod(method);
        this.method = method;
    }
    private void validateAmount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
    }
    public void setAmount(double amount) {
        validateAmount(amount);
        this.amount = amount;
    }

    public void pay(){
        if(paidAt != null){
            throw new IllegalStateException("Payment has already been made.");
        }
        this.paidAt = LocalDate.now();
    }
    public void refund(){
        if(paidAt == null){
            throw new IllegalStateException("Payment has not been made yet.");
        }
        this.paidAt = null;
    }
}

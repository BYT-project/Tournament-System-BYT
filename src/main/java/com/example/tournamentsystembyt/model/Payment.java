package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.*;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Payment {

    private  String id;
    private String method;
    private double amount;
    private LocalDate paidAt;
    // NEW – composition whole
    private TicketOrder ticketOrder;

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

    // NEW composition at creation
    public Payment(String id, String method, double amount, TicketOrder order) {
        this(id, method, amount);
        setTicketOrder(order);
    }

    // NEW
    public TicketOrder getTicketOrder() {
        return ticketOrder;
    }

    // NEW – cannot change "whole", must be non-null
    public void setTicketOrder(TicketOrder ticketOrder) {
        if (ticketOrder == null) {
            throw new NullObjectException("Ticket order");
        }
        if (this.ticketOrder == ticketOrder) {
            return;
        }
        if (this.ticketOrder != null && this.ticketOrder != ticketOrder) {
            throw new InvalidStateException("Payment is already assigned to a different order.");
        }
        this.ticketOrder = ticketOrder;
        ticketOrder.setPayment(this);   // reverse connection
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

    // NEW – used from TicketOrder.delete()
    public void delete() {
        if (ticketOrder != null) {
            TicketOrder order = ticketOrder;
            ticketOrder = null;
            order.clearPaymentOnDelete(this);
        }
        extent.remove(this);
    }
}
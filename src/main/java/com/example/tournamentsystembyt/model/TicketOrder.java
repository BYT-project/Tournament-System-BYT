package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidStateException;
import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketOrder {

    private  String id;
    private  LocalDate createdAt;
    private double amount;
    private String status;
    private List<Ticket> tickets = new ArrayList<>();
    private Payment payment;

    private static final List<TicketOrder> extent = new ArrayList<>();

    private static void addTicketOrder(TicketOrder o) {
        if (o == null) throw new IllegalArgumentException("TicketOrder cannot be null");
        extent.add(o);
    }

    public static List<TicketOrder> getExtent() {
        return new ArrayList<>(extent); // defensive copy
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(TicketOrder.class, extent);
    }

    public static void loadExtent() {
        List<TicketOrder> loaded = ExtentPersistence.loadExtent(TicketOrder.class);
        extent.clear();
        extent.addAll(loaded);
    }
    public TicketOrder(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Order ID");
        }
        this.id = id.trim();
        this.createdAt = LocalDate.now();
        this.amount = 0.0;
        this.status = "PENDING";
        addTicketOrder(this);
    }
    public TicketOrder() {
       this.tickets = new ArrayList<>();
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Payment getPayment() {
        return payment;
    }

    public List<Ticket> getTickets() {
        return Collections.unmodifiableList(tickets);
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    private void validateAmount(double amount) {
        if (amount < 0) {
            throw new NegativeNumberException("Amount", amount);
        }
    }

    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Order status");
        }
        String trimmed = status.trim().toUpperCase();
        if (!trimmed.matches("PENDING|PAID|CANCELLED")) {
            throw new InvalidValueException("Invalid order status: " + trimmed);
        }
    }

    public void calculateTotal() {
        this.amount = tickets.stream()
                .mapToDouble(Ticket::calculateTotalPrice)
                .sum();
    }

    public void setAmount(double amount) {
        validateAmount(amount);
        this.amount = amount;
    }

    public void setStatus(String status) {
        validateStatus(status);
        this.status = status.trim().toUpperCase();
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void addTicket(Ticket ticket) {
        if (ticket == null) {
            throw new NullObjectException("Ticket");
        }
        if (!"PENDING".equals(status)) {
            throw new InvalidStateException("Cannot add tickets to a non-pending order.");
        }
        tickets.add(ticket);
    }

    public void pay(Payment payment) {
        if (payment == null) {
            throw new NullObjectException("Payment");
        }
        if (!"PENDING".equals(status)) {
            throw new InvalidStateException("Ticket order is not in a payable state.");
        }

        calculateTotal();

        if (payment.getAmount() < this.amount) {
            throw new InvalidValueException("Insufficient payment amount.");
        }

        payment.pay();
        this.payment = payment;
        this.status = "PAID";
    }

    public void cancel() {
        if ("CANCELLED".equals(status)) {
            throw new InvalidStateException("Ticket order is already cancelled.");
        }
        if ("PAID".equals(status)) {
            throw new InvalidStateException("Cannot cancel a paid ticket order.");
        }
        this.status = "CANCELLED";
    }
}
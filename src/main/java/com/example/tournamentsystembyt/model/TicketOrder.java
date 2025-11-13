package com.example.tournamentsystembyt.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TicketOrder {
    private final String id;
    private final LocalDate createdAt;
    private double amount;
    private String status;
    private final List<Ticket> tickets = new ArrayList<>();
    private Payment payment;
    private static final List<TicketOrder> EXTENT = new ArrayList<>();
    public TicketOrder(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Order id cannot be empty");
        }
        this.id = id;
        this.createdAt = LocalDate.now();
        this.amount = 0.0;
        this.status = "PENDING";
        EXTENT.add(this);
    }
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    public Payment getPayment() {
        return payment;
    }
    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<Ticket> getTickets() {
        return tickets;
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
            throw new IllegalArgumentException("Amount cannot be negative.");
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
    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty.");
        }
    }
    public void setStatus(String status) {
        validateStatus(status);
        this.status = status;
    }
    public void addTicket(Ticket ticket){
        if(ticket == null){
            throw new IllegalArgumentException("Ticket cannot be null.");
        }
        if(!status.equals("PENDING")){
            throw new IllegalStateException("Cannot add tickets to a non-pending order.");
        }
        tickets.add(ticket);
    }

    public void pay(Payment payment){
        if(payment == null){
            throw new IllegalArgumentException("Payment cannot be null.");
        }
        if(!status.equals("PENDING")){
            throw new IllegalStateException("Ticket order is not in a payable state.");
        }
        calculateTotal();
        if(payment.getAmount() < this.amount){
            throw new IllegalArgumentException("Insufficient payment amount.");
        }
        payment.pay();
        this.payment = payment;
        this.status = "PAID";
    }

    public void cancel(){
        if(status.equals("CANCELLED")){
            throw new IllegalStateException("Ticket order is already cancelled.");
        }
        if(status.equals("PAID")){
            throw new IllegalStateException("Cannot cancel a paid ticket order.");
        }
        this.status = "CANCELLED";
    }

    public static List<TicketOrder> getExtent() {
        return new ArrayList<>(EXTENT);
    }

    public static void checkPendingOrders() {
        for (TicketOrder order : EXTENT) {
            if ("PENDING".equals(order.status)) {
                System.out.println("Pending order found: " + order.id + " created at " + order.createdAt);
            }
        }
    }
}

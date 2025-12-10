package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.util.ArrayList;
import java.util.List;

public class Ticket {

    private String id;
    private double price;
    private String type;
    private String status;

    private Viewer viewer;
    private TicketOrder ticketOrder;

    private static final List<Ticket> extent = new ArrayList<>();

    private static void addTicket(Ticket t) {
        if (t == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }
        extent.add(t);
    }

    public static List<Ticket> getExtent() {
        return new ArrayList<>(extent); // defensive copy
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(Ticket.class, extent);
    }

    public static void loadExtent() {
        List<Ticket> loaded = ExtentPersistence.loadExtent(Ticket.class);
        extent.clear();
        extent.addAll(loaded);
    }

    public static final double TAX_FEE = 0.05; // 5% tax

    public Ticket(String id, double price, String type, String status) {
        setId(id);
        setPrice(price);
        setType(type);
        setStatus(status);
        addTicket(this);
    }
    public Ticket() {
    }

    public Viewer getViewer() {
        return viewer;
    }

    public void setViewer(Viewer newViewer) {
        if (this.viewer == newViewer) {
            return;
        }

        if (this.viewer != null) {
            Viewer old = this.viewer;
            this.viewer = null;
            old.getTickets().remove(this);
        }
        this.viewer = newViewer;

        if (newViewer != null && !newViewer.getTickets().contains(this)) {
            newViewer.getTickets().add(this);
        }
    }

    public TicketOrder getTicketOrder() {
        return ticketOrder;
    }

    public void setTicketOrder(TicketOrder newOrder) {
        if (this.ticketOrder == newOrder) {
            return;
        }

        if (this.ticketOrder != null) {
            TicketOrder old = this.ticketOrder;
            this.ticketOrder = null;
            if (old.getTickets().contains(this)) {
                old.getTickets().remove(this);
            }
        }
        this.ticketOrder = newOrder;
        if (newOrder != null && !newOrder.getTickets().contains(this)) {
            newOrder.getTickets().add(this);
        }
    }

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
    }

    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Ticket status");
        }
        String trimmed = status.trim();

        if (!trimmed.matches("(?i)AVAILABLE|RESERVED|SOLD|CANCELLED")) {
            throw new InvalidValueException("Invalid ticket status: " + trimmed);
        }
    }

    private void validatePrice(double price) {
        if (price < 0) {
            throw new NegativeNumberException("Price", price);
        }
    }

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
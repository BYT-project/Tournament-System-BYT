package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Viewer extends Person {
    private static final List<Viewer> extent = new ArrayList<>();
    private final List<Ticket> tickets = new ArrayList<>();


    private static void addViewer(Viewer v) {
        if (v == null) {
            throw new IllegalArgumentException("Viewer cannot be null");
        }
        extent.add(v);
    }

    public static List<Viewer> getExtent() {
        return new ArrayList<>(extent); // defensive copy
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(Viewer.class, extent);
    }

    public static void loadExtent() {
        List<Viewer> loaded = ExtentPersistence.loadExtent(Viewer.class);
        extent.clear();
        extent.addAll(loaded);
    }

    public List<Ticket> getTickets() {
        return Collections.unmodifiableList(tickets);
    }

    public void addTicket(Ticket ticket) {
        if (ticket == null) {
            throw new NullObjectException("Ticket");
        }
        if (tickets.contains(ticket)) {
            throw new InvalidValueException("Viewer already owns this ticket");
        }
        if (ticket.getViewer() != null && ticket.getViewer() != this) {
            throw new InvalidValueException("Ticket already belongs to another viewer");
        }

        tickets.add(ticket);

        if (ticket.getViewer() != this) {
            ticket.setViewer(this);
        }
    }

    public void removeTicket(Ticket ticket) {
        if (ticket == null) {
            throw new NullObjectException("Ticket");
        }
        if (!tickets.contains(ticket)) {
            throw new InvalidValueException("Viewer does not own this ticket");
        }

        tickets.remove(ticket);

        if (ticket.getViewer() == this) {
            ticket.setViewer(null);
        }
    }

    public Viewer(String firstName,
                  String lastName,
                  LocalDate dateOfBirth,
                  String email,
                  String phone) {
        super(firstName, lastName, dateOfBirth, email, phone);
        addViewer(this);
    }
    public Viewer() {
        super();
    }
}
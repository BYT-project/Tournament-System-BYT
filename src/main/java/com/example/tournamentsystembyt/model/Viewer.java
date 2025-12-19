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
    private Person person;



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

    public Coach changeToCoach(Coach newRole) {
        if (newRole == null) throw new IllegalArgumentException("Coach role cannot be null");
        Person p = getPerson();
        if (p == null) throw new IllegalStateException("Viewer is not attached to any Person");

        if (newRole.getPerson() != null && newRole.getPerson() != p)
            throw new IllegalStateException("Coach role already belongs to another Person");

        p.assignCoach(newRole);
        p.removeViewer();
        return newRole;
    }

    public Player changeToPlayer(Player newRole) {
        if (newRole == null) throw new IllegalArgumentException("Player role cannot be null");
        Person p = getPerson();
        if (p == null) throw new IllegalStateException("Viewer is not attached to any Person");

        if (newRole.getPerson() != null && newRole.getPerson() != p)
            throw new IllegalStateException("Player role already belongs to another Person");

        p.assignPlayer(newRole);
        p.removeViewer();
        return newRole;
    }

    public Referee changeToReferee(Referee newRole) {
        if (newRole == null) throw new IllegalArgumentException("Referee role cannot be null");
        Person p = getPerson();
        if (p == null) throw new IllegalStateException("Viewer is not attached to any Person");

        if (newRole.getPerson() != null && newRole.getPerson() != p)
            throw new IllegalStateException("Referee role already belongs to another Person");

        p.assignReferee(newRole);
        p.removeViewer();
        return newRole;
    }

    public Staff changeToStaff(Staff newRole) {
        if (newRole == null) throw new IllegalArgumentException("Staff role cannot be null");
        Person p = getPerson();
        if (p == null) throw new IllegalStateException("Viewer is not attached to any Person");

        if (newRole.getPerson() != null && newRole.getPerson() != p)
            throw new IllegalStateException("Staff role already belongs to another Person");

        p.assignStaff(newRole);
        p.removeViewer();
        return newRole;
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

    public void delete() {
        if (person == null) {
            return;
        }

        Person owner = person;
        owner.removeViewer();
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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
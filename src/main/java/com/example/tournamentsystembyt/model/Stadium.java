package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.util.*;

public class Stadium {

    private  String name;
    private int capacity;
    private String location;
    private static final List<Stadium> extent = new ArrayList<>();
    private Match match;


    //Qualified association
    private final Map<Integer, MatchTicket> ticketsBySeat = new HashMap<>();


    private static void addStadium(Stadium s) {
        if (s == null) throw new IllegalArgumentException("Stadium cannot be null");
        extent.add(s);
    }

    public static List<Stadium> getExtent() {
        return new ArrayList<>(extent); // encapsulation: return defensive copy
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(Stadium.class, extent);
    }

    public static void loadExtent() {
        List<Stadium> loaded = ExtentPersistence.loadExtent(Stadium.class);
        extent.clear();
        extent.addAll(loaded);
    }

    public Stadium(String name, int capacity, String location) {
        validateName(name);
        validateCapacity(capacity);
        validateLocation(location);

        this.name = name.trim();
        this.capacity = capacity;
        this.location = location.trim();
        addStadium(this);
    }
    public Stadium(){
    }


    public Match getMatch() {
        return match;
    }

    public void setMatch(Match newMatch) {
        if (this.match == newMatch) {
            return;
        }

        if (this.match != null) {
            Match old = this.match;
            this.match = null;
            if (old.getStadium() == this) {
                old.setStadium(null);
            }
        }

        this.match = newMatch;

        if (newMatch != null && newMatch.getStadium() != this) {
            newMatch.setStadium(this);
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Stadium name");
        }
        if (name.trim().length() < 2) {
            throw new InvalidValueException("Stadium name must contain at least 2 characters.");
        }
        if (!name.matches("[A-Za-zÀ-ÖØ-öø-ÿ0-9\\-\\s]+")) {
            throw new InvalidValueException("Stadium name contains invalid characters.");
        }
    }

    private void validateCapacity(int capacity) {
        if (capacity <= 0) {
            throw new NegativeNumberException("Capacity", capacity);
        }
        if (capacity < 1000) {
            throw new InvalidValueException("Capacity must be at least 1000 seats for a stadium.");
        }
        if (capacity > 150000) {
            throw new InvalidValueException("Capacity exceeds the maximum realistic size for a stadium.");
        }
    }

    private void validateLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Location");
        }
        if (location.trim().length() < 2) {
            throw new InvalidValueException("Location must contain at least 2 characters.");
        }
        if (!location.matches("[A-Za-zÀ-ÖØ-öø-ÿ0-9\\-\\s,]+")) {
            throw new InvalidValueException("Location contains invalid characters.");
        }
    }


    //Qualified association
    public void addMatchTicket(MatchTicket ticket) {
        if (ticket == null) {
            throw new InvalidValueException("Ticket cannot be null");
        }

        int seatNumber = ticket.getSeatNumber();
        if (seatNumber <= 0) {
            throw new InvalidValueException("Seat number must be positive");
        }

        if (seatNumber > this.getCapacity()) {
            throw new InvalidValueException("Seat number exceeds stadium capacity");
        }

        if (ticketsBySeat.containsKey(seatNumber)) {
            throw new InvalidValueException("Seat " + seatNumber + " is already taken in this stadium");
        }

        if (ticket.getStadium() != null && ticket.getStadium() != this) {
            throw new InvalidValueException("Ticket already belongs to another stadium");
        }

        // Delegate to ticket: it will update its own stadium reference
        // and also add itself to this stadium's ticketsBySeat map.
        ticket.setStadium(this);
    }

    public void removeMatchTicket(int seatNumber) {
        MatchTicket ticket = ticketsBySeat.get(seatNumber);
        if (ticket == null) {
            throw new InvalidValueException("No ticket found at seat " + seatNumber + " in this stadium");
        }

        // Ticket will remove itself from this stadium's map as part of setStadium(null)
        ticket.setStadium(null);
    }

    public MatchTicket findTicketBySeat(int seatNumber) {
        return ticketsBySeat.get(seatNumber);
    }


    public void setCapacity(int capacity) {
        validateCapacity(capacity);
        this.capacity = capacity;
    }

    public void setLocation(String location) {
        validateLocation(location);
        this.location = location.trim();
    }

    public Map<Integer, MatchTicket> getTicketsBySeat() {
        return ticketsBySeat;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLocation() {
        return location;
    }
}
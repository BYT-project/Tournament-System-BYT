package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.util.ArrayList;
import java.util.List;

public class TournamentTicket extends Ticket {

    private Stadium stadium;
    private Integer seatNumber; // 0..1 (optional)

    private static final List<TournamentTicket> extent = new ArrayList<>();

    private static void addTournamentTicket(TournamentTicket t) {
        if (t == null) throw new IllegalArgumentException("TournamentTicket cannot be null");
        extent.add(t);
    }

    public static List<Ticket> getExtent() {
        return new ArrayList<>(extent); // defensive copy
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(TournamentTicket.class, extent);
    }

    public static void loadExtent() {
        List<TournamentTicket> loaded = ExtentPersistence.loadExtent(TournamentTicket.class);
        extent.clear();
        extent.addAll(loaded);
    }

    public TournamentTicket(String id,
                            double price,
                            String status,
                            Stadium stadium,
                            Integer seatNumber) {

        super(id, price, "TOURNAMENT", status);

        setStadium(stadium);
        setSeatNumber(seatNumber); // may be null
        addTournamentTicket(this);
    }

    public TournamentTicket() {
        super();
    }

    private void validateSeatNumber(Integer seatNumber) {
        if (seatNumber == null) return; // optional
        if (seatNumber <= 0) {
            throw new NegativeNumberException("Seat number", seatNumber);
        }
        if (stadium != null && seatNumber > stadium.getCapacity()) {
            throw new InvalidValueException(
                    "Seat number must be between 1 and " + stadium.getCapacity() + "."
            );
        }
    }

    public Stadium getStadium() {
        return stadium;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setStadium(Stadium stadium) {
        if (stadium == null) {
            throw new NullObjectException("Stadium");
        }
        this.stadium = stadium;

        // If seat is already chosen, re-validate it with the new stadium
        validateSeatNumber(this.seatNumber);
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
        validateSeatNumber(this.seatNumber);
    }
}
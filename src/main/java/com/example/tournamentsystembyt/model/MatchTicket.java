package com.example.tournamentsystembyt.model;

public class MatchTicket extends Ticket {
    private final Stadium stadium;
    private final int seatNumber;
    public MatchTicket(String id, double price, String status, Stadium stadium, int seatNumber) {
        super(id, price, "MATCH", status);
        if (stadium == null) {
            throw new IllegalArgumentException("Stadium cannot be null");
        }
        validateSeatNumber(seatNumber);
        this.stadium = stadium;
        this.seatNumber = seatNumber;
    }
    public Stadium getStadium() {
        return stadium;
    }
    public int getSeatNumber() {
        return seatNumber;
    }
    private void validateSeatNumber(int seatNumber) {
        if (seatNumber <= 0 || seatNumber > stadium.getCapacity()) {
            throw new IllegalArgumentException("Seat number must be between 1 and " + stadium.getCapacity());
        }
    }
}
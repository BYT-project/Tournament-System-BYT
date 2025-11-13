package com.example.tournamentsystembyt.model;

public class TournamentTicket extends Ticket {
    private  Stadium stadium;
    private Integer seatNumber;

    public TournamentTicket(String id, double price, String status, Stadium stadium, Integer seatNumber) {
        super(id, price, "TOURNAMENT", status);
        if (stadium == null) {
            throw new IllegalArgumentException("Stadium cannot be empty");
        }
        if (seatNumber == null) {
            throw new IllegalArgumentException("Seat number cannot be empty");
        }
        this.stadium = stadium;
        this.seatNumber = seatNumber;
    }

    public Stadium getStadium() {
        return stadium;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }
    public void setStadium(Stadium stadium) {
        if (stadium == null) {
            throw new IllegalArgumentException("Stadium cannot be empty");
        }
        this.stadium = stadium;
    }
    public void setSeatNumber(Integer seatNumber) {
        if (seatNumber == null) {
            throw new IllegalArgumentException("Seat number cannot be empty");
        }
        this.seatNumber = seatNumber;
    }
    
}

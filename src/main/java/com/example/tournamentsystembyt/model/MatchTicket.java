package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;

import java.util.Map;

public class MatchTicket extends Ticket {

    private  Stadium stadium;
    private  int seatNumber;

    public MatchTicket(String id,
                       double price,
                       String status,
                       Stadium stadium,
                       int seatNumber) {

        super(id, price, "MATCH", status);

        if (stadium == null) {
            throw new NullObjectException("Stadium");
        }
        this.stadium = stadium;

        validateSeatNumber(seatNumber);
        this.seatNumber = seatNumber;
    }

    private void validateSeatNumber(int seatNumber) {
        if (seatNumber <= 0) {
            throw new NegativeNumberException("Seat number", seatNumber);
        }
        if (seatNumber > stadium.getCapacity()) {
            throw new InvalidValueException(
                    "Seat number must be between 1 and " + stadium.getCapacity() + "."
            );
        }
    }


    //Qualified association
    public void setStadium(Stadium newStadium) {
        // If nothing changes, do nothing
        if (this.stadium == newStadium) {
            return;
        }

        if (this.stadium != null) {
            Map<Integer, MatchTicket> oldMap = this.stadium.getTicketsBySeat();
            Integer keyToRemove = null;
            for (Map.Entry<Integer, MatchTicket> entry : oldMap.entrySet()) {
                if (entry.getValue() == this) {
                    keyToRemove = entry.getKey();
                    break;
                }
            }
            if (keyToRemove != null) {
                oldMap.remove(keyToRemove);
            }
        }

        this.stadium = newStadium;

        if (newStadium != null) {
            Map<Integer, MatchTicket> newMap = newStadium.getTicketsBySeat();
            newMap.put(this.seatNumber, this);
        }
    }

    public void setSeatNumber(int seatNumber) {
        if (seatNumber <= 0) {
            throw new IllegalArgumentException("Seat number must be positive");
        }

        if (stadium != null) {
            throw new IllegalStateException(
                    "Cannot change seat number while ticket is assigned to a stadium. " +
                            "Remove it from the stadium first."
            );
        }

        this.seatNumber = seatNumber;
    }

    public Stadium getStadium() {
        return stadium;
    }

    public int getSeatNumber() {
        return seatNumber;
    }
}


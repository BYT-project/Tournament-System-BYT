package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;

public class MatchTicket extends Ticket {

    private final Stadium stadium;
    private final int seatNumber;

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

    public Stadium getStadium() {
        return stadium;
    }

    public int getSeatNumber() {
        return seatNumber;
    }
}
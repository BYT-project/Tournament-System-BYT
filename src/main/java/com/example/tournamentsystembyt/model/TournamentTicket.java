package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;

public class TournamentTicket extends Ticket {

    private Stadium stadium;
    private Integer seatNumber; // 0..1 (optional)

    public TournamentTicket(String id,
                            double price,
                            String status,
                            Stadium stadium,
                            Integer seatNumber) {

        super(id, price, "TOURNAMENT", status);

        setStadium(stadium);
        setSeatNumber(seatNumber); // may be null
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

    public Integer getSeatNumber() {
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
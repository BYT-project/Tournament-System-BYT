package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

public class PhysicalTicket {

    private final Ticket ticket;
    private String barcode;

    public PhysicalTicket(Ticket ticket, String barcode) {
        if (ticket == null) {
            throw new NullObjectException("Ticket");
        }
        this.ticket = ticket;
        setBarcode(barcode);
    }

    public void setBarcode(String barcode) {
        if (barcode == null || barcode.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Barcode");
        }
        String trimmed = barcode.trim();
        if (trimmed.length() < 6) {
            throw new InvalidValueException("Barcode must contain at least 6 characters.");
        }
        this.barcode = trimmed;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public String getBarcode() {
        return barcode;
    }
}
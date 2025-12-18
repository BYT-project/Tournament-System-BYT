package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

public class PhysicalTicket {

    private Ticket ticket;
    private String barcode;

    public PhysicalTicket(Ticket ticket, String barcode) {
        if (ticket == null) {
            throw new NullObjectException("Ticket");
        }
        this.ticket = ticket;
        setBarcode(barcode);
    }
    public PhysicalTicket(String barcode) {
        if(ticket == null){
            throw new NullObjectException("Ticket");
        }
        setBarcode(barcode);
        setTicket(ticket);
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
    void setTicket(Ticket newTicket) {
        if (this.ticket == newTicket) return;

        if (this.ticket != null) {
            Ticket old = this.ticket;
            this.ticket = null;
            if (old.getPhysicalTicket() == this) {
                old.setPhysicalTicket(null);
            }
        }

        this.ticket = newTicket;
        if (newTicket != null && newTicket.getPhysicalTicket() != this) {
            newTicket.setPhysicalTicket(this);
        
    }
}
    public void changeToDigitalTicket(String downloadLink, String qrCode) {
        if (ticket == null) {
            throw new IllegalStateException("PhysicalTicket is not attached to any Ticket");
        }

        if (ticket.getDigitalTicket() == null) {
            ticket.setDigitalTicket(new DigitalTicket(downloadLink, qrCode));
        }
    }

    public Ticket getTicket() {
        return ticket;
    }

    public String getBarcode() {
        return barcode;
    }
}
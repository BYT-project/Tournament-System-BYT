package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

public class DigitalTicket {

    private Ticket ticket;
    private String downloadLink;
    private String qrCode;

    public DigitalTicket(Ticket ticket, String downloadLink, String qrCode) {
        if (ticket == null) {
            throw new NullObjectException("Ticket");
        }
        this.ticket = ticket;
        setDownloadLink(downloadLink);
        setQrCode(qrCode);
    }
    public DigitalTicket(String downloadLink, String qrCode) {
        if(ticket == null){
            throw new NullObjectException("Ticket");
        }
        setDownloadLink(downloadLink);
        setQrCode(qrCode);
        setTicket(ticket);
    }

    public void setDownloadLink(String downloadLink) {
        if (downloadLink == null || downloadLink.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Download link");
        }
        String trimmed = downloadLink.trim();
        if (!trimmed.matches("^(https?://).+")) {
            throw new InvalidValueException("Download link must start with http:// or https://");
        }
        this.downloadLink = trimmed;
    }

    public void setQrCode(String qrCode) {
        if (qrCode == null || qrCode.trim().isEmpty()) {
            throw new NullOrEmptyStringException("QR-code");
        }
        String trimmed = qrCode.trim();
        if (trimmed.length() < 6) {
            throw new InvalidValueException("QR-code must contain at least 6 characters.");
        }
        this.qrCode = trimmed;
    }
    void setTicket(Ticket newTicket) {
        if (this.ticket == newTicket) return;

        // detach from old parent
        if (this.ticket != null) {
            Ticket old = this.ticket;
            this.ticket = null;
            if (old.getDigitalTicket() == this) {
                old.setDigitalTicket(null);
            }
        }

        // attach to new parent
        this.ticket = newTicket;
        if (newTicket != null && newTicket.getDigitalTicket() != this) {
            newTicket.setDigitalTicket(this);
        }
    }

    public void changeToPhysicalTicket(String barcode) {
        if (ticket == null) {
            throw new IllegalStateException("DigitalTicket is not attached to any Ticket");
        }

        if (ticket.getPhysicalTicket() == null) {
            ticket.setPhysicalTicket(new PhysicalTicket(barcode));
        }
    }

    public Ticket getTicket() {
        return ticket;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public String getQrCode() {
        return qrCode;
    }
}
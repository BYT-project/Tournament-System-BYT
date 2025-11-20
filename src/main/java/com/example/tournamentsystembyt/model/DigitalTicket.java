package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

public class DigitalTicket {

    private final Ticket ticket;
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

    public void setDownloadLink(String downloadLink) {
        if (downloadLink == null || downloadLink.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Download link");
        }
        String trimmed = downloadLink.trim();
        // Very simple URL-like check
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
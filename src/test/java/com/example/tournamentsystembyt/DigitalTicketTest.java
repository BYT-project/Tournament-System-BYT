package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.model.DigitalTicket;
import com.example.tournamentsystembyt.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DigitalTicketTest {

    private Ticket ticket;
    private DigitalTicket digitalTicket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket("123", 50.0, "Standard", "AVAILABLE");
        digitalTicket = new DigitalTicket(ticket, "http://example.com/ticket", "QRCODE123456");
    }

    @Test
    void testDigitalTicketCreation() {
        assertEquals(ticket, digitalTicket.getTicket());
        assertEquals("http://example.com/ticket", digitalTicket.getDownloadLink());
        assertEquals("QRCODE123456", digitalTicket.getQrCode());
    }

    @Test
    void testNullTicket() {
        assertThrows(NullObjectException.class, () -> new DigitalTicket(null, "http://example.com/ticket", "QRCODE123456"));
    }

    @Test
    void testSetEmptyDownloadLink() {
        assertThrows(NullOrEmptyStringException.class, () -> digitalTicket.setDownloadLink(""));
        assertThrows(NullOrEmptyStringException.class, () -> digitalTicket.setDownloadLink(" "));
    }

    @Test
    void testSetInvalidDownloadLink() {
        assertThrows(InvalidValueException.class, () -> digitalTicket.setDownloadLink("example.com/ticket"));
    }

    @Test
    void testSetEmptyQrCode() {
        assertThrows(NullOrEmptyStringException.class, () -> digitalTicket.setQrCode(""));
        assertThrows(NullOrEmptyStringException.class, () -> digitalTicket.setQrCode(" "));
    }

    @Test
    void testSetInvalidQrCode() {
        assertThrows(InvalidValueException.class, () -> digitalTicket.setQrCode("12345"));
    }
}
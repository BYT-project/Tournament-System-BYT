package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.model.PhysicalTicket;
import com.example.tournamentsystembyt.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhysicalTicketTest {

    private Ticket ticket;
    private PhysicalTicket physicalTicket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket("123", 50.0, "Standard", "AVAILABLE");
        physicalTicket = new PhysicalTicket(ticket, "BARCODE123456");
    }

    @Test
    void testPhysicalTicketCreation() {
        assertEquals(ticket, physicalTicket.getTicket());
        assertEquals("BARCODE123456", physicalTicket.getBarcode());
    }

    @Test
    void testNullTicket() {
        assertThrows(NullObjectException.class, () -> new PhysicalTicket(null, "BARCODE123456"));
    }

    @Test
    void testSetEmptyBarcode() {
        assertThrows(NullOrEmptyStringException.class, () -> physicalTicket.setBarcode(""));
        assertThrows(NullOrEmptyStringException.class, () -> physicalTicket.setBarcode(" "));
    }

    @Test
    void testSetInvalidBarcode() {
        assertThrows(InvalidValueException.class, () -> physicalTicket.setBarcode("12345"));
    }
}
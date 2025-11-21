package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.model.MatchTicket;
import com.example.tournamentsystembyt.model.Stadium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchTicketTest {

    private Stadium stadium;
    private MatchTicket matchTicket;

    @BeforeEach
    void setUp() {
        stadium = new Stadium("Test Stadium", 10000, "Test Location");
        matchTicket = new MatchTicket("123", 50.0, "AVAILABLE", stadium, 100);
    }

    @Test
    void testMatchTicketCreation() {
        assertEquals("123", matchTicket.getId());
        assertEquals(50.0, matchTicket.getPrice());
        assertEquals("MATCH", matchTicket.getType());
        assertEquals("AVAILABLE", matchTicket.getStatus());
        assertEquals(stadium, matchTicket.getStadium());
        assertEquals(100, matchTicket.getSeatNumber());
    }

    @Test
    void testNullStadium() {
        assertThrows(NullObjectException.class, () -> new MatchTicket("123", 50.0, "AVAILABLE", null, 100));
    }

    @Test
    void testInvalidSeatNumber() {
        assertThrows(NegativeNumberException.class, () -> new MatchTicket("123", 50.0, "AVAILABLE", stadium, 0));
        assertThrows(NegativeNumberException.class, () -> new MatchTicket("123", 50.0, "AVAILABLE", stadium, -1));
        assertThrows(InvalidValueException.class, () -> new MatchTicket("123", 50.0, "AVAILABLE", stadium, 10001));
    }
}
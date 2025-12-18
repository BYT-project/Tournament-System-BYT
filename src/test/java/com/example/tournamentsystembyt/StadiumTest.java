package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StadiumTest {

    private Stadium stadium;

    private Tournament tournament() {
        return new Tournament(
                "WC",
                "Football",
                new Date(System.currentTimeMillis() - 10000000),
                new Date(System.currentTimeMillis() - 1000),
                1000
        );
    }

    private Stage stage() {
        return new GroupStage(1, "Groups", 4, 4, tournament());
    }

    @BeforeEach
    void setUp() {
        stadium = new Stadium("Test Stadium", 10000, "Test Location");
    }

    @AfterEach
    void cleanUpExtent() {
        Stadium.clearExtent();
    }

    @Test
    void testStadiumCreation() {
        assertEquals("Test Stadium", stadium.getName());
        assertEquals(10000, stadium.getCapacity());
        assertEquals("Test Location", stadium.getLocation());
    }

    @Test
    void testSetInvalidCapacity() {
        assertThrows(Exception.class, () -> stadium.setCapacity(999));
        assertThrows(Exception.class, () -> stadium.setCapacity(150001));
    }

    @Test
    void testSetEmptyLocation() {
        assertThrows(Exception.class, () -> stadium.setLocation(""));
        assertThrows(Exception.class, () -> stadium.setLocation(" "));
    }

    private Stadium createStadium(int capacity) {
        return new Stadium("National Stadium", capacity, "Warsaw");
    }

    private MatchTicket createTicket(String id, Stadium stadium, int seatNumber) {
        return new MatchTicket(id, 100.0, "AVAILABLE", stadium, seatNumber);
    }

    @Test
    void addMatchTicket_throwsWhenTicketIsNull() {
        assertThrows(InvalidValueException.class,
                () -> stadium.addMatchTicket(null));
    }

    @Test
    void addMatchTicket_throwsWhenTicketBelongsToAnotherStadium() {
        Stadium s1 = createStadium(10000);
        Stadium s2 = createStadium(10000);
        MatchTicket ticket = createTicket("T1", s1, 1);

        s1.addMatchTicket(ticket);

        assertThrows(InvalidValueException.class,
                () -> s2.addMatchTicket(ticket));

        assertSame(s1, ticket.getStadium());
    }

    @Test
    void removeMatchTicket_throwsWhenSeatNotPresent() {
        MatchTicket t1 = createTicket("T1", stadium, 1);
        stadium.addMatchTicket(t1);

        assertThrows(InvalidValueException.class,
                () -> stadium.removeMatchTicket(2));
    }

    @Test
    void setSeatNumber_throwsWhenTicketAssignedToStadium() {
        MatchTicket ticket = createTicket("T1", stadium, 1);
        stadium.addMatchTicket(ticket);

        assertThrows(IllegalStateException.class,
                () -> ticket.setSeatNumber(5));

        assertEquals(1, ticket.getSeatNumber());
    }

    @Test
    void setMatchOnStadium_updatesBothSides() {
        Stage stage = stage();
        Match match = new Match(LocalDate.now(), LocalTime.NOON, "Scheduled", stage);

        match.setStadium(stadium);

        assertEquals(stadium, match.getStadium());
        assertEquals(match, stadium.getMatch());
    }

    @Test
    void setStadium_cannotReuseStadiumForAnotherMatch() {
        Stage stage = stage();
        Match match1 = new Match(LocalDate.now(), LocalTime.NOON, "Scheduled", stage);
        Match match2 = new Match(LocalDate.now(), LocalTime.MIDNIGHT, "Scheduled", stage);

        match1.setStadium(stadium);

        assertThrows(InvalidValueException.class,
                () -> match2.setStadium(stadium));
    }
}
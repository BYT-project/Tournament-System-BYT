package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TournamentTicketTest {

    private Tournament tournament;
    private Stadium stadium;
    private Match match;

    @BeforeEach
    void setup() {
        tournament = new Tournament(
                "World Cup",
                "Football",
                new Date(System.currentTimeMillis() - 10000000),
                new Date(System.currentTimeMillis() - 1000),
                1_000_000
        );

        stadium = new Stadium("Wembley", 90000, "London");

        Stage stage = new GroupStage(1, "Groups", 4, 4, tournament);
        match = new Match(LocalDate.now(), LocalTime.NOON, "Scheduled", stage);

        TournamentTicket.clearExtent();
    }

    @Test
    void ticketCreatedWithTournamentAndStadium() {
        TournamentTicket ticket =
                new TournamentTicket("T1", 50, "AVAILABLE", tournament, stadium, null);

        assertEquals(tournament, ticket.getTournament());
        assertEquals(stadium, ticket.getStadium());
        assertNull(ticket.getSeatNumber());
        assertTrue(TournamentTicket.getExtent().contains(ticket));
    }

    @Test
    void assignMatchCreatesReverseConnection() {
        TournamentTicket ticket =
                new TournamentTicket("T1", 50, "AVAILABLE", tournament, stadium, null);

        ticket.setMatch(match);

        assertEquals(match, ticket.getMatch());
        assertTrue(match.getTournamentTickets().contains(ticket));
    }

    @Test
    void ticketCannotMoveToAnotherMatch() {
        TournamentTicket ticket =
                new TournamentTicket("T1", 50, "AVAILABLE", tournament, stadium, null);

        ticket.setMatch(match);

        Match otherMatch = new Match(
                LocalDate.now(), LocalTime.NOON, "Scheduled",
                new GroupStage(2, "Groups", 4, 4, tournament)
        );

        assertThrows(InvalidValueException.class,
                () -> ticket.setMatch(otherMatch));
    }

    @Test
    void unassignMatchRemovesReverseConnection() {
        TournamentTicket ticket =
                new TournamentTicket("T1", 50, "AVAILABLE", tournament, stadium, null);

        ticket.setMatch(match);
        ticket.setMatch(null);

        assertNull(ticket.getMatch());
        assertFalse(match.getTournamentTickets().contains(ticket));
    }

    @Test
    void seatNumberCannotBeAssignedBeforeMatch() {
        TournamentTicket ticket =
                new TournamentTicket("T1", 50, "AVAILABLE", tournament, stadium, null);

        assertThrows(InvalidValueException.class,
                () -> ticket.setSeatNumber(10));
    }

    @Test
    void seatNumberCannotBeNegativeOrZero() {
        TournamentTicket ticket =
                new TournamentTicket("T1", 50, "AVAILABLE", tournament, stadium, null);

        ticket.setMatch(match);

        assertThrows(NegativeNumberException.class, () -> ticket.setSeatNumber(0));
        assertThrows(NegativeNumberException.class, () -> ticket.setSeatNumber(-5));
    }

    @Test
    void seatNumberMustNotExceedStadiumCapacity() {
        TournamentTicket ticket =
                new TournamentTicket("T1", 50, "AVAILABLE", tournament, stadium, null);

        ticket.setMatch(match);

        assertThrows(InvalidValueException.class,
                () -> ticket.setSeatNumber(100_000)); // stadium capacity 90,000
    }

    @Test
    void seatNumberAllowedWhenValid() {
        TournamentTicket ticket =
                new TournamentTicket("T1", 50, "AVAILABLE", tournament, stadium, null);

        ticket.setMatch(match);
        ticket.setSeatNumber(500);

        assertEquals(500, ticket.getSeatNumber());
    }

    @Test
    void deletingTournamentDeletesTickets() {
        TournamentTicket t1 =
                new TournamentTicket("A1", 50, "AVAILABLE", tournament, stadium, null);
        TournamentTicket t2 =
                new TournamentTicket("B1", 50, "AVAILABLE", tournament, stadium, null);

        assertTrue(tournament.getTournamentTickets().contains(t1));
        assertTrue(tournament.getTournamentTickets().contains(t2));

        tournament.delete();

        assertFalse(TournamentTicket.getExtent().contains(t1));
        assertFalse(TournamentTicket.getExtent().contains(t2));
    }

    @Test
    void deletingMatchUnassignsTicket() {
        TournamentTicket ticket =
                new TournamentTicket("T1", 50, "AVAILABLE", tournament, stadium, null);

        ticket.setMatch(match);

        match.delete();

        assertNull(ticket.getMatch());
    }
}
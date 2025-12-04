package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    private Stage stage;
    private Match match;

    private Tournament tournament() {
        return new Tournament("World Cup", "Football",
                new Date(System.currentTimeMillis() - 10000000),
                new Date(System.currentTimeMillis() - 1000),
                1_000_000);
    }

    @BeforeEach
    void setUp() {
        Tournament t = tournament();
        stage = new GroupStage(1, "Groups", 4, 4, t);
        match = new Match(LocalDate.now(), LocalTime.NOON, "Scheduled", stage);
    }

    // OLD TESTS

    @Test
    void rejectsNullDateOrTimeOrStage() {
        assertThrows(NullObjectException.class,
                () -> new Match(null, LocalTime.NOON, "Scheduled", stage));

        assertThrows(NullObjectException.class,
                () -> new Match(LocalDate.now(), null, "Scheduled", stage));

        assertThrows(NullObjectException.class,
                () -> new Match(LocalDate.now(), LocalTime.NOON, "Scheduled", null));
    }

    @Test
    void status_cannotBeEmpty() {
        assertThrows(InvalidValueException.class,
                () -> new Match(LocalDate.now(), LocalTime.NOON, " ", stage));
    }

    @Test
    void testStartMatch() {
        match.start();
        assertEquals("Ongoing", match.getStatus());
    }

    @Test
    void testCannotStartMatchIfNotScheduled() {
        match.start();
        assertThrows(InvalidValueException.class, () -> match.start());
    }

    @Test
    void testPauseMatch() {
        match.start();
        match.pause();
        assertEquals("Paused", match.getStatus());
    }

    @Test
    void testCannotPauseMatchIfNotOngoing() {
        assertThrows(InvalidValueException.class, () -> match.pause());
    }

    @Test
    void testEndMatchFromOngoing() {
        match.start();
        match.setResults(1, 0, 1);
        match.end();
        assertEquals("Finished", match.getStatus());
    }

    @Test
    void testEndMatchFromPaused() {
        match.start();
        match.pause();
        match.setResults(1, 0, 1);
        match.end();
        assertEquals("Finished", match.getStatus());
    }

    @Test
    void testCannotEndMatchIfNotOngoingOrPaused() {
        assertThrows(InvalidValueException.class, () -> match.end());
    }

    @Test
    void testCannotEndMatchWithoutScores() {
        match.start();
        assertThrows(InvalidValueException.class, () -> match.end());
    }

    @Test
    void testCancelMatch() {
        match.cancelMatch();
        assertEquals("Cancelled", match.getStatus());
    }

    @Test
    void testCannotCancelFinishedMatch() {
        match.start();
        match.setResults(1, 0, 1);
        match.end();
        assertThrows(InvalidValueException.class, () -> match.cancelMatch());
    }

    @Test
    void testSetResultsWithNegativeScores() {
        assertThrows(NegativeNumberException.class, () -> match.setResults(-1, 0, 1));
        assertThrows(NegativeNumberException.class, () -> match.setResults(0, -1, 1));
    }

    @Test
    void testSetResultsWithInvalidWinnerId() {
        assertThrows(InvalidValueException.class, () -> match.setResults(1, 0, 0));
        assertThrows(InvalidValueException.class, () -> match.setResults(1, 0, -1));
    }

    @Test
    void testSetResultsWithDrawAndWinner() {
        assertThrows(InvalidValueException.class, () -> match.setResults(1, 1, 1));
    }

    @Test
    void testSetValidResult() {
        match.setResults(2, 1, 5);
        assertEquals(2, match.getHomeScore());
        assertEquals(1, match.getAwayScore());
        assertEquals(5, match.getWinnerTeamId());
    }

    // REVERSE TESTS

    @Test
    void matchAddedToStageReverseConnection() {
        assertTrue(stage.getMatches().contains(match));
        assertEquals(stage, match.getStage());
    }

    @Test
    void deletingStageDeletesMatches() {
        stage.delete();
        assertTrue(stage.getMatches().isEmpty());
    }

    @Test
    void removingMatchFromStageDeletesMatch() {
        Match m2 = new Match(LocalDate.now(), LocalTime.MIDNIGHT, "Scheduled", stage);

        assertTrue(stage.getMatches().contains(m2));

        stage.removeMatch(m2);

        assertFalse(stage.getMatches().contains(m2));
    }

    // NEW TESTS — MATCH ↔ TOURNAMENT TICKET ASSOCIATION

    @Test
    void assigningTicketToMatchCreatesReverseConnection() {
        Stadium stadium = new Stadium("Wembley", 90000, "London");
        Tournament t = tournament();

        TournamentTicket ticket = new TournamentTicket(
                "T1", 50, "AVAILABLE", t, stadium, null);

        ticket.setMatch(match);

        assertTrue(match.getTournamentTickets().contains(ticket));
        assertEquals(match, ticket.getMatch());
    }

    @Test
    void ticketCannotMoveToAnotherMatch() {
        Stadium s = new Stadium("Wembley", 90000, "London");
        Tournament t = tournament();

        TournamentTicket ticket = new TournamentTicket("T1", 50, "AVAILABLE", t, s, null);

        ticket.setMatch(match);

        Match otherMatch = new Match(LocalDate.now(), LocalTime.NOON, "Scheduled", stage);

        assertThrows(InvalidValueException.class, () -> ticket.setMatch(otherMatch));
    }

    @Test
    void deletingMatchRemovesTicketMatchReference() {
        Stadium s = new Stadium("Wembley", 90000, "London");
        Tournament t = tournament();
        TournamentTicket ticket = new TournamentTicket("T1", 50, "AVAILABLE", t, s, null);

        ticket.setMatch(match);

        match.delete();

        assertNull(ticket.getMatch());
    }

    @Test
    void seatNumberCannotBeSetWithoutMatch() {
        Stadium s = new Stadium("Wembley", 2000, "London");
        Tournament t = tournament();
        TournamentTicket ticket = new TournamentTicket("T1", 50, "AVAILABLE", t, s, null);

        assertThrows(InvalidValueException.class,
                () -> ticket.setSeatNumber(15));
    }
}
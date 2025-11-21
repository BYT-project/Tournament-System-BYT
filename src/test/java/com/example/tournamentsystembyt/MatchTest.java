package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.model.GroupStage;
import com.example.tournamentsystembyt.model.Match;
import com.example.tournamentsystembyt.model.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    private Stage stage;
    private Match match;

    @BeforeEach
    void setUp() {
        stage = new GroupStage(1, "Groups", 4, 4);
        match = new Match(LocalDate.now(), LocalTime.NOON, "Scheduled", stage);
    }

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
        match.start(); // Status is now Ongoing
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
}
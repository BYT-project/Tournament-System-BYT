package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RefereeTest {

    private Referee referee;

    private Tournament tournament() {
        return new Tournament(
                "WC",
                "Football",
                new Date(System.currentTimeMillis() - 10000000),
                new Date(System.currentTimeMillis() - 1000),
                1000
        );
    }

    @BeforeEach
    void setUp() {
        referee = new Referee("John", "Doe", LocalDate.of(1980, 1, 1), "john.doe@example.com", "123456789", 10);
    }

    @Test
    void testRefereeCreation() {
        assertEquals("John", referee.getFirstName());
        assertEquals("Doe", referee.getLastName());
        assertEquals(LocalDate.of(1980, 1, 1), referee.getDateOfBirth());
        assertEquals("john.doe@example.com", referee.getEmail());
        assertEquals("123456789", referee.getPhone());
        assertEquals(10, referee.getExperience());
    }

    @Test
    void testSetNegativeExperience() {
        assertThrows(Exception.class, () -> referee.setExperience(-1));
    }

    @Test
    void testSetUnrealisticExperience() {
        assertThrows(Exception.class, () -> referee.setExperience(81));
    }
    @Test
    void addMatchToReferee_updatesBothSides() {
        Stage stage = new GroupStage(1, "Groups", 4, 4, tournament());
        Match match = new Match(LocalDate.now(), LocalTime.NOON, "Scheduled", stage);

        referee.addMatch(match);

        assertTrue(referee.getMatches().contains(match));
        assertTrue(match.getReferees().contains(referee));
    }
}
package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TeamTests {

    private Team team;

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
        team = new Team("Warriors", "USA", "San Francisco", 50);
    }

    @Test
    void rejectsEmptyName() {
        assertThrows(IllegalArgumentException.class,
                () -> new Team(" ", "USA", "San Francisco", 50));
    }

    @Test
    void rejectsEmptyCountry() {
        assertThrows(IllegalArgumentException.class,
                () -> new Team("Warriors", " ", "San Francisco", 50));
    }

    @Test
    void rejectsEmptyCity() {
        assertThrows(IllegalArgumentException.class,
                () -> new Team("Warriors", "USA", " ", 50));
    }

    @Test
    void rejectsNegativeRankPoints() {
        assertThrows(IllegalArgumentException.class,
                () -> new Team("Warriors", "USA", "San Francisco", -1));
    }

    @Test
    void addMatchFromTeam_updatesBothSides() {
        Stage stage = new GroupStage(1, "Groups", 4, 4, tournament());
        Match match = new Match(LocalDate.now(), LocalTime.NOON, "Scheduled", stage);

        team.addMatch(match);

        assertTrue(team.getMatches().contains(match));
        assertTrue(match.getTeams().contains(team));
    }

    @Test
    void removeMatchFromTeam_updatesBothSides() {
        Stage stage = new GroupStage(1, "Groups", 4, 4, tournament());
        Match match = new Match(LocalDate.now(), LocalTime.NOON, "Scheduled", stage);

        team.addMatch(match);
        team.removeMatch(match);

        assertFalse(team.getMatches().contains(match));
        assertFalse(match.getTeams().contains(team));
    }

    @Test
    void addNullMatchFromTeam_throwsNullObjectException() {
        assertThrows(NullObjectException.class, () -> team.addMatch(null));
    }
}
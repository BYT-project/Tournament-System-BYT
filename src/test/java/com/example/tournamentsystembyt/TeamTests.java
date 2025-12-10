package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TeamTests {

    private Team team;

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
    void canAddPlayer() {
        Player p = new Player("Steph", "Curry",
                LocalDate.now().minusYears(38), "stephcurry@mail.com", "303030",
                1.92, 82, "Point Guard", 30);

        team.getPlayers().add(p);
        assertEquals(1, team.getPlayers().size());
    }

    @Test
    void canAddCoach() {
        Coach c1 = new Coach("AB", "BC", LocalDate.now().minusYears(40), "a@a.com", "1123231", "Head", 10);
        team.getCoaches().add(c1);
        assertEquals(1, team.getCoaches().size());
    }

    @Test
    void addMatchFromTeam_updatesBothSides() {
        Stage stage = new GroupStage(1, "Groups", 4, 4);
        Match match = new Match(LocalDate.now(), LocalTime.NOON, "Scheduled", stage);

        team.addMatch(match);

        assertTrue(team.getMatches().contains(match));
        assertTrue(match.getTeams().contains(team));
    }

    @Test
    void removeMatchFromTeam_updatesBothSides() {
        Stage stage = new GroupStage(1, "Groups", 4, 4);
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
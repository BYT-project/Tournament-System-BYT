package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.Coach;
import com.example.tournamentsystembyt.model.Player;
import com.example.tournamentsystembyt.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
}
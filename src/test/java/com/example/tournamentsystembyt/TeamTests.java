package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.Coach;
import com.example.tournamentsystembyt.model.Player;
import com.example.tournamentsystembyt.model.Team;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    private Team validTeam() {
        return new Team("Warriors", "USA", "San Francisco", 50);
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
        Team team = validTeam();
        Player p = new Player("Steph", "Curry",
                LocalDate.now().minusYears(38), "stephcurry@mail.com", "303030",
                192, 82, "Point Guard", 30);

        team.addPlayer(p);
        assertEquals(1, team.getPlayers().size());
    }

    @Test
    void canAddCoachUntilLimit() {
        Team team = validTeam();

        Coach c1 = new Coach("A", "B", LocalDate.now().minusYears(40), "a@a.com", "111", "Head", 10);
        Coach c2 = new Coach("C", "D", LocalDate.now().minusYears(45), "c@c.com", "222", "Assistant", 8);

        team.addCoach(c1);
        team.addCoach(c2);

        assertEquals(2, team.getCoaches().size());
    }

    @Test
    void cannotAddMoreThanTwoCoaches() {
        Team team = validTeam();

        Coach c1 = new Coach("A", "B", LocalDate.now().minusYears(40), "a@a.com", "111", "Head", 10);
        Coach c2 = new Coach("C", "D", LocalDate.now().minusYears(45), "c@c.com", "222", "Assistant", 8);
        Coach c3 = new Coach("E", "F", LocalDate.now().minusYears(35), "e@e.com", "333", "Assistant", 5);

        team.addCoach(c1);
        team.addCoach(c2);

        assertThrows(IllegalArgumentException.class, () -> team.addCoach(c3));
    }
}
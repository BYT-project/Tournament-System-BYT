package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.Coach;
import com.example.tournamentsystembyt.model.Team;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CoachTest {

    private Coach validCoach() {
        return new Coach("Mike", "Smith",
                LocalDate.now().minusYears(40), "mike@mail.com", "555",
                "Head Coach", 10);
    }

    @Test
    void rejectsEmptyRole() {
        assertThrows(IllegalArgumentException.class,
                () -> new Coach("Mike", "Smith",
                        LocalDate.now().minusYears(40), "mike@mail.com", "555",
                        " ", 10));
    }

    @Test
    void rejectsNegativeExperience() {
        assertThrows(IllegalArgumentException.class,
                () -> new Coach("Mike", "Smith",
                        LocalDate.now().minusYears(40), "mike@mail.com", "555",
                        "Head Coach", -1));
    }

    @Test
    void canAddCoachedTeams() {
        Coach coach = validCoach();
        Team team = new Team("Wolves", "USA", "Minnesota", 100);

        coach.addTeam(team);
        assertEquals(1, coach.getTeamsCoached().size());
    }

    @Test
    void cannotAddNullTeam() {
        Coach coach = validCoach();
        assertThrows(IllegalArgumentException.class, () -> coach.addTeam(null));
    }
}
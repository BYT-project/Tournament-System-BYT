package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.Player;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player validPlayer() {
        return new Player("Max", "Power",
                LocalDate.now().minusYears(25), "max@mail.com", "123",
                180, 75, "Midfielder", 10);
    }

    @Test
    void rejectsNonPositiveHeight() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player("Max", "Power",
                        LocalDate.now().minusYears(25), "max@mail.com", "123",
                        0, 75, "Midfielder", 10));
    }

    @Test
    void rejectsNonPositiveWeight() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player("Max", "Power",
                        LocalDate.now().minusYears(25), "max@mail.com", "123",
                        180, -5, "Midfielder", 10));
    }

    @Test
    void rejectsEmptyPosition() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player("Max", "Power",
                        LocalDate.now().minusYears(25), "max@mail.com", "123",
                        180, 75, " ", 10));
    }

    @Test
    void rejectsNonPositiveShirtNumber() {
        assertThrows(IllegalArgumentException.class,
                () -> new Player("Max", "Power",
                        LocalDate.now().minusYears(25), "max@mail.com", "123",
                        180, 75, "Midfielder", 0));
    }
}
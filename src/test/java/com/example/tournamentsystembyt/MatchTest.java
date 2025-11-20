package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.model.GroupStage;
import com.example.tournamentsystembyt.model.Match;
import com.example.tournamentsystembyt.model.Stage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    private Stage stage() {
        return new GroupStage(1, "Groups", 4, 4);
    }

    @Test
    void rejectsNullDateOrTimeOrStage() {
        assertThrows(NullObjectException.class,
                () -> new Match(null, LocalTime.NOON, "Scheduled", stage()));

        assertThrows(NullObjectException.class,
                () -> new Match(LocalDate.now(), null, "Scheduled", stage()));

        assertThrows(NullObjectException.class,
                () -> new Match(LocalDate.now(), LocalTime.NOON, "Scheduled", null));
    }

    @Test
    void status_cannotBeEmpty() {
        assertThrows(InvalidValueException.class,
                () -> new Match(LocalDate.now(), LocalTime.NOON, " ", stage()));
    }
}
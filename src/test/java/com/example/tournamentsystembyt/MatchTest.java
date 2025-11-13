package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.model.Match;
import com.example.tournamentsystembyt.model.Stage;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    private Stage stage() { return new Stage(1, "Groups"); }

    @Test
    void rejectsNullDateOrTimeOrStage() {
        assertThrows(InvalidValueException.class,
                () -> new Match(null, LocalTime.NOON, "Scheduled", stage()));
        assertThrows(InvalidValueException.class,
                () -> new Match(LocalDate.now(), null, "Scheduled", stage()));
        assertThrows(InvalidValueException.class,
                () -> new Match(LocalDate.now(), LocalTime.NOON, "Scheduled", null));
    }

    @Test
    void status_cannotBeEmpty() {
        assertThrows(InvalidValueException.class,
                () -> new Match(LocalDate.now(), LocalTime.NOON, " ", stage()));
    }


}

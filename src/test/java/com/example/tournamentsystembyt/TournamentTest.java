package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.model.Stage;
import com.example.tournamentsystembyt.model.Tournament;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TournamentTest {

    private Date past(long days) { return new Date(System.currentTimeMillis() - days * 86_400_000L); }
    private Date future(long days) { return new Date(System.currentTimeMillis() + days * 86_400_000L); }

    private Tournament validTournament() {
        return new Tournament("World Cup", "Football", past(30), past(1), 1_000_000d);
    }

    @Test
    void rejectsEmptyName() {
        assertThrows(InvalidValueException.class,
                () -> new Tournament("  ", "Football", past(10), past(1), 1000d));
    }

    @Test
    void rejectsEmptySportType() {
        assertThrows(InvalidValueException.class,
                () -> new Tournament("WC", " ", past(10), past(1), 1000d));
    }

    @Test
    void startDate_cannotBeFuture() {
        assertThrows(InvalidValueException.class,
                () -> new Tournament("WC", "Football", future(1), future(2), 1000d));
    }

    @Test
    void endDate_cannotBeBeforeStartDate() {
        Date start = past(1);
        Date end = past(30);
        assertThrows(InvalidValueException.class,
                () -> new Tournament("WC", "Football", start, end, 1000d));
    }

    @Test
    void prizePool_cannotBeNegative() {
        assertThrows(InvalidValueException.class,
                () -> new Tournament("WC", "Football", past(10), past(1), -1));
    }

    @Test
    void registrationFee_cannotBeNegative() {
        Tournament t = validTournament();
        assertThrows(InvalidValueException.class, () -> t.setRegistrationFee(-5d));
    }

    @Test
    void setChampion_rejectsEmpty() {
        Tournament t = validTournament();
        assertThrows(InvalidValueException.class, () -> t.setChampion(" "));
    }



}

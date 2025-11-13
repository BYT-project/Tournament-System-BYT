package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.Referee;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RefereeTest {

    @Test
    void rejectsNegativeExperience() {
        assertThrows(IllegalArgumentException.class,
                () -> new Referee("John", "Ref",
                        LocalDate.now().minusYears(30), "ref@mail.com", "222",
                        -1));
    }

    @Test
    void validExperienceAccepted() {
        Referee ref = new Referee("John", "Ref",
                LocalDate.now().minusYears(30), "ref@mail.com", "222",
                5);
        assertEquals(5, ref.getExperience());
    }
}
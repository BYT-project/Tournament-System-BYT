package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.Referee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RefereeTest {

    private Referee referee;

    @BeforeEach
    void setUp() {
        referee = new Referee("John", "Doe", LocalDate.of(1980, 1, 1), "john.doe@example.com", "123456789", 10);
    }

    @Test
    void testRefereeCreation() {
        assertEquals("John", referee.getFirstName());
        assertEquals("Doe", referee.getLastName());
        assertEquals(LocalDate.of(1980, 1, 1), referee.getDateOfBirth());
        assertEquals("john.doe@example.com", referee.getEmail());
        assertEquals("123456789", referee.getPhone());
        assertEquals(10, referee.getExperience());
    }

    @Test
    void testSetNegativeExperience() {
        assertThrows(Exception.class, () -> referee.setExperience(-1));
    }

    @Test
    void testSetUnrealisticExperience() {
        assertThrows(Exception.class, () -> referee.setExperience(81));
    }
}
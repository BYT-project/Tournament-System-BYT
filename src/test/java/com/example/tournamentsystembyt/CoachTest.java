package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.model.Coach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CoachTest {

    private Coach coach;

    @BeforeEach
    void setUp() {
        coach = new Coach("John", "Doe", LocalDate.of(1980, 1, 1), "john.doe@example.com", "123456789", "Head Coach", 10);
    }

    @Test
    void testCoachCreation() {
        assertEquals("John", coach.getFirstName());
        assertEquals("Doe", coach.getLastName());
        assertEquals(LocalDate.of(1980, 1, 1), coach.getDateOfBirth());
        assertEquals("john.doe@example.com", coach.getEmail());
        assertEquals("123456789", coach.getPhone());
        assertEquals("Head Coach", coach.getRole());
        assertEquals(10, coach.getExperience());
    }

    @Test
    void testSetEmptyRole() {
        assertThrows(NullOrEmptyStringException.class, () -> coach.setRole(""));
        assertThrows(NullOrEmptyStringException.class, () -> coach.setRole(" "));
    }

    @Test
    void testSetNegativeExperience() {
        assertThrows(NegativeNumberException.class, () -> coach.setExperience(-1));
    }
}
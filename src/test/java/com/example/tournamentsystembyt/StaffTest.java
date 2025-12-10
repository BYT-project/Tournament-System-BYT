package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.model.Staff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.model.Match;
import com.example.tournamentsystembyt.model.Stage;
import com.example.tournamentsystembyt.model.GroupStage;
import java.time.LocalTime;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StaffTest {

    private Staff staff;

    @BeforeEach
    void setUp() {
        staff = new Staff("John", "Doe", LocalDate.of(1990, 1, 1), "john.doe@example.com", "123456789", "Manager", 50000);
    }

    @Test
    void testStaffCreation() {
        assertEquals("John", staff.getFirstName());
        assertEquals("Doe", staff.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), staff.getDateOfBirth());
        assertEquals("john.doe@example.com", staff.getEmail());
        assertEquals("123456789", staff.getPhone());
        assertEquals(50000, staff.getSalary());
    }


    @Test
    void testSetNegativeSalary() {
        assertThrows(NegativeNumberException.class, () -> staff.setSalary(-1));
    }

    @Test
    void addMatchToStaff_updatesBothSides() {
        Stage stage = new GroupStage(1, "Groups", 4, 4);
        Match match = new Match(LocalDate.now(), LocalTime.NOON, "Scheduled", stage);

        staff.addMatch(match);

        assertTrue(staff.getMatches().contains(match));
        assertTrue(match.getStaffMembers().contains(staff));
    }

    @Test
    void removeMatchFromStaff_respectsMultiplicity() {
        Stage stage = new GroupStage(1, "Groups", 4, 4);
        Match m1 = new Match(LocalDate.now(), LocalTime.NOON, "Scheduled", stage);
        Match m2 = new Match(LocalDate.now(), LocalTime.MIDNIGHT, "Scheduled", stage);

        staff.addMatch(m1);
        staff.addMatch(m2);

        staff.removeMatch(m2);
        assertFalse(staff.getMatches().contains(m2));
        assertFalse(m2.getStaffMembers().contains(staff));
        assertTrue(staff.getMatches().contains(m1));

        assertThrows(InvalidValueException.class, () -> staff.removeMatch(m1));
    }

}
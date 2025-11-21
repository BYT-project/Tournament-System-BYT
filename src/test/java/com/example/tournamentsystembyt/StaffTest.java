package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.model.Staff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.Staff;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class StaffTest {

    private Staff validStaff() {
        return new Staff("Jane", "Doe",
                LocalDate.now().minusYears(28), "jane@mail.com", "111",
                "Manager", 3000);
    }

    @Test
    void rejectsEmptyJobTitle() {
        assertThrows(IllegalArgumentException.class,
                () -> new Staff("Jane", "Doe",
                        LocalDate.now().minusYears(28), "jane@mail.com", "111",
                        " ", 3000));
    }

    @Test
    void rejectsNegativeSalary() {
        assertThrows(IllegalArgumentException.class,
                () -> new Staff("Jane", "Doe",
                        LocalDate.now().minusYears(28), "jane@mail.com", "111",
                        "Manager", -10));
    }

    @Test
    void validSalaryAccepted() {
        Staff s = validStaff();
        assertEquals(3000, s.getSalary());
    }
}
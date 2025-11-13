package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.model.Person;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    static class TestPerson extends Person {
        public TestPerson(String first, String last, LocalDate dob, String email, String phone) {
            super(first, last, dob, email, phone);
        }
    }

    private TestPerson validPerson() {
        return new TestPerson("John", "Doe", LocalDate.now().minusYears(20), "john@example.com", "123456789");
    }

    @Test
    void rejectsEmptyFirstName() {
        assertThrows(InvalidValueException.class,
                () -> new TestPerson(" ", "Doe", LocalDate.now().minusYears(20), "john@example.com", "123"));
    }

    @Test
    void rejectsEmptyLastName() {
        assertThrows(InvalidValueException.class,
                () -> new TestPerson("John", "  ", LocalDate.now().minusYears(20), "john@example.com", "123"));
    }

    @Test
    void rejectsFutureDateOfBirth() {
        assertThrows(InvalidValueException.class,
                () -> new TestPerson("John", "Doe", LocalDate.now().plusDays(1), "john@example.com", "123"));
    }

    @Test
    void rejectsInvalidEmail() {
        assertThrows(InvalidValueException.class,
                () -> new TestPerson("John", "Doe", LocalDate.now().minusYears(20), "invalidEmail", "123"));
    }

    @Test
    void ageIsCalculatedCorrectly() {
        TestPerson p = new TestPerson("John", "Doe", LocalDate.now().minusYears(30), "john@example.com", "123");
        assertEquals(30, p.getAge());
    }

    @Test
    void rejectsEmptyPhone() {
        assertThrows(InvalidValueException.class,
                () -> new TestPerson("John", "Doe", LocalDate.now().minusYears(20), "john@example.com", " "));
    }
}
package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidDateException;
import com.example.tournamentsystembyt.exceptions.InvalidEmailException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.model.Person;
import com.example.tournamentsystembyt.model.Viewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Viewer("John", "Doe", LocalDate.of(1990, 1, 1), "john.doe@example.com", "123456789");
    }

    @Test
    void testPersonCreation() {
        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), person.getDateOfBirth());
        assertEquals("john.doe@example.com", person.getEmail());
        assertEquals("123456789", person.getPhone());
    }

    @Test
    void testSetEmptyFirstName() {
        assertThrows(NullOrEmptyStringException.class, () -> person.setFirstName(""));
        assertThrows(NullOrEmptyStringException.class, () -> person.setFirstName(" "));
    }

    @Test
    void testSetEmptyLastName() {
        assertThrows(NullOrEmptyStringException.class, () -> person.setLastName(""));
        assertThrows(NullOrEmptyStringException.class, () -> person.setLastName(" "));
    }

    @Test
    void testSetInvalidBirthDate() {
        assertThrows(InvalidDateException.class, () -> person.setDateOfBirth(LocalDate.now().plusDays(1)));
    }

    @Test
    void testSetInvalidEmail() {
        assertThrows(InvalidEmailException.class, () -> person.setEmail("invalid-email"));
    }

    @Test
    void testSetEmptyPhoneNumber() {
        assertThrows(NullOrEmptyStringException.class, () -> person.setPhone(""));
        assertThrows(NullOrEmptyStringException.class, () -> person.setPhone(" "));
    }
}
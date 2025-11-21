package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.Stadium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StadiumTest {

    private Stadium stadium;

    @BeforeEach
    void setUp() {
        stadium = new Stadium("Test Stadium", 10000, "Test Location");
    }

    @Test
    void testStadiumCreation() {
        assertEquals("Test Stadium", stadium.getName());
        assertEquals(10000, stadium.getCapacity());
        assertEquals("Test Location", stadium.getLocation());
    }

    @Test
    void testSetInvalidCapacity() {
        assertThrows(Exception.class, () -> stadium.setCapacity(999));
        assertThrows(Exception.class, () -> stadium.setCapacity(150001));
    }

    @Test
    void testSetEmptyLocation() {
        assertThrows(Exception.class, () -> stadium.setLocation(""));
        assertThrows(Exception.class, () -> stadium.setLocation(" "));
    }
}
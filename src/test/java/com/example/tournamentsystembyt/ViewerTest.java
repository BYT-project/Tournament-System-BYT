package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.Viewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ViewerTest {

    private Viewer viewer;

    @BeforeEach
    void setUp() {
        viewer = new Viewer("John", "Doe", LocalDate.of(1990, 1, 1), "john.doe@example.com", "123456789");
    }

    @Test
    void testViewerCreation() {
        assertEquals("John", viewer.getFirstName());
        assertEquals("Doe", viewer.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), viewer.getDateOfBirth());
        assertEquals("john.doe@example.com", viewer.getEmail());
        assertEquals("123456789", viewer.getPhone());
    }
}
package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.MediaPartner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MediaPartnerTest {

    private MediaPartner mediaPartner;

    @BeforeEach
    void setUp() {
        mediaPartner = new MediaPartner("Test Partner", "partner@example.com", "Test Type");
    }

    @Test
    void testMediaPartnerCreation() {
        assertEquals("Test Partner", mediaPartner.getName());
        assertEquals("partner@example.com", mediaPartner.getEmail());
        assertEquals("Test Type", mediaPartner.getType());
    }

    @Test
    void testSetEmptyName() {
        assertThrows(Exception.class, () -> mediaPartner.setName(""));
        assertThrows(Exception.class, () -> mediaPartner.setName(" "));
    }

    @Test
    void testSetInvalidEmail() {
        assertThrows(Exception.class, () -> mediaPartner.setEmail("invalid-email"));
    }

    @Test
    void testSetEmptyType() {
        assertThrows(Exception.class, () -> mediaPartner.setType(""));
        assertThrows(Exception.class, () -> mediaPartner.setType(" "));
    }
}
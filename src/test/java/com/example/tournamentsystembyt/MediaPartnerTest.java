package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.model.MediaPartner;
import com.example.tournamentsystembyt.model.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MediaPartnerTest {

    private MediaPartner mediaPartner;

    @BeforeEach
    void setUp() {
        mediaPartner = new MediaPartner("Test Partner", "partner@example.com", "Test Type");
    }

    private Tournament createTournament(String name) {
        Date start = new Date(System.currentTimeMillis() - 10L * 24 * 60 * 60 * 1000);
        Date end   = new Date(System.currentTimeMillis() - 5L * 24 * 60 * 60 * 1000);
        return new Tournament("World Cup", "Football", start, end, 10000.0);
    }

    private MediaPartner createPartner(String name) {
        return new MediaPartner(name, name.toLowerCase() + "@example.com", "TV");
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



    @Test
    void addMediaPartner_setsReverseConnection() {
        Tournament t = createTournament("World Cup");
        MediaPartner partner = createPartner("ESPN");

        t.addMediaPartner(partner);

        List<MediaPartner> partners = t.getMediaPartners();
        assertEquals(1, partners.size());
        assertTrue(partners.contains(partner));
        assertEquals(t, partner.getTournament());
    }

    @Test
    void addMediaPartner_rejectsNull() {
        Tournament t = createTournament("World Cup");
        assertThrows(InvalidValueException.class, () -> t.addMediaPartner(null));
    }

    @Test
    void addMediaPartner_rejectsDuplicateInSameTournament() {
        Tournament t = createTournament("World Cup");
        MediaPartner partner = createPartner("ESPN");

        t.addMediaPartner(partner);
        assertThrows(InvalidValueException.class, () -> t.addMediaPartner(partner));
        assertEquals(1, t.getMediaPartners().size());
    }

    @Test
    void addMediaPartner_rejectsPartnerAlreadyAssignedToDifferentTournament() {
        Tournament t1 = createTournament("WC1");
        Tournament t2 = createTournament("WC2");
        MediaPartner partner = createPartner("ESPN");

        t1.addMediaPartner(partner);

        assertEquals(t1, partner.getTournament());
        assertTrue(t1.getMediaPartners().contains(partner));
        assertTrue(t2.getMediaPartners().isEmpty());
    }

    @Test
    void removeMediaPartner_updatesReverseConnection() {
        Tournament t = createTournament("World Cup");
        MediaPartner p1 = createPartner("ESPN");
        MediaPartner p2 = createPartner("BBC");

        t.addMediaPartner(p1);
        t.addMediaPartner(p2);

        t.removeMediaPartner(p2);

        assertEquals(1, t.getMediaPartners().size());
        assertFalse(t.getMediaPartners().contains(p2));
        assertNull(p2.getTournament());
        assertEquals(t, p1.getTournament());
    }

    @Test
    void removeMediaPartner_rejectsNullOrNotAttached() {
        Tournament t = createTournament("World Cup");
        MediaPartner attached = createPartner("ESPN");
        MediaPartner notAttached = createPartner("Sky");

        t.addMediaPartner(attached);

        assertThrows(InvalidValueException.class, () -> t.removeMediaPartner(null));
        assertThrows(InvalidValueException.class, () -> t.removeMediaPartner(notAttached));
    }
    }

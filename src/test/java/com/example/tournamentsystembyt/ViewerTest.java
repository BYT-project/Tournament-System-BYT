package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.model.Viewer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import com.example.tournamentsystembyt.model.Ticket;


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

    @Test
    void addTicketToViewer_updatesBothSides() {
        Ticket ticket = new Ticket("T123", 100.0, "VIP", "AVAILABLE");

        viewer.addTicket(ticket);

        assertTrue(viewer.getTickets().contains(ticket));
        assertEquals(viewer, ticket.getViewer());
    }

    @Test
    void addTicketAlreadyOwnedByOtherViewer_throwsInvalidValueException() {
        Viewer other = new Viewer("Jane", "Smith",
                LocalDate.of(1991, 2, 2), "jane@example.com", "987654321");
        Ticket ticket = new Ticket("T123", 100.0, "VIP", "AVAILABLE");

        other.addTicket(ticket);

        assertThrows(InvalidValueException.class, () -> viewer.addTicket(ticket));
    }

}
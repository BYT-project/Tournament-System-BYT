package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.example.tournamentsystembyt.model.Viewer;
import com.example.tournamentsystembyt.model.TicketOrder;
import java.time.LocalDate;

class TicketTest {

    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticket = new Ticket("T123", 100.0, "VIP", "AVAILABLE");
    }

    @Test
    void testTicketCreation() {
        assertEquals("T123", ticket.getId());
        assertEquals(100.0, ticket.getPrice());
        assertEquals("VIP", ticket.getType());
        assertEquals("AVAILABLE", ticket.getStatus());
    }

    @Test
    void testSetEmptyId() {
        assertThrows(Exception.class, () -> ticket.setId(""));
        assertThrows(Exception.class, () -> ticket.setId(" "));
    }


    @Test
    void setTicketOrder_updatesBothSides() {
        TicketOrder order = new TicketOrder("O1");

        ticket.setTicketOrder(order);

        assertEquals(order, ticket.getTicketOrder());
        assertTrue(order.getTickets().contains(ticket));
    }

    @Test
    void testSetNegativePrice() {
        assertThrows(Exception.class, () -> ticket.setPrice(-1));
    }

    @Test
    void testSetEmptyType() {
        assertThrows(Exception.class, () -> ticket.setType(""));
        assertThrows(Exception.class, () -> ticket.setType(" "));
    }

    @Test
    void testSetInvalidStatus() {
        assertThrows(Exception.class, () -> ticket.setStatus("INVALID_STATUS"));
    }

    @Test
    void testCalculateTotalPrice() {
        assertEquals(105.0, ticket.calculateTotalPrice(), 0.001);
    }
}
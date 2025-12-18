package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.DigitalTicket;
import com.example.tournamentsystembyt.model.PhysicalTicket;
import com.example.tournamentsystembyt.model.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketCompositionTest {

    private Ticket createTicket() {
        return new Ticket("T1", 100.0, "MATCH", "AVAILABLE");
    }

    @AfterEach
    void cleanExtent() {
        Ticket.clearExtent();
    }

    // COMPOSITION

    @Test
    void ticketCanHaveDigitalTicket() {
        Ticket ticket = createTicket();
        DigitalTicket dt =
                new DigitalTicket(ticket, "https://download.com/t1", "QR12345");

        ticket.setDigitalTicket(dt);

        assertEquals(dt, ticket.getDigitalTicket());
        assertEquals(ticket, dt.getTicket());
    }

    @Test
    void ticketCanHavePhysicalTicket() {
        Ticket ticket = createTicket();
        PhysicalTicket pt =
                new PhysicalTicket(ticket, "BARCODE123");

        ticket.setPhysicalTicket(pt);

        assertEquals(pt, ticket.getPhysicalTicket());
        assertEquals(ticket, pt.getTicket());
    }

    @Test
    void ticketCanHaveBothDigitalAndPhysicalTickets() {
        Ticket ticket = createTicket();

        DigitalTicket dt =
                new DigitalTicket(ticket, "https://download.com/t1", "QR12345");
        PhysicalTicket pt =
                new PhysicalTicket(ticket, "BARCODE123");

        ticket.setDigitalTicket(dt);
        ticket.setPhysicalTicket(pt);

        assertNotNull(ticket.getDigitalTicket());
        assertNotNull(ticket.getPhysicalTicket());
    }

    // REVERSE CONNECTION REMOVAL

    @Test
    void removingDigitalTicketBreaksReverseConnection() {
        Ticket ticket = createTicket();
        DigitalTicket dt =
                new DigitalTicket(ticket, "https://download.com/t1", "QR12345");

        ticket.setDigitalTicket(dt);
        ticket.removeDigitalTicket();

        assertNull(ticket.getDigitalTicket());
        assertNull(dt.getTicket());
    }

    @Test
    void removingPhysicalTicketBreaksReverseConnection() {
        Ticket ticket = createTicket();
        PhysicalTicket pt =
                new PhysicalTicket(ticket, "BARCODE123");

        ticket.setPhysicalTicket(pt);
        ticket.removePhysicalTicket();

        assertNull(ticket.getPhysicalTicket());
        assertNull(pt.getTicket());
    }

    // COMPOSITION CONSTRAINTS

    @Test
    void digitalTicketCannotBeSharedBetweenTickets() {
        Ticket t1 = createTicket();
        Ticket t2 = new Ticket("T2", 120.0, "MATCH", "AVAILABLE");

        DigitalTicket dt =
                new DigitalTicket(t1, "https://download.com/t1", "QR12345");

        t1.setDigitalTicket(dt);

        assertThrows(IllegalStateException.class,
                () -> t2.setDigitalTicket(dt));
    }

    @Test
    void physicalTicketCannotBeSharedBetweenTickets() {
        Ticket t1 = createTicket();
        Ticket t2 = new Ticket("T2", 120.0, "MATCH", "AVAILABLE");

        PhysicalTicket pt =
                new PhysicalTicket(t1, "BARCODE123");

        t1.setPhysicalTicket(pt);

        assertThrows(IllegalStateException.class,
                () -> t2.setPhysicalTicket(pt));
    }

    // COMPOSITION DELETION

    @Test
    void deletingTicketRemovesDigitalTicket() {
        Ticket ticket = createTicket();
        DigitalTicket dt =
                new DigitalTicket(ticket, "https://download.com/t1", "QR12345");

        ticket.setDigitalTicket(dt);
        ticket.delete();

        assertNull(dt.getTicket());
        assertFalse(Ticket.getExtent().contains(ticket));
    }

    @Test
    void deletingTicketRemovesPhysicalTicket() {
        Ticket ticket = createTicket();
        PhysicalTicket pt =
                new PhysicalTicket(ticket, "BARCODE123");

        ticket.setPhysicalTicket(pt);
        ticket.delete();

        assertNull(pt.getTicket());
        assertFalse(Ticket.getExtent().contains(ticket));
    }

    @Test
    void deletingTicketWithBothPartsRemovesBoth() {
        Ticket ticket = createTicket();

        DigitalTicket dt =
                new DigitalTicket(ticket, "https://download.com/t1", "QR12345");
        PhysicalTicket pt =
                new PhysicalTicket(ticket, "BARCODE123");

        ticket.setDigitalTicket(dt);
        ticket.setPhysicalTicket(pt);
        ticket.delete();

        assertNull(dt.getTicket());
        assertNull(pt.getTicket());
        assertFalse(Ticket.getExtent().contains(ticket));
    }
}
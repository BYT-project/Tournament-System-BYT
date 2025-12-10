package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidStateException;
import com.example.tournamentsystembyt.model.Payment;
import com.example.tournamentsystembyt.model.Ticket;
import com.example.tournamentsystembyt.model.TicketOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TicketOrderTest {

    private TicketOrder ticketOrder;

    @BeforeEach
    void setUp() {
        TicketOrder.clearExtent();
        Payment.clearExtent();
        ticketOrder = new TicketOrder("O123");
    }

    // OLD

    @Test
    void testTicketOrderCreation() {
        assertEquals("O123", ticketOrder.getId());
        assertEquals("PENDING", ticketOrder.getStatus());
        assertEquals(0.0, ticketOrder.getAmount());
        assertTrue(ticketOrder.getTickets().isEmpty());
        assertNull(ticketOrder.getPayment());
    }
    @Test
    void addTicket_setsOrderOnTicketAndPreventsDuplicates() {
        Ticket ticket = new Ticket("T123", 100.0, "VIP", "AVAILABLE");

        ticketOrder.addTicket(ticket);

        assertEquals(ticketOrder, ticket.getTicketOrder());
        assertEquals(1, ticketOrder.getTickets().size());
        assertThrows(InvalidValueException.class, () -> ticketOrder.addTicket(ticket));
    }

    @Test
    void removeTicket_updatesBothSidesAndChecksMultiplicity() {
        Ticket t1 = new Ticket("T1", 100.0, "VIP", "AVAILABLE");
        Ticket t2 = new Ticket("T2", 50.0, "Regular", "AVAILABLE");

        ticketOrder.addTicket(t1);
        ticketOrder.addTicket(t2);

        ticketOrder.removeTicket(t2);

        assertNull(t2.getTicketOrder());
        assertEquals(1, ticketOrder.getTickets().size());

        assertThrows(InvalidValueException.class, () -> ticketOrder.removeTicket(t1));
    }


    @Test
    void testAddTicket() {
        Ticket ticket = new Ticket("T123", 100.0, "VIP", "AVAILABLE");
        ticketOrder.addTicket(ticket);
        assertEquals(1, ticketOrder.getTickets().size());
    }

    @Test
    void testCalculateTotal() {
        Ticket ticket1 = new Ticket("T123", 100.0, "VIP", "AVAILABLE");
        Ticket ticket2 = new Ticket("T124", 50.0, "Regular", "AVAILABLE");
        ticketOrder.addTicket(ticket1);
        ticketOrder.addTicket(ticket2);
        ticketOrder.calculateTotal();
        assertEquals(157.5, ticketOrder.getAmount(), 0.001);
    }

    @Test
    void testPay() {
        Ticket ticket = new Ticket("T123", 100.0, "VIP", "AVAILABLE");
        ticketOrder.addTicket(ticket);
        Payment payment = new Payment("P123", "Credit Card", 105.0);
        ticketOrder.pay(payment);
        assertEquals("PAID", ticketOrder.getStatus());
        assertNotNull(ticketOrder.getPayment());
        assertNotNull(payment.getPaidAt());
    }

    @Test
    void testPayWithInsufficientAmount() {
        Ticket ticket = new Ticket("T123", 100.0, "VIP", "AVAILABLE");
        ticketOrder.addTicket(ticket);
        Payment payment = new Payment("P123", "Credit Card", 100.0);
        assertThrows(Exception.class, () -> ticketOrder.pay(payment));
    }

    @Test
    void testCancel() {
        ticketOrder.cancel();
        assertEquals("CANCELLED", ticketOrder.getStatus());
    }

    @Test
    void testCannotCancelPaidOrder() {
        Ticket ticket = new Ticket("T123", 100.0, "VIP", "AVAILABLE");
        ticketOrder.addTicket(ticket);
        Payment payment = new Payment("P123", "Credit Card", 105.0);
        ticketOrder.pay(payment);
        assertThrows(Exception.class, () -> ticketOrder.cancel());
    }

    // NEW

    @Test
    void paymentAssignedToOrderReverseConnection() {
        Ticket ticket = new Ticket("T1", 100, "VIP", "AVAILABLE");
        ticketOrder.addTicket(ticket);

        Payment p = new Payment("P1", "Card", 110);

        ticketOrder.pay(p);

        assertEquals(ticketOrder, p.getTicketOrder());
        assertEquals(p, ticketOrder.getPayment());
    }

    @Test
    void paymentCannotBeAssignedToAnotherOrder() {
        TicketOrder order2 = new TicketOrder("O999");
        Ticket t = new Ticket("T1", 100, "VIP", "AVAILABLE");

        ticketOrder.addTicket(t);
        order2.addTicket(new Ticket("X1", 50, "VIP", "AVAILABLE"));

        Payment p = new Payment("P1", "Card", 150);

        ticketOrder.pay(p);

        assertThrows(Exception.class, () -> order2.pay(p));
    }

    @Test
    void deletingOrderDeletesPayment() {
        Ticket t = new Ticket("T1", 100, "VIP", "AVAILABLE");
        ticketOrder.addTicket(t);

        Payment p = new Payment("P1", "Card", 150);
        ticketOrder.pay(p);

        ticketOrder.delete();

        assertFalse(Payment.getExtent().contains(p));
    }
}
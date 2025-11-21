package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment("P123", "Credit Card", 100.0);
    }

    @Test
    void testPaymentCreation() {
        assertEquals("P123", payment.getId());
        assertEquals("Credit Card", payment.getMethod());
        assertEquals(100.0, payment.getAmount());
        assertNull(payment.getPaidAt());
    }

    @Test
    void testSetEmptyMethod() {
        assertThrows(Exception.class, () -> payment.setMethod(""));
        assertThrows(Exception.class, () -> payment.setMethod(" "));
    }

    @Test
    void testSetNegativeAmount() {
        assertThrows(Exception.class, () -> payment.setAmount(-1));
    }

    @Test
    void testPay() {
        payment.pay();
        assertNotNull(payment.getPaidAt());
    }

    @Test
    void testCannotPayTwice() {
        payment.pay();
        assertThrows(Exception.class, () -> payment.pay());
    }

    @Test
    void testRefund() {
        payment.pay();
        payment.refund();
        assertNull(payment.getPaidAt());
    }

    @Test
    void testCannotRefundUnpaid() {
        assertThrows(Exception.class, () -> payment.refund());
    }
}
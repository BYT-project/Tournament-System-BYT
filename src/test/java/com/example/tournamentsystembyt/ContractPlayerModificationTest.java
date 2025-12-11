package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidDateException;
import com.example.tournamentsystembyt.exceptions.InvalidStateException;
import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContractPlayerModificationTest {

    private Player player;
    private Team team;
    private ContractPlayer contract;

    @BeforeEach
    void setUp() {
        Player.clearExtent();
        Team.clearExtent();
        ContractPlayer.clearExtent();

        team = new Team("Team A", "Country", "City", 100);
        player = new Player("John", "Doe",
                LocalDate.of(1995, 1, 1),
                "john@example.com", "123456789",
                1.8, 80, "Midfielder", 8);

        contract = new ContractPlayer(
                player,
                team,
                LocalDate.now(),
                1000.0,
                "Initial contract"
        );
    }

    // SALARY CHANGE TESTS

    @Test
    void activeContractAllowsSalaryChange() {
        contract.changeSalary(1200.0);
        assertEquals(1200.0, contract.getSalary());
    }

    @Test
    void salaryCannotBeNegative() {
        assertThrows(NegativeNumberException.class, () -> contract.changeSalary(-50));
    }

    // TERMINATION TESTS

    @Test
    void terminationSetsEndDateAndDeactivatesContract() {
        LocalDate end = LocalDate.now();

        contract.terminate(end);

        assertFalse(contract.isActive());
        assertEquals(end, contract.getEndDate());
    }

    @Test
    void terminationWithoutDateUsesToday() {
        contract.terminate(null);

        assertFalse(contract.isActive());
        assertEquals(LocalDate.now(), contract.getEndDate());
    }

    @Test
    void cannotTerminateAlreadyInactiveContract() {
        contract.terminate(LocalDate.now());

        assertThrows(InvalidValueException.class,
                () -> contract.terminate(LocalDate.now()));
    }

    @Test
    void cannotTerminateWithEndDateBeforeStartDate() {
        assertThrows(InvalidDateException.class,
                () -> contract.terminate(contract.getStartDate().minusDays(1)));
    }

    // REACTIVATION TESTS

    @Test
    void inactiveContractCanBeReactivated() {
        contract.terminate(LocalDate.now());

        contract.reactivate();

        assertTrue(contract.isActive());
        assertNull(contract.getEndDate());
    }

    @Test
    void activeContractCannotBeReactivated() {
        assertThrows(InvalidStateException.class, () -> contract.reactivate());
    }

    @Test
    void inactiveContractCannotChangeSalary() {
        contract.terminate(LocalDate.now());

        assertThrows(InvalidStateException.class,
                () -> contract.changeSalary(2000));
    }
}
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

class ContractCoachModificationTest {

    private Coach coach;
    private Team team;
    private ContractCoach contract;

    @BeforeEach
    void setUp() {
        Coach.clearExtent();
        Team.clearExtent();
        ContractCoach.clearExtent();

        team = new Team("Team B", "Country", "City", 200);
        coach = new Coach(
                "Michael", "Smith",
                LocalDate.of(1980, 5, 10),
                "mike@example.com", "987654321",
                "Head Coach", 12
        );

        contract = new ContractCoach(
                coach,
                team,
                LocalDate.now(),
                1500.0,
                "Initial coaching contract"
        );
    }

    // SALARY CHANGE TESTS

    @Test
    void activeContractAllowsSalaryChange() {
        contract.changeSalary(1800.0);
        assertEquals(1800.0, contract.getSalary());
    }

    @Test
    void salaryCannotBeNegative() {
        assertThrows(NegativeNumberException.class, () -> contract.changeSalary(-50));
    }

    // TERMINATION TESTS

    @Test
    void terminatingContractSetsEndDateAndDeactivates() {
        LocalDate end = LocalDate.now();

        contract.terminate(end);

        assertFalse(contract.isActive());
        assertEquals(end, contract.getEndDate());
    }

    @Test
    void terminatingWithoutEndDateUsesToday() {
        contract.terminate(null);

        assertFalse(contract.isActive());
        assertEquals(LocalDate.now(), contract.getEndDate());
    }

    @Test
    void cannotTerminateInactiveContract() {
        contract.terminate(LocalDate.now());

        assertThrows(InvalidValueException.class,
                () -> contract.terminate(LocalDate.now()));
    }

    @Test
    void cannotTerminateWithInvalidEndDate() {
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
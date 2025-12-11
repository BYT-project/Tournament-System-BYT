package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TeamContractAssociationTest {

    private Team team;
    private Player player;
    private Coach coach;

    @BeforeEach
    void setUp() {
        Team.clearExtent();
        Player.clearExtent();
        Coach.clearExtent();
        ContractPlayer.clearExtent();
        ContractCoach.clearExtent();

        team = new Team("FC Test", "Country", "City", 500);

        player = new Player(
                "John", "Doe",
                LocalDate.of(1990, 1, 1),
                "john@example.com", "123456789",
                1.80, 75, "Striker", 9
        );

        coach = new Coach(
                "Mike", "Smith",
                LocalDate.of(1980, 5, 15),
                "mike@example.com", "987654321",
                "Head Coach", 10
        );
    }

    // PLAYER HIRE TESTS

    @Test
    void addingPlayerCreatesActiveContract() {
        team.addPlayer(player);

        assertEquals(1, team.getPlayerContracts().size());
        assertEquals(1, player.getPlayerContracts().size());
        assertEquals(1, ContractPlayer.getExtent().size());

        ContractPlayer contract = team.getPlayerContracts().get(0);

        assertEquals(player, contract.getPlayer());
        assertEquals(team, contract.getTeam());
        assertTrue(contract.isActive());
        assertEquals(1000.0, contract.getSalary());
        assertEquals("Auto-created contract", contract.getDescription());
        assertEquals(LocalDate.now(), contract.getStartDate());
        assertNull(contract.getEndDate());
    }

    @Test
    void addingSamePlayerTwiceThrowsException() {
        team.addPlayer(player);

        assertThrows(InvalidValueException.class, () -> team.addPlayer(player));
    }

    @Test
    void removingPlayerTerminatesActiveContract() {
        team.addPlayer(player);
        ContractPlayer contract = team.getPlayerContracts().get(0);

        team.removePlayer(player);

        assertFalse(contract.isActive());
        assertEquals(LocalDate.now(), contract.getEndDate());
        assertEquals(0, team.getPlayers().size());
        assertEquals(1, team.getPlayerContracts().size()); // history kept
    }

    @Test
    void removingNonExistingPlayerThrowsException() {
        assertThrows(InvalidValueException.class, () -> team.removePlayer(player));
    }

    // COACH HIRE TESTS

    @Test
    void addingCoachCreatesActiveContract() {
        team.addCoach(coach);

        assertEquals(1, team.getCoachContracts().size());
        assertEquals(1, coach.getCoachContracts().size());
        assertEquals(1, ContractCoach.getExtent().size());

        ContractCoach contract = team.getCoachContracts().get(0);

        assertEquals(coach, contract.getCoach());
        assertEquals(team, contract.getTeam());
        assertTrue(contract.isActive());
        assertEquals(1500.0, contract.getSalary());
        assertEquals("Auto-created contract", contract.getDescription());
        assertEquals(LocalDate.now(), contract.getStartDate());
        assertNull(contract.getEndDate());
    }

    @Test
    void addingSameCoachTwiceThrowsException() {
        team.addCoach(coach);

        assertThrows(InvalidValueException.class, () -> team.addCoach(coach));
    }

    @Test
    void removingCoachTerminatesActiveContract() {
        team.addCoach(coach);
        ContractCoach contract = team.getCoachContracts().get(0);

        team.removeCoach(coach);

        assertFalse(contract.isActive());
        assertEquals(LocalDate.now(), contract.getEndDate());
        assertEquals(0, team.getCoaches().size());
        assertEquals(1, team.getCoachContracts().size()); // history kept
    }

    @Test
    void removingNonExistingCoachThrowsException() {
        assertThrows(InvalidValueException.class, () -> team.removeCoach(coach));
    }

    // EDGE CASES

    @Test
    void addingNullPlayerThrowsException() {
        assertThrows(NullObjectException.class, () -> team.addPlayer(null));
    }

    @Test
    void addingNullCoachThrowsException() {
        assertThrows(NullObjectException.class, () -> team.addCoach(null));
    }

    @Test
    void contractHistoryIsPreservedAfterTermination() {
        team.addPlayer(player);
        ContractPlayer contract = team.getPlayerContracts().get(0);

        team.removePlayer(player);

        assertEquals(1, ContractPlayer.getExtent().size());  // still stored
        assertFalse(contract.isActive());
    }

    @Test
    void deletingContractRemovesItFromPlayerTeamAndExtent() {
        team.addPlayer(player);
        ContractPlayer contract = team.getPlayerContracts().get(0);

        contract.delete();

        assertFalse(ContractPlayer.getExtent().contains(contract));
        assertFalse(player.getPlayerContracts().contains(contract));
        assertFalse(team.getPlayerContracts().contains(contract));
    }

}
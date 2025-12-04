package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.model.Coach;
import com.example.tournamentsystembyt.model.Player;
import com.example.tournamentsystembyt.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TeamCoachPlayerAssociationTest {

    private Team teamA;
    private Team teamB;
    private Player player1;
    private Player player2;
    private Coach coach1;
    private Coach coach2;

    @BeforeEach
    void setUp() {
        // clear extents so tests are independent
        Team.clearExtent();
        Player.clearExtent();
        Coach.clearExtent();

        teamA = new Team("Team A", "Poland", "Warsaw", 100);
        teamB = new Team("Team B", "Poland", "Gdansk", 80);

        player1 = new Player(
                "John", "Doe",
                LocalDate.of(1995, 1, 1),
                "john@example.com", "123456789",
                1.80, 75.0, "Forward", 9
        );

        player2 = new Player(
                "Jane", "Smith",
                LocalDate.of(1998, 2, 2),
                "jane@example.com", "987654321",
                1.70, 60.0, "Midfielder", 10
        );

        coach1 = new Coach(
                "Mike", "Johnson",
                LocalDate.of(1970, 3, 3),
                "coach1@example.com", "111111111",
                "Head coach", 15
        );

        coach2 = new Coach(
                "Alex", "Brown",
                LocalDate.of(1980, 4, 4),
                "coach2@example.com", "222222222",
                "Assistant", 5
        );
    }

    // ===================== Team–Player association =====================

    @Test
    void addPlayerToTeam_shouldAddToTeamPlayersList() {
        teamA.addPlayer(player1);

        assertEquals(1, teamA.getPlayers().size());
        assertTrue(teamA.getPlayers().contains(player1));
    }

    @Test
    void addSamePlayerTwice_shouldThrowInvalidValueException() {
        teamA.addPlayer(player1);

        assertThrows(InvalidValueException.class, () -> teamA.addPlayer(player1));
    }

    @Test
    void removePlayerFromTeam_shouldRemoveFromList() {
        teamA.addPlayer(player1);
        teamA.addPlayer(player2);

        teamA.removePlayer(player1);

        assertEquals(1, teamA.getPlayers().size());
        assertFalse(teamA.getPlayers().contains(player1));
        assertTrue(teamA.getPlayers().contains(player2));
    }

    @Test
    void removePlayerNotInTeam_shouldThrowInvalidValueException() {
        // player1 was never added to teamA
        assertThrows(InvalidValueException.class, () -> teamA.removePlayer(player1));
    }

    @Test
    void addNullPlayer_shouldThrowNullObjectException() {
        assertThrows(NullObjectException.class, () -> teamA.addPlayer(null));
    }

    @Test
    void removeNullPlayer_shouldThrowNullObjectException() {
        assertThrows(NullObjectException.class, () -> teamA.removePlayer(null));
    }

    // ===================== Team–Coach & Coach–Team association =====================

    @Test
    void addCoachToTeam_shouldUpdateBothSides() {
        teamA.addCoach(coach1);

        // Team side
        assertEquals(1, teamA.getCoaches().size());
        assertTrue(teamA.getCoaches().contains(coach1));

        // Coach side (reverse)
        assertEquals(1, coach1.getTeamsCoached().size());
        assertTrue(coach1.getTeamsCoached().contains(teamA));
    }

    @Test
    void addSameCoachTwiceToTeam_shouldThrowInvalidValueException() {
        teamA.addCoach(coach1);

        assertThrows(InvalidValueException.class, () -> teamA.addCoach(coach1));
    }

    @Test
    void removeCoachFromTeam_shouldUpdateBothSides() {
        teamA.addCoach(coach1);
        teamA.addCoach(coach2);

        teamA.removeCoach(coach1);

        // teamA side
        assertFalse(teamA.getCoaches().contains(coach1));
        assertTrue(teamA.getCoaches().contains(coach2));

        // coach1 side
        assertFalse(coach1.getTeamsCoached().contains(teamA));
        // coach2 side still has teamA
        assertTrue(coach2.getTeamsCoached().contains(teamA));
    }

    @Test
    void removeCoachNotAssignedToTeam_shouldThrowInvalidValueException() {
        // coach1 is not assigned to teamA
        assertThrows(InvalidValueException.class, () -> teamA.removeCoach(coach1));
    }

    @Test
    void addNullCoach_shouldThrowNullObjectException() {
        assertThrows(NullObjectException.class, () -> teamA.addCoach(null));
    }

    @Test
    void removeNullCoach_shouldThrowNullObjectException() {
        assertThrows(NullObjectException.class, () -> teamA.removeCoach(null));
    }

    // -------- Tests from Coach side (modifying association via Coach API) --------

    @Test
    void addTeamFromCoachSide_shouldUpdateBothSides() {
        coach1.addTeam(teamA);

        assertTrue(coach1.getTeamsCoached().contains(teamA));
        assertTrue(teamA.getCoaches().contains(coach1));
    }

    @Test
    void addSameTeamTwiceFromCoachSide_shouldThrowInvalidValueException() {
        coach1.addTeam(teamA);

        assertThrows(InvalidValueException.class, () -> coach1.addTeam(teamA));
    }

    @Test
    void removeTeamFromCoachSide_shouldUpdateBothSides() {
        coach1.addTeam(teamA);
        coach1.addTeam(teamB);

        coach1.removeTeam(teamA);

        // coach1 side
        assertFalse(coach1.getTeamsCoached().contains(teamA));
        assertTrue(coach1.getTeamsCoached().contains(teamB));

        // teamA side
        assertFalse(teamA.getCoaches().contains(coach1));
        // teamB still has coach1
        assertTrue(teamB.getCoaches().contains(coach1));
    }

    @Test
    void removeTeamNotAssignedFromCoachSide_shouldThrowInvalidValueException() {
        // teamA not added to coach1 yet
        assertThrows(InvalidValueException.class, () -> coach1.removeTeam(teamA));
    }

    @Test
    void addNullTeamFromCoachSide_shouldThrowNullObjectException() {
        assertThrows(NullObjectException.class, () -> coach1.addTeam(null));
    }

    @Test
    void removeNullTeamFromCoachSide_shouldThrowNullObjectException() {
        assertThrows(NullObjectException.class, () -> coach1.removeTeam(null));
    }
}
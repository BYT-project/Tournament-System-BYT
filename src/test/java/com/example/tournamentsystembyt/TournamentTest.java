package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidDateException;
import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.model.GroupStage;
import com.example.tournamentsystembyt.model.PlayoffStage;
import com.example.tournamentsystembyt.model.Stadium;
import com.example.tournamentsystembyt.model.Team;
import com.example.tournamentsystembyt.model.Tournament;
import com.example.tournamentsystembyt.model.TournamentTicket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TournamentTest {

    private Date past(long days) {
        return new Date(System.currentTimeMillis() - days * 86_400_000L);
    }

    private Date future(long days) {
        return new Date(System.currentTimeMillis() + days * 86_400_000L);
    }

    private Tournament validTournament() {
        return new Tournament("World Cup", "Football", past(30), past(1), 1_000_000d);
    }

    @BeforeEach
    void clearExtents() {
        Tournament.clearExtent();
        GroupStage.clearExtent();
        PlayoffStage.clearExtent();
        Team.clearExtent();
        Stadium.clearExtent();
        TournamentTicket.clearExtent();
    }

    // OLD TESTS

    @Test
    void rejectsEmptyName() {
        assertThrows(InvalidValueException.class,
                () -> new Tournament("  ", "Football", past(10), past(1), 1000d));
    }

    @Test
    void rejectsEmptySportType() {
        assertThrows(InvalidValueException.class,
                () -> new Tournament("WC", "  ", past(10), past(1), 1000d));
    }

    @Test
    void startDate_cannotBeFuture() {
        assertThrows(InvalidDateException.class,
                () -> new Tournament("WC", "Football", future(1), future(2), 1000d));
    }

    @Test
    void endDate_cannotBeBeforeStartDate() {
        Date start = past(1);
        Date end = past(30);

        assertThrows(InvalidDateException.class,
                () -> new Tournament("WC", "Football", start, end, 1000d));
    }

    @Test
    void prizePool_cannotBeNegative() {
        assertThrows(NegativeNumberException.class,
                () -> new Tournament("WC", "Football", past(10), past(1), -1));
    }

    @Test
    void registrationFee_cannotBeNegative() {
        Tournament t = validTournament();

        assertThrows(NegativeNumberException.class,
                () -> t.setRegistrationFee(-5d));
    }

    @Test
    void setChampion_rejectsEmpty() {
        Tournament t = validTournament();

        assertThrows(InvalidValueException.class,
                () -> t.setChampion(" "));
    }

    // NEW TESTS

    @Test
    void addingStageViaConstructorCreatesReverseConnection() {
        Tournament t = validTournament();

        GroupStage gs = new GroupStage(1, "Groups", 4, 4, t);

        assertTrue(t.getStages().contains(gs));
        assertEquals(t, gs.getTournament());
    }

    @Test
    void addStageViaMethodMovesStageBetweenTournaments() {
        Tournament t1 = validTournament();
        GroupStage gs = new GroupStage(1, "Group A", 2, 4, t1);

        Tournament t2 = new Tournament("Euro", "Football", past(20), past(5), 50_000);

        // when we add an already-owned stage to another tournament,
        // it is *moved* (composition: cannot be shared, but can be re-parented)
        t2.addStage(gs);

        assertEquals(t2, gs.getTournament());
        assertTrue(t2.getStages().contains(gs));
        assertFalse(t1.getStages().contains(gs));
    }

    @Test
    void createGroupStageAutomaticallyAddsToTournament() {
        Tournament t = validTournament();

        GroupStage stage = t.createGroupStage("Group B", 2, 2);

        assertTrue(t.getStages().contains(stage));
        assertEquals(t, stage.getTournament());
    }

    @Test
    void cannotAddSameStageTwiceToSameTournament() {
        Tournament t = validTournament();
        GroupStage gs = new GroupStage(1, "Group C", 3, 3, t);

        assertThrows(InvalidValueException.class, () -> t.addStage(gs));
    }

    @Test
    void setTournamentRejectsNull() {
        Tournament t = validTournament();
        GroupStage gs = new GroupStage(1, "Group Z", 1, 4, t);

        assertThrows(NullObjectException.class, () -> gs.setTournament(null));
    }

    @Test
    void stageCanMoveBetweenTournamentsAndReverseConnectionsUpdate() {
        Tournament t1 = validTournament();
        Tournament t2 = new Tournament("Euro", "Football", past(40), past(5), 80_000);

        GroupStage gs = new GroupStage(1, "Group D", 2, 4, t1);

        gs.setTournament(t2);

        assertEquals(t2, gs.getTournament());
        assertTrue(t2.getStages().contains(gs));
        assertFalse(t1.getStages().contains(gs));
    }

    @Test
    void tournamentDeleteRemovesStagesFromExtentAndBreaksLinks() {
        Tournament t = validTournament();
        GroupStage gs1 = t.createGroupStage("Group A", 2, 2);
        PlayoffStage ps1 = t.createPlayoffStage("Quarterfinal", 1, "Single");

        assertTrue(GroupStage.getExtent().contains(gs1));
        assertTrue(PlayoffStage.getExtent().contains(ps1));

        t.delete();

        assertFalse(GroupStage.getExtent().contains(gs1));
        assertFalse(PlayoffStage.getExtent().contains(ps1));
        assertFalse(Tournament.getExtent().contains(t));
    }

    @Test
    void removingStageViaDeleteRemovesFromTournament() {
        Tournament t = validTournament();
        GroupStage g = t.createGroupStage("Group", 1, 4);

        assertTrue(t.getStages().contains(g));

        g.delete();

        assertFalse(t.getStages().contains(g));
        assertFalse(GroupStage.getExtent().contains(g));
    }

    @Test
    void tournamentCreatesTicketsViaReverseConnection() {
        Tournament t = validTournament();
        Stadium stadium = new Stadium("Wembley", 90_000, "London");

        TournamentTicket tt = new TournamentTicket("T1", 50, "AVAILABLE", t, stadium, 10);

        assertTrue(t.getTournamentTickets().contains(tt));
        assertEquals(t, tt.getTournament());
    }

    @Test
    void deletingTournamentDeletesTickets() {
        Tournament t = validTournament();
        Stadium s = new Stadium("Arena", 2_000, "City");

        TournamentTicket tt1 = new TournamentTicket("AA", 100, "SOLD", t, s, 5);
        TournamentTicket tt2 = new TournamentTicket("BB", 90, "SOLD", t, s, 10);

        assertTrue(t.getTournamentTickets().contains(tt1));
        assertTrue(t.getTournamentTickets().contains(tt2));

        t.delete();

        assertFalse(t.getTournamentTickets().contains(tt1));
        assertFalse(t.getTournamentTickets().contains(tt2));
    }

    @Test
    void cannotAddNullStage() {
        Tournament t = validTournament();
        assertThrows(NullObjectException.class, () -> t.addStage(null));
    }

    @Test
    void groupStageConstructorAddsToExtent() {
        Tournament t = validTournament();
        GroupStage gs = new GroupStage(10, "Groups", 2, 2, t);
        assertTrue(GroupStage.getExtent().contains(gs));
    }

    @Test
    void createTournamentThrowsIfNoTeams() {
        Tournament t = validTournament();
        t.createGroupStage("Group", 2, 2);

        assertThrows(InvalidValueException.class, t::createTournament);
    }

    @Test
    void createTournamentThrowsIfNoStages() {
        Tournament t = validTournament();
        Team team = new Team("Test United", "England", "London", 0);

        t.addTeam(team);

        assertThrows(InvalidValueException.class, t::createTournament);
    }
}
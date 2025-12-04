package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.model.GroupStage;
import com.example.tournamentsystembyt.model.PlayoffStage;
import com.example.tournamentsystembyt.model.Stage;
import com.example.tournamentsystembyt.model.Tournament;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class StageTest {

    private Tournament tournament() {
        return new Tournament("WC", "Football",
                new Date(System.currentTimeMillis() - 10000000),
                new Date(System.currentTimeMillis() - 1000),
                1000);
    }

    // OLD TESTS

    @Test
    void ctor_rejectsNonPositiveId() {
        Tournament t = tournament();

        assertThrows(InvalidValueException.class,
                () -> new GroupStage(0, "Stage", 4, 4, t));

        assertThrows(InvalidValueException.class,
                () -> new GroupStage(-1, "Stage", 4, 4, t));
    }

    @Test
    void ctor_rejectsEmptyName() {
        Tournament t = tournament();

        assertThrows(InvalidValueException.class,
                () -> new GroupStage(1, "  ", 4, 4, t));
    }

    // NEW TESTS

    @Test
    void stageIsAddedToTournament_WhenCreated() {
        Tournament t = tournament();
        Stage s = new GroupStage(1, "Groups", 4, 4, t);

        assertTrue(t.getStages().contains(s));
        assertEquals(t, s.getTournament());
    }

    @Test
    void stageMovesToAnotherTournament() {
        Tournament t1 = tournament();
        Tournament t2 = new Tournament("EC", "Football",
                new Date(System.currentTimeMillis() - 20000000),
                new Date(System.currentTimeMillis() - 1000),
                500);

        Stage s = new GroupStage(1, "Groups", 4, 4, t1);

        s.setTournament(t2);

        assertFalse(t1.getStages().contains(s));
        assertTrue(t2.getStages().contains(s));
    }

    @Test
    void deletingStageRemovesItFromTournament() {
        Tournament t = tournament();
        Stage s = new GroupStage(1, "Groups", 4, 4, t);

        s.delete();

        assertFalse(t.getStages().contains(s));
        assertNull(s.getTournament());
    }

    @Test
    void creatingPlayoffStageAlsoAddsToTournament() {
        Tournament t = tournament();
        PlayoffStage ps = new PlayoffStage(2, "Playoffs", 3, "BO3", t);

        assertTrue(t.getStages().contains(ps));
        assertEquals(t, ps.getTournament());
    }
}
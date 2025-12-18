package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.model.GroupStage;
import com.example.tournamentsystembyt.model.PlayoffStage;
import com.example.tournamentsystembyt.model.Stage;
import com.example.tournamentsystembyt.model.Tournament;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class StageInheritanceViaCompositionTest {

    private Tournament tournament() {
        return new Tournament("WC", "Football",
                new Date(System.currentTimeMillis() - 10000000),
                new Date(System.currentTimeMillis() - 1000),
                1000);
    }

    @Test
    void stageCanBeGroupStage() {
        GroupStage gs = new GroupStage(1, "Groups", 4, 4, tournament());
        Stage s = gs.getStage();

        assertTrue(s.isGroupStage());
        assertFalse(s.isPlayoffStage());
    }

    @Test
    void stageCanBePlayoffStage() {
        PlayoffStage ps = new PlayoffStage(2, "Playoffs", 3, "BO3", tournament());
        Stage s = ps.getStage();

        assertTrue(s.isPlayoffStage());
        assertFalse(s.isGroupStage());
    }

    @Test
    void stageCannotHaveBothSpecializations() {
        Tournament t = tournament();
        GroupStage gs = new GroupStage(1, "Groups", 4, 4, t);
        Stage s = gs.getStage();

        assertThrows(InvalidValueException.class,
                () -> s.attachPlayoffStage(
                        new PlayoffStage(2, "Playoffs", 3, "BO3", t)
                ));
    }

    @Test
    void invalidSpecializationAccessThrows() {
        GroupStage gs = new GroupStage(1, "Groups", 4, 4, tournament());
        Stage s = gs.getStage();

        assertThrows(InvalidValueException.class, s::getPlayoffStage);
    }
}
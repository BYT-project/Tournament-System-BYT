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

    // OLD TESTS – unchanged

    @Test
    void ctor_rejectsNonPositiveId() {
        Tournament t = tournament();

        assertThrows(InvalidValueException.class,
                () -> new GroupStage(0, "Stage", 4, 4, t));
    }

    @Test
    void ctor_rejectsEmptyName() {
        Tournament t = tournament();

        assertThrows(InvalidValueException.class,
                () -> new GroupStage(1, "  ", 4, 4, t));
    }

    // NEW – composition deletion

    @Test
    void deletingStageRemovesCompositionPart() {
        Tournament t = tournament();
        GroupStage gs = new GroupStage(1, "Groups", 4, 4, t);
        Stage s = gs.getStage();

        s.delete();

        assertThrows(Exception.class, s::getGroupStage);
        assertFalse(GroupStage.getExtent().contains(gs));
    }
}
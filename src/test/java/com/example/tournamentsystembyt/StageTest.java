package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.model.GroupStage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StageTest {

    @Test
    void ctor_rejectsNonPositiveId() {
        assertThrows(InvalidValueException.class,
                () -> new GroupStage(0, "Stage", 4, 4));

        assertThrows(InvalidValueException.class,
                () -> new GroupStage(-1, "Stage", 4, 4));
    }

    @Test
    void ctor_rejectsEmptyName() {
        assertThrows(InvalidValueException.class,
                () -> new GroupStage(1, "  ", 4, 4));
    }
}
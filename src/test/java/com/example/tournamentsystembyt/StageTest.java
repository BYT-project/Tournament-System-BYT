package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.model.Stage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StageTest {

    @Test
    void ctor_rejectsNonPositiveId() {
        assertThrows(InvalidValueException.class, () -> new Stage(0, "Stage"));
        assertThrows(InvalidValueException.class, () -> new Stage(-1, "Stage"));
    }

    @Test
    void ctor_rejectsEmptyName() {
        assertThrows(InvalidValueException.class, () -> new Stage(1, "  "));
    }

}

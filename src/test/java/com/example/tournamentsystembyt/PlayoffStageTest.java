package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.PlayoffStage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayoffStageTest {

    private PlayoffStage playoffStage;

    @BeforeEach
    void setUp() {
        playoffStage = new PlayoffStage(1, "Playoffs", 3, "Best of 3");
    }

    @Test
    void testPlayoffStageCreation() {
        assertEquals(1, playoffStage.getId());
        assertEquals("Playoffs", playoffStage.getName());
        assertEquals(3, playoffStage.getNumberOfRounds());
        assertEquals("Best of 3", playoffStage.getMatchType());
    }

    @Test
    void testSetInvalidNumberOfRounds() {
        assertThrows(Exception.class, () -> playoffStage.setNumberOfRounds(0));
        assertThrows(Exception.class, () -> playoffStage.setNumberOfRounds(11));
    }

    @Test
    void testSetEmptyMatchType() {
        assertThrows(Exception.class, () -> playoffStage.setMatchType(""));
        assertThrows(Exception.class, () -> playoffStage.setMatchType(" "));
    }
}
package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.PlayoffStage;
import com.example.tournamentsystembyt.model.Stage;
import com.example.tournamentsystembyt.model.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PlayoffStageTest {

    private PlayoffStage playoffStage;

    private Tournament tournament() {
        return new Tournament("WC", "Football",
                new Date(System.currentTimeMillis() - 10000000),
                new Date(System.currentTimeMillis() - 1000),
                1000);
    }

    @BeforeEach
    void setUp() {
        PlayoffStage.clearExtent();
        playoffStage = new PlayoffStage(1, "Playoffs", 3, "Best of 3", tournament());
    }

    // OLD

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

    // NEW – extent

    @Test
    void playoffAddedToExtent() {
        assertTrue(PlayoffStage.getExtent().contains(playoffStage));
    }

    @Test
    void deletingPlayoffRemovesFromExtent() {
        playoffStage.delete();
        assertFalse(PlayoffStage.getExtent().contains(playoffStage));
    }

    // NEW – reverse connection

    @Test
    void playoffStageHasBackReferenceToStage() {
        Stage stage = playoffStage.getStage();

        assertNotNull(stage);
        assertEquals(playoffStage, stage.getPlayoffStage());
    }
}
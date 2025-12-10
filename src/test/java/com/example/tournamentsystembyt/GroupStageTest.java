package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.GroupStage;
import com.example.tournamentsystembyt.model.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class GroupStageTest {

    private GroupStage groupStage;

    private Tournament tournament() {
        return new Tournament("WC", "Football",
                new Date(System.currentTimeMillis() - 10000000),
                new Date(System.currentTimeMillis() - 1000),
                1000);
    }

    @BeforeEach
    void setUp() {
        GroupStage.clearExtent();
        groupStage = new GroupStage(1, "Group Stage", 4, 4, tournament());
    }

    // OLD

    @Test
    void testGroupStageCreation() {
        assertEquals(1, groupStage.getId());
        assertEquals("Group Stage", groupStage.getName());
        assertEquals(4, groupStage.getNumberOfGroups());
        assertEquals(4, groupStage.getTeamsPerGroup());
    }

    @Test
    void testSetInvalidNumberOfGroups() {
        assertThrows(Exception.class, () -> groupStage.setNumberOfGroups(0));
        assertThrows(Exception.class, () -> groupStage.setNumberOfGroups(65));
    }

    @Test
    void testSetInvalidTeamsPerGroup() {
        assertThrows(Exception.class, () -> groupStage.setTeamsPerGroup(0));
        assertThrows(Exception.class, () -> groupStage.setTeamsPerGroup(21));
    }

    // NEW

    @Test
    void groupStageAddedToExtent() {
        assertTrue(GroupStage.getExtent().contains(groupStage));
    }

    @Test
    void deletingGroupStageRemovesFromExtent() {
        groupStage.delete();
        assertFalse(GroupStage.getExtent().contains(groupStage));
    }
}
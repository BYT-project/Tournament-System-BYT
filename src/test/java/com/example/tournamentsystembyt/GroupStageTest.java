package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.model.GroupStage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GroupStageTest {

    private GroupStage groupStage;

    @BeforeEach
    void setUp() {
        groupStage = new GroupStage(1, "Group Stage", 4, 4);
    }

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
}
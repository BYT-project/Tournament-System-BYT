package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.helpers.ExtentPersistence;
import com.example.tournamentsystembyt.model.Team;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class ExtentPresisTest {

    @Before
    public void setUp() throws Exception {
        Team.clearExtent();
        Files.deleteIfExists(Path.of("Team_extent.json"));
    }

    @Test
    public void testConstructorAddsToExtent() {
        assertEquals(0, Team.getExtent().size());

        Team t = new Team("Arsenal", "UK", "London", 100);

        assertEquals(1, Team.getExtent().size());
        assertTrue(Team.getExtent().contains(t));
    }

    @Test
    public void testExtentEncapsulation() {
        Team t = new Team("Arsenal", "UK", "London", 100);

        var copy = Team.getExtent();
        copy.clear(); // modifying returned list

        // Internal list MUST remain unchanged
        assertEquals(1, Team.getExtent().size());
        assertTrue(Team.getExtent().contains(t));
    }

    @Test
    public void testSaveExtentCreatesFile() throws Exception {
        new Team("Arsenal", "UK", "London", 100);

        boolean saved = Team.saveExtent();
        assertTrue(saved);

        Path file = Path.of("Team_extent.json");

        assertTrue(Files.exists(file));
        assertTrue(Files.size(file) > 0);
    }

    @Test
    public void testLoadExtentRestoresObjects() throws Exception {
        new Team("Arsenal", "UK", "London", 100);
        Team.saveExtent();

        // simulate clean app start
        Team.clearExtent();
        assertEquals(0, Team.getExtent().size());

        Team.loadExtent();

        assertEquals(1, Team.getExtent().size());
        Team loaded = Team.getExtent().get(0);

        assertEquals("Arsenal", loaded.getName());
        assertEquals("UK", loaded.getCountry());
        assertEquals("London", loaded.getCity());
    }

    @Test
    public void testRoundTripSaveLoad() throws Exception {
        new Team("Barcelona", "Spain", "Barcelona", 200);
        new Team("Chelsea", "UK", "London", 180);

        Team.saveExtent();

        Team.clearExtent();
        assertEquals(0, Team.getExtent().size());

        Team.loadExtent();

        assertEquals(2, Team.getExtent().size());
    }

    @Test
    public void testLoadMissingFileReturnsEmpty() throws Exception {
        Files.deleteIfExists(Path.of("Team_extent.json"));
        Team.clearExtent();

        Team.loadExtent();

        assertTrue(Team.getExtent().isEmpty());
    }
}
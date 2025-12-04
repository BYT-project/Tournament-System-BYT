package com.example.tournamentsystembyt;

import com.example.tournamentsystembyt.helpers.ExtentPersistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ExtentPersistenceTest {

    public static class TestClass {
        private String name;
        private int value;

        public TestClass() {}

        public TestClass(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() { return name; }
        public int getValue() { return value; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestClass testClass = (TestClass) o;
            return value == testClass.value && Objects.equals(name, testClass.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, value);
        }
    }

    private final String testFileName = "TestClass_extent.json";

    @BeforeEach
    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(testFileName));
    }

    @Test
    void testSaveAndLoadExtent() {
        // 1. Prepare data
        List<TestClass> extentToSave = new ArrayList<>();
        extentToSave.add(new TestClass("Object1", 100));
        extentToSave.add(new TestClass("Object2", 200));

        // 2. Save the extent
        boolean saved = ExtentPersistence.saveExtent(TestClass.class, extentToSave);
        assertTrue(saved, "Save operation should be successful.");
        assertTrue(Files.exists(Path.of(testFileName)), "Extent file should be created.");

        // 3. Load the extent
        List<TestClass> loadedExtent = ExtentPersistence.loadExtent(TestClass.class);
        assertNotNull(loadedExtent, "Loaded extent should not be null.");
        assertEquals(2, loadedExtent.size(), "Loaded extent should have the same size as the saved one.");

        // 4. Verify content
        assertEquals(extentToSave, loadedExtent, "Loaded extent should be equal to the original one.");
    }

    @Test
    void testLoadNonExistentExtent() {
        List<TestClass> loadedExtent = ExtentPersistence.loadExtent(TestClass.class);
        assertNotNull(loadedExtent);
        assertTrue(loadedExtent.isEmpty());
    }
}
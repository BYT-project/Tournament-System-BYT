package com.example.tournamentsystembyt.model;

public class Stadium {
    private final String name;
    private int capacity;
    private String location;
    public Stadium(String name, int capacity, String location) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Stadium name cannot be empty");
        }
        validateCapacity(capacity);
        this.name = name;
        this.capacity = capacity;
        this.location = location;
    }
    public String getName() {
        return name;
    }
    public int getCapacity() {
        return capacity;
    }
    public String getLocation() {
        return location;
    }
    private void validateCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
    }
    public void setCapacity(int capacity) {
        validateCapacity(capacity);
        this.capacity = capacity;
    }
    public void setLocation(String location) {
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Location cannot be empty");
        }
        this.location = location;
    }
}

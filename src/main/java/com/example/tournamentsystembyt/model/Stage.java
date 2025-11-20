package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Stage {

    private int id;
    private String name;
    private boolean isCompleted;

    private final List<Match> matches;

    public Stage(int id, String name) {
        setId(id);
        setName(name);
        this.isCompleted = false;
        this.matches = new ArrayList<>();
    }

    public void seedTeams() {
        // Will be implemented later
    }

    public void createMatch(Match match) {
        if (match == null) {
            throw new NullObjectException("Match");
        }
        if (matches.contains(match)) {
            throw new InvalidValueException("This match already exists in the stage.");
        }
        matches.add(match);
    }

    public void advanceTeams() {
        // Will be implemented later
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new InvalidValueException("Stage ID must be positive.");
        }
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Stage name");
        }
        String trimmed = name.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException("Stage name must contain at least 2 characters.");
        }
        this.name = trimmed;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches);
    }
}
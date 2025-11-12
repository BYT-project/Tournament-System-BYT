package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;

import java.util.ArrayList;
import java.util.List;

public class Stage {
    private int id;
    private String name;
    private boolean isCompleted;

    private List<Match> matches;

    public Stage(int id, String name) {
        setId(id);
        setName(name);
        this.isCompleted = false;
        this.matches = new ArrayList<>();
    }

    public void seedTeams() {
    }

    public void createMatch(Match match) {

    }

    public void advanceTeams() {

    }

    public void setId(int id) {
        if (id <= 0)
            throw new InvalidValueException("Stage ID must be positive.");
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new InvalidValueException("Stage name cannot be empty.");
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public boolean isCompleted() { return isCompleted; }
    public List<Match> getMatches() { return matches; }
}

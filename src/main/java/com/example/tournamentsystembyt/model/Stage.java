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
    // NEW – composition "whole"
    private Tournament tournament;

    private final List<Match> matches;

    public Stage(int id, String name, Tournament tournament) {
        setId(id);
        setName(name);
        this.isCompleted = false;
        this.matches = new ArrayList<>();
        setTournament(tournament);
    }
    public Stage() {
        this.matches = new ArrayList<>();
    }

    // NEW – internal helpers for reverse match connection
    void internalAddMatch(Match match) {
        if (!matches.contains(match)) {
            matches.add(match);
        }
    }

    void internalRemoveMatch(Match match) {
        matches.remove(match);
    }

    // NEW
    public void setTournament(Tournament tournament) {
        if (tournament == null) {
            throw new NullObjectException("Tournament");
        }
        if (this.tournament == tournament) {
            return;
        }
        // remove from old tournament
        if (this.tournament != null) {
            this.tournament.internalRemoveStage(this);
        }
        this.tournament = tournament;
        tournament.internalAddStage(this);
    }

    public Tournament getTournament() {
        return tournament;
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
        // Let Match.setStage handle reverse connection & collection
        match.setStage(this);
    }

    // NEW – to remove a match respecting composition
    public void removeMatch(Match match) {
        if (match == null) {
            throw new NullObjectException("Match");
        }
        if (!matches.contains(match)) {
            throw new InvalidValueException("Match is not part of this stage.");
        }
        match.delete(); // composition: deleting the part
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

    // NEW – used from Tournament.delete()
    public void delete() {
        for (Match m : new ArrayList<>(matches)) {
            m.delete();
        }
        if (tournament != null) {
            Tournament old = tournament;
            tournament = null;
            old.internalRemoveStage(this);
        }
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
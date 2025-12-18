package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stage {

    private int id;
    private String name;
    private boolean isCompleted;
    private Tournament tournament;

    private final List<Match> matches;

    // NEW – composition components (XOR)
    private GroupStage groupStageComponent;
    private PlayoffStage playoffStageComponent;

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

    public void setTournament(Tournament tournament) {
        if (tournament == null) {
            throw new NullObjectException("Tournament");
        }
        if (this.tournament == tournament) {
            return;
        }
        if (this.tournament != null) {
            this.tournament.internalRemoveStage(this);
        }
        this.tournament = tournament;
        tournament.internalAddStage(this);
    }

    public Tournament getTournament() {
        return tournament;
    }

    // NEW – attach GroupStage (XOR + reverse handled in constructor)
    public void attachGroupStage(GroupStage groupStage) {
        if (groupStage == null) {
            throw new NullObjectException("GroupStage");
        }
        if (playoffStageComponent != null) {
            throw new InvalidValueException("Stage already has a PlayoffStage.");
        }
        if (this.groupStageComponent != null) {
            throw new InvalidValueException("GroupStage already attached.");
        }
        this.groupStageComponent = groupStage;
    }

    // NEW – attach PlayoffStage (XOR + reverse handled in constructor)
    public void attachPlayoffStage(PlayoffStage playoffStage) {
        if (playoffStage == null) {
            throw new NullObjectException("PlayoffStage");
        }
        if (groupStageComponent != null) {
            throw new InvalidValueException("Stage already has a GroupStage.");
        }
        if (this.playoffStageComponent != null) {
            throw new InvalidValueException("PlayoffStage already attached.");
        }
        this.playoffStageComponent = playoffStage;
    }

    public boolean isGroupStage() {
        return groupStageComponent != null;
    }

    public boolean isPlayoffStage() {
        return playoffStageComponent != null;
    }

    public GroupStage getGroupStage() {
        if (groupStageComponent == null) {
            throw new InvalidValueException("Stage is not a GroupStage.");
        }
        return groupStageComponent;
    }

    public PlayoffStage getPlayoffStage() {
        if (playoffStageComponent == null) {
            throw new InvalidValueException("Stage is not a PlayoffStage.");
        }
        return playoffStageComponent;
    }

    public void createMatch(Match match) {
        if (match == null) {
            throw new NullObjectException("Match");
        }
        if (matches.contains(match)) {
            throw new InvalidValueException("This match already exists in the stage.");
        }
        match.setStage(this);
    }

    public void removeMatch(Match match) {
        if (match == null) {
            throw new NullObjectException("Match");
        }
        if (!matches.contains(match)) {
            throw new InvalidValueException("Match is not part of this stage.");
        }
        match.delete();
    }

    public void delete() {
        for (Match m : new ArrayList<>(matches)) {
            m.delete();
        }
        if (tournament != null) {
            Tournament old = tournament;
            tournament = null;
            old.internalRemoveStage(this);
        }
        // NEW – composition cleanup
        groupStageComponent = null;
        playoffStageComponent = null;
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

    public int getId() { return id; }
    public String getName() { return name; }
    public boolean isCompleted() { return isCompleted; }

    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches);
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

}
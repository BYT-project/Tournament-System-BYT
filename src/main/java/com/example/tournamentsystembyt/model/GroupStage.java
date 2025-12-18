package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.util.ArrayList;
import java.util.List;

public class GroupStage extends Stage {

    private int numberOfGroups;
    private int teamsPerGroup;

    // NEW – reverse composition reference
    private final Stage stage;

    private static final List<GroupStage> extent = new ArrayList<>();

    private static void addStage(GroupStage gs) {
        if (gs == null) {
            throw new IllegalArgumentException("GroupStage cannot be null");
        }
        extent.add(gs);
    }

    public static List<Stage> getExtent() {
        return new ArrayList<>(extent);
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(GroupStage.class, extent);
    }

    public static void loadExtent() {
        List<GroupStage> loaded = ExtentPersistence.loadExtent(GroupStage.class);
        extent.clear();
        extent.addAll(loaded);
    }

    public GroupStage(int id,
                      String stageName,
                      int numberOfGroups,
                      int teamsPerGroup,
                      Tournament tournament) {

        super(id, stageName, tournament);
        this.stage = this;

        setNumberOfGroups(numberOfGroups);
        setTeamsPerGroup(teamsPerGroup);
        addStage(this);

        // NEW – composition registration
        super.attachGroupStage(this);
    }

    public Stage getStage() {
        return stage;
    }

    public void delete() {
        super.delete();
        extent.remove(this);
    }

    public void setNumberOfGroups(int numberOfGroups) {
        if (numberOfGroups <= 0) {
            throw new NegativeNumberException("Number of groups", numberOfGroups);
        }
        if (numberOfGroups > 64) {
            throw new InvalidValueException("Number of groups is unrealistically high.");
        }
        this.numberOfGroups = numberOfGroups;
    }

    public void setTeamsPerGroup(int teamsPerGroup) {
        if (teamsPerGroup <= 0) {
            throw new NegativeNumberException("Teams per group", teamsPerGroup);
        }
        if (teamsPerGroup > 20) {
            throw new InvalidValueException("Teams per group value is unrealistically high.");
        }
        this.teamsPerGroup = teamsPerGroup;
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }

    public int getTeamsPerGroup() {
        return teamsPerGroup;
    }
}
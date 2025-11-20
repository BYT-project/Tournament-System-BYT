package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;

public class GroupStage extends Stage {

    private int numberOfGroups;
    private int teamsPerGroup;

    public GroupStage(int id,
                      String stageName,
                      int numberOfGroups,
                      int teamsPerGroup) {

        super(id, stageName);
        setNumberOfGroups(numberOfGroups);
        setTeamsPerGroup(teamsPerGroup);
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
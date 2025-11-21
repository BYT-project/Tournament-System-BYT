package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.util.ArrayList;
import java.util.List;

public class PlayoffStage extends Stage {

    private int numberOfRounds;
    private String matchType;

    private static final List<PlayoffStage> extent = new ArrayList<>();

    private static void addPlayoffStage(PlayoffStage ps) {
        if (ps == null) {
            throw new IllegalArgumentException("PlayoffStage cannot be null");
        }
        extent.add(ps);
    }

    public static List<PlayoffStage> getExtent() {
        return new ArrayList<>(extent); // defensive copy
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(PlayoffStage.class, extent);
    }

    public static void loadExtent() {
        List<PlayoffStage> loaded = ExtentPersistence.loadExtent(PlayoffStage.class);
        extent.clear();
        extent.addAll(loaded);
    }

    public PlayoffStage(int id,
                        String stageName,
                        int numberOfRounds,
                        String matchType) {

        super(id, stageName);
        setNumberOfRounds(numberOfRounds);
        setMatchType(matchType);
        addPlayoffStage(this);
    }
    public PlayoffStage(){
       super();
    }

    public void setNumberOfRounds(int numberOfRounds) {
        if (numberOfRounds <= 0) {
            throw new NegativeNumberException("Number of rounds", numberOfRounds);
        }
        if (numberOfRounds > 10) {
            throw new InvalidValueException("Number of rounds is unrealistically high.");
        }
        this.numberOfRounds = numberOfRounds;
    }

    public void setMatchType(String matchType) {
        if (matchType == null || matchType.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Match type");
        }
        if (matchType.trim().length() < 2) {
            throw new InvalidValueException("Match type must contain at least 2 characters.");
        }
        this.matchType = matchType.trim();
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public String getMatchType() {
        return matchType;
    }
}
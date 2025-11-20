package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

public class PlayoffStage extends Stage {

    private int numberOfRounds;
    private String matchType;

    public PlayoffStage(int id,
                        String stageName,
                        int numberOfRounds,
                        String matchType) {

        super(id, stageName);
        setNumberOfRounds(numberOfRounds);
        setMatchType(matchType);
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
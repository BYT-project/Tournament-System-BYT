package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Team {

    private static final int MAX_COACHES = 2;

    private String name;
    private String country;
    private String city;
    private int rankPoints;

    private final List<Player> players;
    private final List<Coach> coaches;

    public Team(String name, String country, String city, int rankPoints) {
        setName(name);
        setCountry(country);
        setCity(city);
        setRankPoints(rankPoints);

        this.players = new ArrayList<>();
        this.coaches = new ArrayList<>();
    }

    // ---------------------------
    //     PLAYER MANAGEMENT
    // ---------------------------

    public void addPlayer(Player player) {
        if (player == null) {
            throw new NullObjectException("Player");
        }
        if (players.contains(player)) {
            throw new InvalidValueException("This player is already in the team.");
        }
        players.add(player);
    }

    public void removePlayer(Player player) {
        if (player == null) {
            throw new NullObjectException("Player");
        }
        players.remove(player);
    }

    // ---------------------------
    //     COACH MANAGEMENT
    // ---------------------------

    public void addCoach(Coach coach) {
        if (coach == null) {
            throw new NullObjectException("Coach");
        }
        if (coaches.size() >= MAX_COACHES) {
            throw new InvalidValueException("A team can have at most " + MAX_COACHES + " coaches.");
        }
        if (coaches.contains(coach)) {
            throw new InvalidValueException("This coach is already assigned to the team.");
        }
        coaches.add(coach);
    }

    public void removeCoach(Coach coach) {
        if (coach == null) {
            throw new NullObjectException("Coach");
        }
        coaches.remove(coach);
    }

    // ---------------------------
    //       VALIDATION
    // ---------------------------

    public void setName(String name) {
        validateString(name, "Team name");
        this.name = name.trim();
    }

    public void setCountry(String country) {
        validateString(country, "Country");
        this.country = country.trim();
    }

    public void setCity(String city) {
        validateString(city, "City");
        this.city = city.trim();
    }

    public void setRankPoints(int rankPoints) {
        if (rankPoints < 0) {
            throw new NegativeNumberException("Rank points", rankPoints);
        }
        this.rankPoints = rankPoints;
    }

    // ---------------------------
    //      STRING VALIDATION
    // ---------------------------

    private void validateString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new NullOrEmptyStringException(fieldName);
        }
        String trimmed = value.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException(fieldName + " must contain at least 2 characters.");
        }
        if (!trimmed.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\-\\s]+")) {
            throw new InvalidValueException(fieldName + " contains invalid characters.");
        }
    }

    // ---------------------------
    //         GETTERS
    // ---------------------------

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public int getRankPoints() {
        return rankPoints;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public List<Coach> getCoaches() {
        return Collections.unmodifiableList(coaches);
    }
}
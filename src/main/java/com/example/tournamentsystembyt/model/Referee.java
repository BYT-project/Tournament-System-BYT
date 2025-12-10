package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Referee extends Person {
    private int experience; // in years

    private static final List<Referee> extent = new ArrayList<>();
    private final List<Match> matches = new ArrayList<>();

    private static void addReferee(Referee r) {
        if (r == null) {
            throw new IllegalArgumentException("Referee cannot be null");
        }
        extent.add(r);
    }

    public static List<Referee> getExtent() {
        return new ArrayList<>(extent); // defensive copy for encapsulation
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(Referee.class, extent);
    }

    public static void loadExtent() {
        List<Referee> loaded = ExtentPersistence.loadExtent(Referee.class);
        extent.clear();
        extent.addAll(loaded);
    }

    public Referee(String firstName,
                   String lastName,
                   LocalDate dateOfBirth,
                   String email,
                   String phone,
                   int experience) {
        super(firstName, lastName, dateOfBirth, email, phone);
        setExperience(experience);
        addReferee(this);
    }
    public Referee(){
        super();
    }

    public void setExperience(int experience) {
        if (experience < 0) {
            throw new NegativeNumberException("Experience", experience);
        }
        if (experience > 80) {
            throw new InvalidValueException("Experience seems unrealistically high.");
        }
        this.experience = experience;
    }

    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches);
    }

    public void addMatch(Match match) {
        if (match == null) {
            throw new NullObjectException("Match");
        }
        if (!matches.contains(match)) {
            matches.add(match);
        }
        if (!match.getReferees().contains(this)) {
            match.addReferee(this);
        }
    }

    public void removeMatch(Match match) {
        if (match == null) {
            throw new NullObjectException("Match");
        }
        if (!matches.contains(match)) {
            throw new InvalidValueException("Referee is not assigned to this match");
        }
        matches.remove(match);

        if (match.getReferees().contains(this)) {
            match.removeReferee(this);
        }
    }

    public int getExperience() {
        return experience;
    }
}
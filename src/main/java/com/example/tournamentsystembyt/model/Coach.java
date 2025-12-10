package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Coach extends Person {
    private String role;
    private int experience; // in years
    private final List<Team> teamsCoached;     // multi-valued attribute

    private static final List<Coach> extent = new ArrayList<>();


    private static void addCoach(Coach coach) {
        if (coach == null) {
            throw new NullObjectException("Coach");
        }
        extent.add(coach);
    }

    public static List<Coach> getExtent() {
        return new ArrayList<>(extent); // encapsulation
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(Coach.class, extent);
    }

    public static void loadExtent() {
        List<Coach> loaded = ExtentPersistence.loadExtent(Coach.class);
        extent.clear();
        for (Coach c : loaded) {
            addCoach(c);
        }
    }

    public Coach(String firstName,
                 String lastName,
                 LocalDate dateOfBirth,
                 String email,
                 String phone,
                 String role,
                 int experience) {
        super(firstName, lastName, dateOfBirth, email, phone);
        setRole(role);
        setExperience(experience);
        this.teamsCoached = new ArrayList<>();
        addCoach(this);
    }
    public Coach() {
        super();
        this.teamsCoached = new ArrayList<>();
    }

    public void addTeam(Team team) {
        if (team == null) {
            throw new NullObjectException("Team");
        }
        if (!teamsCoached.contains(team)) {
            teamsCoached.add(team);
        }
    }

    public void removeTeam(Team team) {
        teamsCoached.remove(team);
    }

    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Role");
        }
        String trimmed = role.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException("Role must contain at least 2 characters.");
        }
        this.role = trimmed;
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
    // package-private on purpose – only Team should call these
    void addTeamInternal(Team team) {
        if (team == null) {
            throw new NullObjectException("Team");
        }
        if (teamsCoached.contains(team)) {
            // duplication
            throw new InvalidValueException("Team is already in this coach's list.");
        }
        teamsCoached.add(team);
    }

    void removeTeamInternal(Team team) {
        if (team == null) {
            throw new NullObjectException("Team");
        }
        if (!teamsCoached.contains(team)) {
            throw new InvalidValueException("This coach is not assigned to the given team.");
        }
        // min multiplicity here is 0, so removal is allowed
        teamsCoached.remove(team);
    }

    public String getRole() {
        return role;
    }

    public int getExperience() {
        return experience;
    }

    public List<Team> getTeamsCoached() {
        return Collections.unmodifiableList(new ArrayList<>(teamsCoached));
    }
}
package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Coach extends Person {
    private String role;
    private int experience; // in years
    private final List<Team> teamsCoached;     // multi-valued attribute

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
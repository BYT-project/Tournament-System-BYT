package com.example.tournamentsystembyt.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Coach extends Person {
    private String role;
    private int experience;
    private List<Team> teamsCoached;  // multi-valued attribute, here the problem is that we can store only the teams from out system, and cannot from outside, we need to discuss it

    public Coach(String firstName, String lastName, LocalDate dateOfBirth, String email, String phone,
                 String role, int experience) {
        super(firstName, lastName, dateOfBirth, email, phone);
        setRole(role);
        setExperience(experience);
        this.teamsCoached = new ArrayList<>();
    }

    public void addTeam(Team team) {
        if (team == null)
            throw new IllegalArgumentException("Team cannot be null.");
        if (!teamsCoached.contains(team))
            teamsCoached.add(team);
    }

    public void removeTeam(Team team) {
        teamsCoached.remove(team);
    }

    public void setRole(String role) {
        if (role == null || role.trim().isEmpty())
            throw new IllegalArgumentException("Role cannot be empty.");
        this.role = role;
    }

    public void setExperience(int experience) {
        if (experience < 0)
            throw new IllegalArgumentException("Experience cannot be negative.");
        this.experience = experience;
    }

    public String getRole() { return role; }
    public int getExperience() { return experience; }
    public List<Team> getTeamsCoached() { return teamsCoached; }
}
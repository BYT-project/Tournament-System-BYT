package com.example.tournamentsystembyt.model;

import java.time.LocalDate;

public class Referee extends Person {
    private int experience;

    public Referee(String firstName, String lastName, LocalDate dateOfBirth, String email, String phone,
                   int experience) {
        super(firstName, lastName, dateOfBirth, email, phone);
        setExperience(experience);
    }

    public void setExperience(int experience) {
        if (experience < 0) throw new IllegalArgumentException("Experience cannot be negative.");
        this.experience = experience;
    }

    public int getExperience() { return experience; }
}
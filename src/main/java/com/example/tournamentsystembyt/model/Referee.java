package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;

import java.time.LocalDate;

public class Referee extends Person {
    private int experience; // in years

    public Referee(String firstName,
                   String lastName,
                   LocalDate dateOfBirth,
                   String email,
                   String phone,
                   int experience) {
        super(firstName, lastName, dateOfBirth, email, phone);
        setExperience(experience);
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

    public int getExperience() {
        return experience;
    }
}
package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Referee extends Person {
    private int experience; // in years

    private static final List<Referee> extent = new ArrayList<>();

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

    public int getExperience() {
        return experience;
    }
}
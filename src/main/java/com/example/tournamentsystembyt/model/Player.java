package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

import java.time.LocalDate;

public class Player extends Person {
    private double height;   // meters
    private double weight;   // kilograms
    private String position;
    private int shirtNum;

    public Player(String firstName,
                  String lastName,
                  LocalDate dateOfBirth,
                  String email,
                  String phone,
                  double height,
                  double weight,
                  String position,
                  int shirtNum) {
        super(firstName, lastName, dateOfBirth, email, phone);
        setHeight(height);
        setWeight(weight);
        setPosition(position);
        setShirtNum(shirtNum);
    }

    public void setHeight(double height) {
        if (height <= 0) {
            throw new NegativeNumberException("Height", height);
        }
        if (height < 1.2 || height > 2.5) {
            throw new InvalidValueException("Height should be between 1.2m and 2.5m.");
        }
        this.height = height;
    }

    public void setWeight(double weight) {
        if (weight <= 0) {
            throw new NegativeNumberException("Weight", weight);
        }
        if (weight < 30 || weight > 200) {
            throw new InvalidValueException("Weight should be between 30kg and 200kg.");
        }
        this.weight = weight;
    }

    public void setPosition(String position) {
        if (position == null || position.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Position");
        }
        String trimmed = position.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException("Position must contain at least 2 characters.");
        }
        this.position = trimmed;
    }

    public void setShirtNum(int shirtNum) {
        if (shirtNum <= 0) {
            throw new NegativeNumberException("Shirt number", shirtNum);
        }
        if (shirtNum > 99) {
            throw new InvalidValueException("Shirt number must be between 1 and 99.");
        }
        this.shirtNum = shirtNum;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public String getPosition() {
        return position;
    }

    public int getShirtNum() {
        return shirtNum;
    }
}
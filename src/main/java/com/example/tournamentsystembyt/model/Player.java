package com.example.tournamentsystembyt.model;

import java.time.LocalDate;

public class Player extends Person {
    private double height;
    private double weight;
    private String position;
    private int shirtNum;

    public Player(String firstName, String lastName, LocalDate dateOfBirth, String email, String phone,
                  double height, double weight, String position, int shirtNum) {
        super(firstName, lastName, dateOfBirth, email, phone);
        setHeight(height);
        setWeight(weight);
        setPosition(position);
        setShirtNum(shirtNum);
    }

    public void setHeight(double height) {
        if (height <= 0) throw new IllegalArgumentException("Height must be positive.");
        this.height = height;
    }

    public void setWeight(double weight) {
        if (weight <= 0) throw new IllegalArgumentException("Weight must be positive.");
        this.weight = weight;
    }

    public void setPosition(String position) {
        if (position == null || position.trim().isEmpty())
            throw new IllegalArgumentException("Position cannot be empty.");
        this.position = position;
    }

    public void setShirtNum(int shirtNum) {
        if (shirtNum <= 0) throw new IllegalArgumentException("Shirt number must be positive.");
        this.shirtNum = shirtNum;
    }

    public double getHeight() { return height; }
    public double getWeight() { return weight; }
    public String getPosition() { return position; }
    public int getShirtNum() { return shirtNum; }
}
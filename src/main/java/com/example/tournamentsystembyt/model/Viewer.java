package com.example.tournamentsystembyt.model;

import java.time.LocalDate;

public class Viewer extends Person {
    public Viewer(String firstName,
                  String lastName,
                  LocalDate dateOfBirth,
                  String email,
                  String phone) {
        super(firstName, lastName, dateOfBirth, email, phone);
    }
}
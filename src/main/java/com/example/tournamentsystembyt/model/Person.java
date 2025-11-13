package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;

import java.time.LocalDate;
import java.time.Period;

public abstract class Person {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;

    public Person(String firstName, String lastName, LocalDate dateOfBirth, String email, String phone) {
        setFirstName(firstName);
        setLastName(lastName);
        setDateOfBirth(dateOfBirth);
        setEmail(email);
        setPhone(phone);
    }

    public int getAge() { // derived attribute
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty())
            throw new InvalidValueException("First name cannot be empty.");
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty())
            throw new InvalidValueException("Last name cannot be empty.");
        this.lastName = lastName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null)
            throw new InvalidValueException("Date of birth cannot be null.");
        if (dateOfBirth.isAfter(LocalDate.now()))
            throw new InvalidValueException("Date of birth cannot be in the future.");
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@"))
            throw new InvalidValueException("Invalid email address.");
        this.email = email;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty())
            throw new InvalidValueException("Phone number cannot be empty.");
        this.phone = phone;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
}
package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidDateException;
import com.example.tournamentsystembyt.exceptions.InvalidEmailException;
import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

import java.time.LocalDate;
import java.time.Period;

public abstract class Person {
    private static final int MAX_HUMAN_AGE_YEARS = 120;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;

    public Person(String firstName,
                  String lastName,
                  LocalDate dateOfBirth,
                  String email,
                  String phone) {
        setFirstName(firstName);
        setLastName(lastName);
        setDateOfBirth(dateOfBirth);
        setEmail(email);
        setPhone(phone);
    }
    protected Person(){


    }

    // Derived attribute
    public int getAge() {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new NullOrEmptyStringException("First name");
        }
        String trimmed = firstName.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException("First name must contain at least 2 characters.");
        }
        if (!trimmed.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\-\\s]+")) {
            throw new InvalidValueException("First name contains invalid characters.");
        }
        this.firstName = trimmed;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Last name");
        }
        String trimmed = lastName.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException("Last name must contain at least 2 characters.");
        }
        if (!trimmed.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\-\\s]+")) {
            throw new InvalidValueException("Last name contains invalid characters.");
        }
        this.lastName = trimmed;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new NullObjectException("Date of birth");
        }

        LocalDate today = LocalDate.now();
        if (dateOfBirth.isAfter(today)) {
            throw new InvalidDateException("Date of birth cannot be in the future.");
        }

        if (dateOfBirth.isBefore(today.minusYears(MAX_HUMAN_AGE_YEARS))) {
            throw new InvalidDateException("Date of birth is unrealistically old.");
        }

        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Email");
        }
        String trimmed = email.trim();
        if (!trimmed.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new InvalidEmailException(trimmed);
        }
        this.email = trimmed;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Phone number");
        }
        String trimmed = phone.trim();
        if (!trimmed.matches("^[+0-9][0-9\\-\\s]{5,}$")) {
            throw new InvalidValueException("Phone number has an invalid format.");
        }
        this.phone = trimmed;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
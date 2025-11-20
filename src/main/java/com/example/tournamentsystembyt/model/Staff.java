package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

import java.time.LocalDate;

public class Staff extends Person {
    private String jobTitle;
    private double salary;

    public Staff(String firstName,
                 String lastName,
                 LocalDate dateOfBirth,
                 String email,
                 String phone,
                 String jobTitle,
                 double salary) {
        super(firstName, lastName, dateOfBirth, email, phone);
        setJobTitle(jobTitle);
        setSalary(salary);
    }

    public void setJobTitle(String jobTitle) {
        if (jobTitle == null || jobTitle.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Job title");
        }
        String trimmed = jobTitle.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException("Job title must contain at least 2 characters.");
        }
        this.jobTitle = trimmed;
    }

    public void setSalary(double salary) {
        if (salary < 0) {
            throw new NegativeNumberException("Salary", salary);
        }
        this.salary = salary;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public double getSalary() {
        return salary;
    }
}
package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Staff extends Person {
    private String jobTitle;
    private double salary;

    private static final List<Staff> extent = new ArrayList<>();

    private static void addStaff(Staff s) {
        if (s == null) throw new IllegalArgumentException("Staff cannot be null");
        extent.add(s);
    }

    public static List<Staff> getExtent() {
        return new ArrayList<>(extent); // defensive copy
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(Staff.class, extent);
    }

    public static void loadExtent() {
        List<Staff> loaded = ExtentPersistence.loadExtent(Staff.class);
        extent.clear();
        extent.addAll(loaded);
    }

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
        addStaff(this);
    }

    public Staff() {
        super();
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
package com.example.tournamentsystembyt.model;

import java.time.LocalDate;

public class Staff extends Person {
    private String jobTitle;
    private double salary;

    public Staff(String firstName, String lastName, LocalDate dateOfBirth, String email, String phone,
                 String jobTitle, double salary) {
        super(firstName, lastName, dateOfBirth, email, phone);
        setJobTitle(jobTitle);
        setSalary(salary);
    }

    public void setJobTitle(String jobTitle) {
        if (jobTitle == null || jobTitle.trim().isEmpty())
            throw new IllegalArgumentException("Job title cannot be empty.");
        this.jobTitle = jobTitle;
    }

    public void setSalary(double salary) {
        if (salary < 0) throw new IllegalArgumentException("Salary cannot be negative.");
        this.salary = salary;
    }

    public String getJobTitle() { return jobTitle; }
    public double getSalary() { return salary; }
}
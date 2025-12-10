package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Staff extends Person {
    private String jobTitle;
    private double salary;
    private final List<Match> matches = new ArrayList<>();
    private Staff supervisor;
    //Supervisees (noun, plural) refers to people who are being supervised by someone else.
    private final List<Staff> supervisees = new ArrayList<>();

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
    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches);
    }

    public void addMatch(Match match) {
        if (match == null) {
            throw new NullObjectException("Match");
        }
        if (!matches.contains(match)) {
            matches.add(match);
        }
        if (!match.getStaffMembers().contains(this)) {
            match.addStaff(this);
        }
    }

    public void removeMatch(Match match) {
        if (match == null) {
            throw new NullObjectException("Match");
        }
        if (!matches.contains(match)) {
            throw new InvalidValueException("Staff is not assigned to this match");
        }
        if (matches.size() == 1) {
            throw new InvalidValueException("Staff must supervise at least one match (1..* multiplicity)");
        }

        matches.remove(match);

        if (match.getStaffMembers().contains(this)) {
            match.removeStaff(this);
        }
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public double getSalary() {
        return salary;
    }


    public Staff getSupervisor() {
        return supervisor;
    }

    public List<Staff> getSupervisees() {
        return Collections.unmodifiableList(supervisees);
    }

    public void setSupervisor(Staff newSupervisor) {
        if (newSupervisor == this) {
            throw new IllegalArgumentException("A staff member cannot supervise themselves.");
        }
        if (this.supervisor == newSupervisor) {
            return;
        }
        if (this.supervisor != null) {
            this.supervisor.supervisees.remove(this);
        }
        this.supervisor = newSupervisor;
        if (newSupervisor != null && !newSupervisor.supervisees.contains(this)) {
            newSupervisor.supervisees.add(this);
        }
    }
    public void addSupervisee(Staff supervisee) {
        if (supervisee == null) {
            throw new IllegalArgumentException("Supervisee cannot be null.");
        }
        if (supervisee == this) {
            throw new IllegalArgumentException("A staff member cannot supervise themselves.");
        }
        if (this.supervisees.contains(supervisee)) {
            throw new InvalidValueException("This staff member already supervises the given supervisee.");
        }

        if (supervisee.supervisor != null && supervisee.supervisor != this) {
            supervisee.supervisor.supervisees.remove(supervisee);
        }

        supervisee.supervisor = this;
        this.supervisees.add(supervisee);

    }
    public void removeSupervisee(Staff supervisee) {
        if (supervisee == null) {
            return;
        }
        if (!this.supervisees.contains(supervisee)) {
            throw new InvalidValueException("The given staff member is not supervised by this supervisor.");
        }
        if (this.supervisees.remove(supervisee)) {
            if (supervisee.supervisor == this) {
                supervisee.supervisor = null;
            }
        }
    }
}
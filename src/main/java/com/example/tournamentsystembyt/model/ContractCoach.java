package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidDateException;
import com.example.tournamentsystembyt.exceptions.InvalidStateException;
import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContractCoach {

    private Coach coach;
    private Team team;

    private LocalDate startDate;
    private LocalDate endDate;   // 0..1
    private boolean isActive;
    private double salary;
    private String description;

    private static final List<ContractCoach> extent = new ArrayList<>();

    private static void addContract(ContractCoach c) {
        if (c == null) throw new IllegalArgumentException("ContractCoach cannot be null");
        extent.add(c);
    }

    public static List<ContractCoach> getExtent() {
        return new ArrayList<>(extent);
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(ContractCoach.class, extent);
    }

    public static void loadExtent() {
        List<ContractCoach> loaded = ExtentPersistence.loadExtent(ContractCoach.class);
        extent.clear();
        extent.addAll(loaded);
    }

    public ContractCoach(Coach coach,
                         Team team,
                         LocalDate startDate,
                         double salary,
                         String description) {
        this(coach, team, startDate, null, true, salary, description);
    }

    public ContractCoach(Coach coach,
                         Team team,
                         LocalDate startDate,
                         LocalDate endDate,
                         boolean isActive,
                         double salary,
                         String description) {

        setCoach(coach);
        setTeam(team);
        setStartDate(startDate);
        setEndDate(endDate);
        setSalary(salary);
        setDescription(description);
        this.isActive = isActive;

        if (this.endDate != null && this.isActive) {
            throw new InvalidValueException("Contract cannot be active if it already has an end date.");
        }

        addContract(this);
    }

    public Coach getCoach() { return coach; }
    public Team getTeam() { return team; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public boolean isActive() { return isActive; }
    public double getSalary() { return salary; }
    public String getDescription() { return description; }

    public void setCoach(Coach coach) {
        if (coach == null) throw new NullObjectException("Coach");
        this.coach = coach;
        // later: coach.internalAddCoachContract(this);
    }

    public void setTeam(Team team) {
        if (team == null) throw new NullObjectException("Team");
        this.team = team;
        // later: team.internalAddCoachContract(this);
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) throw new NullObjectException("Start date");
        if (startDate.isBefore(LocalDate.of(1950, 1, 1))) {
            throw new InvalidDateException("Start date is unrealistically old.");
        }
        if (this.endDate != null && startDate.isAfter(this.endDate)) {
            throw new InvalidDateException("Start date cannot be after end date.");
        }
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new InvalidDateException("End date cannot be before start date.");
        }
        this.endDate = endDate;
        if (endDate != null) {
            this.isActive = false;
        }
    }

    public void setSalary(double salary) {
        if (salary < 0) {
            throw new NegativeNumberException("Salary", salary);
        }
        this.salary = salary;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Contract description");
        }
        this.description = description.trim();
    }

    public void terminate(LocalDate endDate) {
        if (!isActive) {
            throw new InvalidValueException("Contract is already inactive.");
        }
        setEndDate(endDate != null ? endDate : LocalDate.now());
    }

    public void changeSalary(double newSalary) {
        if (!isActive) {
            throw new InvalidStateException("Cannot change salary on inactive contract.");
        }
        setSalary(newSalary);
    }

    public void reactivate() {
        if (isActive) {
            throw new InvalidStateException("Contract is already active.");
        }
        this.isActive = true;
        this.endDate = null;
    }

    public void delete() {
        // later remove from coach & team collections
        extent.remove(this);
    }
}
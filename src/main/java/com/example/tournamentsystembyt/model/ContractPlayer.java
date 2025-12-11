package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.*;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContractPlayer {

    private Player player;
    private Team team;

    private LocalDate startDate;
    private LocalDate endDate;   // 0..1
    private boolean isActive;
    private double salary;
    private String description;

    private static final List<ContractPlayer> extent = new ArrayList<>();

    // ---- extent management ----
    private static void addContract(ContractPlayer c) {
        if (c == null) throw new IllegalArgumentException("ContractPlayer cannot be null");
        extent.add(c);
    }

    public static List<ContractPlayer> getExtent() {
        return new ArrayList<>(extent);
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(ContractPlayer.class, extent);
    }

    public static void loadExtent() {
        List<ContractPlayer> loaded = ExtentPersistence.loadExtent(ContractPlayer.class);
        extent.clear();
        extent.addAll(loaded);
    }

    // ---- constructors ----

    public ContractPlayer(Player player,
                          Team team,
                          LocalDate startDate,
                          double salary,
                          String description) {
        this(player, team, startDate, null, true, salary, description);
    }

    public ContractPlayer(Player player,
                          Team team,
                          LocalDate startDate,
                          LocalDate endDate,
                          boolean isActive,
                          double salary,
                          String description) {

        setPlayer(player);
        setTeam(team);
        setStartDate(startDate);
        setEndDate(endDate);
        setSalary(salary);
        setDescription(description);
        this.isActive = isActive;

        // if we have an endDate, contract is not active
        if (this.endDate != null && this.isActive) {
            throw new InvalidValueException("Contract cannot be active if it already has an end date.");
        }

        addContract(this);

        // Register with Player and Team (reverse connections)
        if (this.player != null) {
            this.player.internalAddPlayerContract(this);
        }
        if (this.team != null) {
            this.team.internalAddPlayerContract(this);
        }
    }


    public Player getPlayer() { return player; }
    public Team getTeam() { return team; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public boolean isActive() { return isActive; }
    public double getSalary() { return salary; }
    public String getDescription() { return description; }


    public void setPlayer(Player player) {
        if (player == null) throw new NullObjectException("Player");
        this.player = player;
        // registration is done in constructor to avoid duplicate calls
    }

    public void setTeam(Team team) {
        if (team == null) throw new NullObjectException("Team");
        this.team = team;
        // registration is done in constructor to avoid duplicate calls
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

    // if we need to remove from extend
    public void delete() {
        // remove from player & team collections
        if (player != null) {
            player.internalRemovePlayerContract(this);
        }
        if (team != null) {
            team.internalRemovePlayerContract(this);
        }
        extent.remove(this);
    }
}
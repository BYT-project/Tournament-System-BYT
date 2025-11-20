package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidDateException;
import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Tournament {

    private String name;
    private String sportType;
    private Date startDate;
    private Date endDate;
    private double prizePool;
    private String championName;        // optional
    private Double registrationFee;     // optional
    private int nextStageId = 1;  // for auto-incrementing stage IDs

    private final List<MediaPartner> mediaPartners;
    private final List<Stage> stages;
    private final List<Team> teams;

    public Tournament(String name,
                      String sportType,
                      Date startDate,
                      Date endDate,
                      double prizePool) {

        setName(name);
        setSportType(sportType);
        setStartDate(startDate);
        setEndDate(endDate);
        setPrizePool(prizePool);

        this.mediaPartners = new ArrayList<>();
        this.stages = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    // ------------------------------
    //       MEDIA PARTNERS
    // ------------------------------

    public void addMediaPartner(MediaPartner partner) {
        if (partner == null) {
            throw new NullObjectException("Media partner");
        }
        if (mediaPartners.contains(partner)) {
            throw new InvalidValueException("This media partner is already registered.");
        }
        mediaPartners.add(partner);
    }

    // ------------------------------
    //       STAGE MANAGEMENT
    // ------------------------------

    public void addStage(Stage stage) {
        if (stage == null) {
            throw new NullObjectException("Stage");
        }
        if (stages.contains(stage)) {
            throw new InvalidValueException("This stage is already part of the tournament.");
        }
        stages.add(stage);
    }

    public GroupStage createGroupStage(String stageName,
                                       int numberOfGroups,
                                       int teamsPerGroup) {

        if (stageName == null || stageName.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Stage name");
        }

        GroupStage stage = new GroupStage(
                nextStageId++,
                stageName.trim(),
                numberOfGroups,
                teamsPerGroup
        );
        stages.add(stage);
        return stage;
    }

    public PlayoffStage createPlayoffStage(String stageName,
                                           int numberOfRounds,
                                           String matchType) {

        if (stageName == null || stageName.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Stage name");
        }

        PlayoffStage stage = new PlayoffStage(
                nextStageId++,
                stageName.trim(),
                numberOfRounds,
                matchType
        );
        stages.add(stage);
        return stage;
    }

    // ------------------------------
    //         TEAM MANAGEMENT
    // ------------------------------

    public void addTeam(Team team) {
        if (team == null) {
            throw new NullObjectException("Team");
        }
        if (teams.contains(team)) {
            throw new InvalidValueException("This team is already registered in the tournament.");
        }
        teams.add(team);
    }

    public void removeTeam(Team team) {
        if (team == null) {
            throw new NullObjectException("Team");
        }
        teams.remove(team);
    }

    // ------------------------------
    //        TOURNAMENT FLOW
    // ------------------------------

    public void createTournament() {
        if (teams.isEmpty()) {
            throw new InvalidValueException("A tournament must have at least one team.");
        }
        if (stages.isEmpty()) {
            throw new InvalidValueException("Tournament must contain at least one stage before creation.");
        }
        // Additional logic goes here
    }

    public void generateSchedule() {
        if (teams.size() < 2) {
            throw new InvalidValueException("At least two teams are required to generate a schedule.");
        }
        // Additional logic goes here
    }

    public void start() {
        if (startDate.after(new Date())) {
            throw new InvalidValueException("Cannot start the tournament before the start date.");
        }
        // Additional logic goes here
    }

    public void finish() {
        // Simple check
        if (championName == null || championName.trim().isEmpty()) {
            throw new InvalidValueException("Tournament cannot finish without selecting a champion.");
        }
        // Additional logic goes here
    }

    public void cancelTournament() {
        // Logic for cancelling a tournament (status, refunds, etc.)
    }

    // ------------------------------
    //           SETTERS
    // ------------------------------

    public void setChampion(String championName) {
        if (championName == null) {
            this.championName = null; // clearing is allowed
            return;
        }
        if (championName.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Champion name");
        }
        if (championName.trim().length() < 2) {
            throw new InvalidValueException("Champion name must contain at least 2 characters.");
        }
        this.championName = championName.trim();
    }

    public void setName(String name) {
        validateString(name, "Tournament name");
        this.name = name.trim();
    }

    public void setSportType(String sportType) {
        validateString(sportType, "Sport type");
        this.sportType = sportType.trim();
    }

    public void setStartDate(Date startDate) {
        if (startDate == null) {
            throw new NullObjectException("Start date");
        }
        Date now = new Date();
        if (startDate.after(now)) {
            throw new InvalidDateException("Start date cannot be in the future.");
        }
        this.startDate = new Date(startDate.getTime()); // defensive copy
    }

    public void setEndDate(Date endDate) {
        if (endDate == null) {
            throw new NullObjectException("End date");
        }
        if (startDate != null && endDate.before(startDate)) {
            throw new InvalidDateException("End date cannot be earlier than start date.");
        }
        this.endDate = new Date(endDate.getTime());
    }

    public void setPrizePool(double prizePool) {
        if (prizePool < 0) {
            throw new NegativeNumberException("Prize pool", prizePool);
        }
        this.prizePool = prizePool;
    }

    public void setRegistrationFee(Double registrationFee) {
        if (registrationFee != null && registrationFee < 0) {
            throw new NegativeNumberException("Registration fee", registrationFee);
        }
        this.registrationFee = registrationFee;
    }

    // ------------------------------
    //       STRING VALIDATION
    // ------------------------------

    private void validateString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new NullOrEmptyStringException(fieldName);
        }
        String trimmed = value.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException(fieldName + " must contain at least 2 characters.");
        }
        if (!trimmed.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\-\\s]+")) {
            throw new InvalidValueException(fieldName + " contains invalid characters.");
        }
    }

    // ------------------------------
    //           GETTERS
    // ------------------------------

    public String getName() {
        return name;
    }

    public String getSportType() {
        return sportType;
    }

    public Date getStartDate() {
        return new Date(startDate.getTime());
    }

    public Date getEndDate() {
        return new Date(endDate.getTime());
    }

    public double getPrizePool() {
        return prizePool;
    }

    public String getChampionName() {
        return championName;
    }

    public Double getRegistrationFee() {
        return registrationFee;
    }

    public List<MediaPartner> getMediaPartners() {
        return Collections.unmodifiableList(mediaPartners);
    }

    public List<Stage> getStages() {
        return Collections.unmodifiableList(stages);
    }

    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }
}
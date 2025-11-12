package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.model.MediaPartner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tournament {
    private String name;
    private String sportType;
    private Date startDate;
    private Date endDate;
    private double prizePool;
    private String championName;          // optional
    private Double registrationFee;       // optional

    private List<MediaPartner> mediaPartners;
    private List<Stage> stages;
    private List<Team> teams;

    public Tournament(String name, String sportType, Date startDate, Date endDate, double prizePool) {
        setName(name);
        setSportType(sportType);
        setStartDate(startDate);
        setEndDate(endDate);
        setPrizePool(prizePool);
        this.mediaPartners = new ArrayList<>();
        this.stages = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    public void addMediaPartner(MediaPartner partner) {

    }

    public void addStage(Stage stage) {

    }

    public void addTeam(Team team) {

    }

    public void removeTeam(Team team) {

    }

    public void createTournament() {
    }

    public void createStage(String stageName) {
    }

    public void generateSchedule() {

    }

    public void start() {

    }

    public void finish() {
    }

    public void cancelTournament() {

    }

    public void setChampion(String championName) {

    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new InvalidValueException("Tournament name cannot be empty.");
        this.name = name;
    }

    public void setSportType(String sportType) {
        if (sportType == null || sportType.trim().isEmpty())
            throw new InvalidValueException("Sport type cannot be empty.");
        this.sportType = sportType;
    }

    public void setStartDate(Date startDate) {
        if (startDate == null)
            throw new InvalidValueException("Start date cannot be null.");
        if (startDate.after(new Date()))
            throw new InvalidValueException("Start date cannot be in the future.");
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        if (endDate == null)
            throw new InvalidValueException("End date cannot be null.");
        if (startDate != null && endDate.before(startDate))
            throw new InvalidValueException("End date cannot be before start date.");
        this.endDate = endDate;
    }

    public void setPrizePool(double prizePool) {
        if (prizePool < 0)
            throw new InvalidValueException("Prize pool cannot be negative.");
        this.prizePool = prizePool;
    }

    public void setRegistrationFee(Double registrationFee) {
        if (registrationFee != null && registrationFee < 0)
            throw new InvalidValueException("Registration fee cannot be negative.");
        this.registrationFee = registrationFee;
    }

    public String getName() { return name; }
    public String getSportType() { return sportType; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public double getPrizePool() { return prizePool; }
    public String getChampionName() { return championName; }
    public Double getRegistrationFee() { return registrationFee; }
    public List<MediaPartner> getMediaPartners() { return mediaPartners; }
    public List<Stage> getStages() { return stages; }
    public List<Team> getTeams() { return teams; }
}

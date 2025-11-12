package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;

import java.time.LocalDate;
import java.time.LocalTime;

public class Match {
    private LocalDate startDate;
    private LocalTime startTime;
    private String status;
    private int homeScore;
    private int awayScore;
    private int winnerTeamId;

    private Stage stage;

    public Match(LocalDate startDate, LocalTime startTime, String status, Stage stage) {
        setStartDate(startDate);
        setStartTime(startTime);
        setStatus(status);
        setStage(stage);
    }

    public void assignReferee() {

    }
    public void start() {

    }
    public void pause() {

    }
    public void end() {

    }

    public void recordEvent() {

    }

    public void cancelMatch() {

    }

    public void setResults(int homeScore, int awayScore, int winnerTeamId) {

    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null)
            throw new InvalidValueException("Start date cannot be null.");
        this.startDate = startDate;
    }

    public void setStartTime(LocalTime startTime) {
        if (startTime == null)
            throw new InvalidValueException("Start time cannot be null.");
        this.startTime = startTime;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty())
            throw new InvalidValueException("Status cannot be empty.");
        this.status = status;
    }

    public void setStage(Stage stage) {
        if (stage == null)
            throw new InvalidValueException("Match must belong to a valid Stage.");
        this.stage = stage;
    }

    public LocalDate getStartDate() { return startDate; }
    public LocalTime getStartTime() { return startTime; }
    public String getStatus() { return status; }
    public Integer getHomeScore() { return homeScore; }
    public Integer getAwayScore() { return awayScore; }
    public Integer getWinnerTeamId() { return winnerTeamId; }
    public Stage getStage() { return stage; }
}

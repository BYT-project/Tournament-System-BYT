package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidDateException;
import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Match {

    private LocalDate startDate;
    private LocalTime startTime;
    private String status;          // "Scheduled", "Ongoing", "Finished", "Cancelled"
    private Integer homeScore;
    private Integer awayScore;
    private Integer winnerTeamId;

    private final List<TournamentTicket> tournamentTickets = new ArrayList<>();// nullable until match ends

    private Stage stage;
    private static final List<Match> extent = new ArrayList<>();
    private static void addMatch(Match match) {
        if (match == null) {
            throw new IllegalArgumentException("Match cannot be null");
        }
        extent.add(match);
    }

    public static List<Match> getExtent() {
        // encapsulation – defensive copy
        return new ArrayList<>(extent);
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(Match.class, extent);
    }

    public static void loadExtent() {
        List<Match> loaded = ExtentPersistence.loadExtent(Match.class);
        extent.clear();
        extent.addAll(loaded);
    }

    public Match(LocalDate startDate,
                 LocalTime startTime,
                 String status,
                 Stage stage) {

        setStartDate(startDate);
        setStartTime(startTime);
        setStatus(status);
        setStage(stage);

        this.homeScore = null;
        this.awayScore = null;
        this.winnerTeamId = null;
        addMatch(this);
    }

    public Match() {
        // Default constructor
    }

    void internalAddTournamentTicket(TournamentTicket ticket) {
        if (!tournamentTickets.contains(ticket)) {
            tournamentTickets.add(ticket);
        }
    }

    void internalRemoveTournamentTicket(TournamentTicket ticket) {
        tournamentTickets.remove(ticket);
    }

    public List<TournamentTicket> getTournamentTickets() {
        return Collections.unmodifiableList(tournamentTickets);
    }

    public void assignReferee() {
        // To be implemented
    }

    public void start() {
        if (!status.equalsIgnoreCase("Scheduled")) {
            throw new InvalidValueException("Match cannot start unless it is in 'Scheduled' state.");
        }
        this.status = "Ongoing";
    }

    public void pause() {
        if (!status.equalsIgnoreCase("Ongoing")) {
            throw new InvalidValueException("Match can only be paused while ongoing.");
        }
        this.status = "Paused";
    }

    public void end() {
        if (!status.equalsIgnoreCase("Ongoing") && !status.equalsIgnoreCase("Paused")) {
            throw new InvalidValueException("Match can only be ended when ongoing or paused.");
        }
        if (homeScore == null || awayScore == null) {
            throw new InvalidValueException("Cannot end match without final scores.");
        }
        this.status = "Finished";
    }

    public void cancelMatch() {
        if (status.equalsIgnoreCase("Finished")) {
            throw new InvalidValueException("A finished match cannot be cancelled.");
        }
        this.status = "Cancelled";
    }

    public void recordEvent() {
        // Event handling logic later
    }

    public void setResults(int homeScore, int awayScore, int winnerTeamId) {
        if (homeScore < 0) throw new NegativeNumberException("Home score", homeScore);
        if (awayScore < 0) throw new NegativeNumberException("Away score", awayScore);

        // winnerTeamId may be 0 or negative → reject
        if (winnerTeamId <= 0) {
            throw new InvalidValueException("Winner team ID must be a positive integer.");
        }

        if (homeScore == awayScore && winnerTeamId != 0) {
            throw new InvalidValueException("Match cannot have a winner in a draw.");
        }

        this.homeScore = homeScore;
        this.awayScore = awayScore;

        // If draw — no winner (or assign special behavior)
        if (homeScore == awayScore) {
            this.winnerTeamId = null;
        } else {
            this.winnerTeamId = winnerTeamId;
        }
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new NullObjectException("Start date");
        }
        if (startDate.isBefore(LocalDate.of(1900, 1, 1))) {
            throw new InvalidDateException("Start date is unrealistically old.");
        }
        this.startDate = startDate;
    }

    public void setStartTime(LocalTime startTime) {
        if (startTime == null) {
            throw new NullObjectException("Start time");
        }
        this.startTime = startTime;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Status");
        }
        String trimmed = status.trim();

        if (!trimmed.matches("(?i)Scheduled|Ongoing|Paused|Finished|Cancelled")) {
            throw new InvalidValueException("Invalid match status: " + trimmed);
        }

        this.status = trimmed;
    }

    public void setStage(Stage stage) {
        if (stage == null) {
            throw new NullObjectException("Stage");
        }
        if (this.stage == stage) {
            return;
        }
        // remove from old stage
        if (this.stage != null) {
            this.stage.internalRemoveMatch(this);
        }
        this.stage = stage;
        stage.internalAddMatch(this);
    }

    // NEW – used from Stage.delete()
    public void delete() {

        // remove tickets → ticket will keep stadium + tournament but lose match
        for (TournamentTicket ticket : new ArrayList<>(tournamentTickets)) {
            ticket.setMatch(null); // clears reverse association
        }

        // detach from stage
        if (stage != null) {
            Stage oldStage = stage;
            stage = null;
            oldStage.internalRemoveMatch(this);
        }

        extent.remove(this);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public String getStatus() {
        return status;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public Integer getWinnerTeamId() {
        return winnerTeamId;
    }

    public Stage getStage() {
        return stage;
    }
}
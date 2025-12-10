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

    private final List<Team> teams = new ArrayList<>();
    private final List<Referee> referees = new ArrayList<>();
    private final List<Staff> staffMembers = new ArrayList<>();
    private Stadium stadium;



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

    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }

    public void addTeam(Team team) {
        if (team == null) {
            throw new NullObjectException("Team");
        }
        if (teams.contains(team)) {
            throw new InvalidValueException("Team is already in this match");
        }
        if (teams.size() >= 2) {
            throw new InvalidValueException("Match cannot have more than 2 teams");
        }

        teams.add(team);

        if (!team.getMatches().contains(this)) {
            team.addMatch(this);
        }
    }

    public void removeTeam(Team team) {
        if (team == null) {
            throw new NullObjectException("Team");
        }
        if (!teams.contains(team)) {
            throw new InvalidValueException("Team is not part of this match");
        }

        teams.remove(team);

        if (team.getMatches().contains(this)) {
            team.removeMatch(this);
        }
    }

    public List<Referee> getReferees() {
        return Collections.unmodifiableList(referees);
    }

    public void addReferee(Referee referee) {
        if (referee == null) {
            throw new NullObjectException("Referee");
        }
        if (referees.contains(referee)) {
            throw new InvalidValueException("Referee already assigned to this match");
        }

        referees.add(referee);

        if (!referee.getMatches().contains(this)) {
            referee.addMatch(this);
        }
    }

    public void removeReferee(Referee referee) {
        if (referee == null) {
            throw new NullObjectException("Referee");
        }
        if (!referees.contains(referee)) {
            throw new InvalidValueException("Referee is not assigned to this match");
        }

        referees.remove(referee);

        if (referee.getMatches().contains(this)) {
            referee.removeMatch(this);
        }
    }

    public List<Staff> getStaffMembers() {
        return Collections.unmodifiableList(staffMembers);
    }

    public void addStaff(Staff staff) {
        if (staff == null) {
            throw new NullObjectException("Staff");
        }
        if (staffMembers.contains(staff)) {
            throw new InvalidValueException("Staff already assigned to this match");
        }

        staffMembers.add(staff);

        if (!staff.getMatches().contains(this)) {
            staff.addMatch(this);
        }
    }

    public void removeStaff(Staff staff) {
        if (staff == null) {
            throw new NullObjectException("Staff");
        }
        if (!staffMembers.contains(staff)) {
            throw new InvalidValueException("Staff is not assigned to this match");
        }

        staffMembers.remove(staff);

        if (staff.getMatches().contains(this)) {
            staff.removeMatch(this);
        }
    }

    public Stadium getStadium() {
        return stadium;
    }

    public void setStadium(Stadium newStadium) {
        if (newStadium == null) {
            throw new NullObjectException("Stadium");
        }
        if (this.stadium == newStadium) {
            return;
        }

        if (this.stadium != null) {
            Stadium old = this.stadium;
            this.stadium = null;
            if (old.getMatch() == this) {
                old.setMatch(null);
            }
        }

        if (newStadium.getMatch() != null && newStadium.getMatch() != this) {
            throw new InvalidValueException("Stadium already has a match assigned (1–1 multiplicity)");
        }

        this.stadium = newStadium;

        if (newStadium.getMatch() != this) {
            newStadium.setMatch(this);
        }
    }

    public void clearStadium() {
        if (this.stadium == null) {
            throw new InvalidValueException("Match has no stadium assigned");
        }
        setStadium(this.stadium);
        this.stadium = null;
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
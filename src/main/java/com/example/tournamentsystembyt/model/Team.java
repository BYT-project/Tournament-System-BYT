package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Team {
    private String name;
    private String country;
    private String city;
    private int rankPoints;

    private List<Player> players;
    private List<Coach> coaches;

    private final List<Match> matches = new ArrayList<>();

    // contract histories (bag of contracts)
    private final List<ContractPlayer> playerContracts = new ArrayList<>();
    private final List<ContractCoach> coachContracts = new ArrayList<>();

    public Team(String name, String country, String city, int rankPoints) {
        setName(name);
        setCountry(country);
        setCity(city);
        setRankPoints(rankPoints);
        this.players = new ArrayList<>();
        this.coaches = new ArrayList<>();

        addTeam(this);
    }
    private static void addTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null");
        }
        extent.add(team);
    }

    public Team() {
        // Must NOT add to extent
        this.players = new ArrayList<>();
        this.coaches = new ArrayList<>();
    }


    public void addMatch(Match match) {
        if (match == null) {
            throw new NullObjectException("Match");
        }
        if (!matches.contains(match)) {
            matches.add(match);
        }
        if (!match.getTeams().contains(this)) {
            match.addTeam(this);
        }
    }

    public void removeMatch(Match match) {
        if (match == null) {
            throw new NullObjectException("Match");
        }
        if (!matches.contains(match)) {
            throw new InvalidValueException("Team is not part of this match");
        }
        matches.remove(match);

        if (match.getTeams().contains(this)) {
            match.removeTeam(this);
        }
    }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Team name cannot be empty.");
        this.name = name;
    }

    public void setCountry(String country) {
        if (country == null || country.trim().isEmpty())
            throw new IllegalArgumentException("Country cannot be empty.");
        this.country = country;
    }

    public void setCity(String city) {
        if (city == null || city.trim().isEmpty())
            throw new IllegalArgumentException("City cannot be empty.");
        this.city = city;
    }

    public void setRankPoints(int rankPoints) {
        if (rankPoints < 0)
            throw new IllegalArgumentException("Rank points cannot be negative.");
        this.rankPoints = rankPoints;
    }
    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches);
    }
    public String getName() { return name; }
    public String getCountry() { return country; }
    public String getCity() { return city; }
    public int getRankPoints() { return rankPoints; }
    public List<Player> getPlayers() { return Collections.unmodifiableList(players); }
    public List<Coach> getCoaches() { return Collections.unmodifiableList(coaches); }

    /**
     * Add a player to this team.
     * Creates an active ContractPlayer(startDate = today, salary = 1000.0, description = "Auto-created contract")
     * and links it to player and team (history bag).
     */
    public void addPlayer(Player player) {
        if (player == null) {
            throw new NullObjectException("Player");
        }
        if (players.contains(player)) {
            // duplication
            throw new InvalidValueException("Player is already in this team.");
        }
        players.add(player);

        // create contract and attach to histories
        ContractPlayer contract = new ContractPlayer(player, this, LocalDate.now(), 1000.0, "Auto-created contract");
        // ContractPlayer constructor will call internal-add methods
    }

    /**
     * Remove player from team. Terminates (deactivates) the active contract if present.
     */
    public void removePlayer(Player player) {
        if (player == null) {
            throw new NullObjectException("Player");
        }
        if (!players.contains(player)) {
            // trying to remove non-associated player
            throw new InvalidValueException("Player is not a member of this team.");
        }

        // find active contract for this player-team pair and terminate it
        ContractPlayer active = null;
        for (ContractPlayer c : new ArrayList<>(playerContracts)) {
            if (c.getPlayer() == player && c.isActive()) {
                active = c;
                break;
            }
        }
        if (active != null) {
            // set endDate = now and deactivate
            active.terminate(LocalDate.now());
        }

        players.remove(player);
    }


    public void addCoach(Coach coach) {
        if (coach == null) {
            throw new NullObjectException("Coach");
        }
        if (coaches.contains(coach)) {
            // duplication
            throw new InvalidValueException("Coach is already assigned to this team.");
        }

        coaches.add(coach);
        coach.addTeamInternal(this);  // update reverse side

        // create contract and attach to histories
        ContractCoach contract = new ContractCoach(coach, this, LocalDate.now(), 1500.0, "Auto-created contract");
        // ContractCoach constructor will call internal-add methods
    }

    public void removeCoach(Coach coach) {
        if (coach == null) {
            throw new NullObjectException("Coach");
        }
        if (!coaches.contains(coach)) {
            throw new InvalidValueException("Coach is not assigned to this team.");
        }

        // find active coach contract and terminate
        ContractCoach active = null;
        for (ContractCoach c : new ArrayList<>(coachContracts)) {
            if (c.getCoach() == coach && c.isActive()) {
                active = c;
                break;
            }
        }
        if (active != null) {
            active.terminate(LocalDate.now());
        }

        coaches.remove(coach);
        coach.removeTeamInternal(this);
    }

    // Called by ContractPlayer to register itself in team's contract list
    void internalAddPlayerContract(ContractPlayer c) {
        if (c == null) throw new NullObjectException("ContractPlayer");
        if (!playerContracts.contains(c)) playerContracts.add(c);
    }

    // Called by ContractPlayer.delete or termination if needed
    void internalRemovePlayerContract(ContractPlayer c) {
        playerContracts.remove(c);
    }

    // Called by ContractCoach to register itself in team's coachContracts list
    void internalAddCoachContract(ContractCoach c) {
        if (c == null) throw new NullObjectException("ContractCoach");
        if (!coachContracts.contains(c)) coachContracts.add(c);
    }

    void internalRemoveCoachContract(ContractCoach c) {
        coachContracts.remove(c);
    }

    public List<ContractPlayer> getPlayerContracts() {
        return Collections.unmodifiableList(playerContracts);
    }

    public List<ContractCoach> getCoachContracts() {
        return Collections.unmodifiableList(coachContracts);
    }

    private static List<Team> extent = new ArrayList<>();

    public static List<Team> getExtent() {
        return new ArrayList<>(extent);
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(Team.class, extent);
    }

    public static void loadExtent() {
        List<Team> loaded = ExtentPersistence.loadExtent(Team.class);
        extent.clear();
        extent.addAll(loaded);
    }
}
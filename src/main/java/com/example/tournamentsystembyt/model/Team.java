package com.example.tournamentsystembyt.model;


import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

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
    public List<Player> getPlayers() { return players; }
    public List<Coach> getCoaches() { return coaches; }

    public void addPlayer(Player player) {
        if (player == null) {
            throw new NullObjectException("Player");
        }
        if (players.contains(player)) {
            // duplication
            throw new InvalidValueException("Player is already in this team.");
        }
        players.add(player);   // multiplicity on Team side is 0..*, so always ok
    }

    public void removePlayer(Player player) {
        if (player == null) {
            throw new NullObjectException("Player");
        }
        if (!players.contains(player)) {
            // trying to remove non-associated player
            throw new InvalidValueException("Player is not a member of this team.");
        }
        // min multiplicity on Team side is 0, so removal is allowed
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
    }

    public void removeCoach(Coach coach) {
        if (coach == null) {
            throw new NullObjectException("Coach");
        }
        if (!coaches.contains(coach)) {
            throw new InvalidValueException("Coach is not assigned to this team.");
        }

        // min multiplicity is 0, so we can always remove, but we protect from invalid removal
        coaches.remove(coach);
        coach.removeTeamInternal(this);
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
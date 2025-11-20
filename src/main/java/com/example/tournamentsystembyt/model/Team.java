package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private String country;
    private String city;
    private int rankPoints;

    private List<Player> players;
    private List<Coach> coaches;

    public Team(String name, String country, String city, int rankPoints) {
        setName(name);
        setCountry(country);
        setCity(city);
        setRankPoints(rankPoints);
        this.players = new ArrayList<>();
        this.coaches = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        if (player == null)
            throw new IllegalArgumentException("Player cannot be null.");
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void addCoach(Coach coach) {
        if (coach == null)
            throw new IllegalArgumentException("Coach cannot be null.");
        if (coaches.size() >= 2)
            throw new IllegalArgumentException("A team can have at most 2 coaches.");
        coaches.add(coach);
    }

    public void removeCoach(Coach coach) {
        coaches.remove(coach);
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

    public String getName() { return name; }
    public String getCountry() { return country; }
    public String getCity() { return city; }
    public int getRankPoints() { return rankPoints; }
    public List<Player> getPlayers() { return players; }
    public List<Coach> getCoaches() { return coaches; }

    private static List<Team> extent = new ArrayList<>();

    public static List<Team> getExtent() { return new ArrayList<>(extent); }
    public static void clearExtent() { extent.clear(); }
    public static boolean saveExtent() { return ExtentPersistence.saveExtent(Team.class, extent); }
    public static void loadExtent() { extent = ExtentPersistence.loadExtent(Team.class); }
}
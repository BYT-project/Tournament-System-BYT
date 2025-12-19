package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Referee extends Person {
    private int experience; // in years

    private static final List<Referee> extent = new ArrayList<>();
    private final List<Match> matches = new ArrayList<>();

    private Person person;


    private static void addReferee(Referee r) {
        if (r == null) {
            throw new IllegalArgumentException("Referee cannot be null");
        }
        extent.add(r);
    }

    public void delete() {
        if (person == null) {
            return;
        }

        Person owner = person;
        owner.removeReferee();
    }

    public Coach changeToCoach(Coach newRole) {
        if (newRole == null) throw new IllegalArgumentException("Coach role cannot be null");
        Person p = getPerson();
        if (p == null) throw new IllegalStateException("Referee is not attached to any Person");

        if (newRole.getPerson() != null && newRole.getPerson() != p)
            throw new IllegalStateException("Coach role already belongs to another Person");

        p.assignCoach(newRole);
        p.removeReferee();
        return newRole;
    }

    public Player changeToPlayer(Player newRole) {
        if (newRole == null) throw new IllegalArgumentException("Player role cannot be null");
        Person p = getPerson();
        if (p == null) throw new IllegalStateException("Referee is not attached to any Person");

        if (newRole.getPerson() != null && newRole.getPerson() != p)
            throw new IllegalStateException("Player role already belongs to another Person");

        p.assignPlayer(newRole);
        p.removeReferee();
        return newRole;
    }

    public Staff changeToStaff(Staff newRole) {
        if (newRole == null) throw new IllegalArgumentException("Staff role cannot be null");
        Person p = getPerson();
        if (p == null) throw new IllegalStateException("Referee is not attached to any Person");

        if (newRole.getPerson() != null && newRole.getPerson() != p)
            throw new IllegalStateException("Staff role already belongs to another Person");

        p.assignStaff(newRole);
        p.removeReferee();
        return newRole;
    }

    public Viewer changeToViewer(Viewer newRole) {
        if (newRole == null) throw new IllegalArgumentException("Viewer role cannot be null");
        Person p = getPerson();
        if (p == null) throw new IllegalStateException("Referee is not attached to any Person");

        if (newRole.getPerson() != null && newRole.getPerson() != p)
            throw new IllegalStateException("Viewer role already belongs to another Person");

        p.assignViewer(newRole);
        p.removeReferee();
        return newRole;
    }


    public static List<Referee> getExtent() {
        return new ArrayList<>(extent); // defensive copy for encapsulation
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(Referee.class, extent);
    }

    public static void loadExtent() {
        List<Referee> loaded = ExtentPersistence.loadExtent(Referee.class);
        extent.clear();
        extent.addAll(loaded);
    }

    public Referee(String firstName,
                   String lastName,
                   LocalDate dateOfBirth,
                   String email,
                   String phone,
                   int experience) {
        super(firstName, lastName, dateOfBirth, email, phone);
        setExperience(experience);
        addReferee(this);
    }
    public Referee(){
        super();
    }

    public void setExperience(int experience) {
        if (experience < 0) {
            throw new NegativeNumberException("Experience", experience);
        }
        if (experience > 80) {
            throw new InvalidValueException("Experience seems unrealistically high.");
        }
        this.experience = experience;
    }

    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches);
    }

    public void addMatch(Match match) {
        if (match == null) {
            throw new NullObjectException("Match");
        }
        if (!matches.contains(match)) {
            matches.add(match);
        }
        if (!match.getReferees().contains(this)) {
            match.addReferee(this);
        }
    }

    public void removeMatch(Match match) {
        if (match == null) {
            throw new NullObjectException("Match");
        }
        if (!matches.contains(match)) {
            throw new InvalidValueException("Referee is not assigned to this match");
        }
        matches.remove(match);

        if (match.getReferees().contains(this)) {
            match.removeReferee(this);
        }
    }

    public int getExperience() {
        return experience;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
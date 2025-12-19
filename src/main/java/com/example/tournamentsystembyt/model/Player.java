package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player extends Person {
    private double height;   // meters
    private double weight;   // kilograms
    private String position;
    private int shirtNum;

    private Person person;


    // NEW: keep all player's contracts (history)
    private final List<ContractPlayer> playerContracts = new ArrayList<>();

    private static final List<Player> extent = new ArrayList<>();

    private static void addPlayer(Player p) {
        if (p == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        extent.add(p);
    }

    public static List<Player> getExtent() {
        return new ArrayList<>(extent); // defensive copy for encapsulation
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(Player.class, extent);
    }

    public static void loadExtent() {
        List<Player> loaded = ExtentPersistence.loadExtent(Player.class);
        extent.clear();
        extent.addAll(loaded);
    }

    public Player(String firstName,
                  String lastName,
                  LocalDate dateOfBirth,
                  String email,
                  String phone,
                  double height,
                  double weight,
                  String position,
                  int shirtNum) {
        super(firstName, lastName, dateOfBirth, email, phone);
        setHeight(height);
        setWeight(weight);
        setPosition(position);
        setShirtNum(shirtNum);
        addPlayer(this);
    }
    public Player(){
        super();
    }

    public Coach changeToCoach(Coach newRole) {
        if (newRole == null) throw new IllegalArgumentException("Coach role cannot be null");
        Person p = getPerson();
        if (p == null) throw new IllegalStateException("Player is not attached to any Person");

        if (newRole.getPerson() != null && newRole.getPerson() != p)
            throw new IllegalStateException("Coach role already belongs to another Person");

        p.assignCoach(newRole);
        p.removePlayer();
        return newRole;
    }

    public Referee changeToReferee(Referee newRole) {
        if (newRole == null) throw new IllegalArgumentException("Referee role cannot be null");
        Person p = getPerson();
        if (p == null) throw new IllegalStateException("Player is not attached to any Person");

        if (newRole.getPerson() != null && newRole.getPerson() != p)
            throw new IllegalStateException("Referee role already belongs to another Person");

        p.assignReferee(newRole);
        p.removePlayer();
        return newRole;
    }

    public Staff changeToStaff(Staff newRole) {
        if (newRole == null) throw new IllegalArgumentException("Staff role cannot be null");
        Person p = getPerson();
        if (p == null) throw new IllegalStateException("Player is not attached to any Person");

        if (newRole.getPerson() != null && newRole.getPerson() != p)
            throw new IllegalStateException("Staff role already belongs to another Person");

        p.assignStaff(newRole);
        p.removePlayer();
        return newRole;
    }

    public Viewer changeToViewer(Viewer newRole) {
        if (newRole == null) throw new IllegalArgumentException("Viewer role cannot be null");
        Person p = getPerson();
        if (p == null) throw new IllegalStateException("Player is not attached to any Person");

        if (newRole.getPerson() != null && newRole.getPerson() != p)
            throw new IllegalStateException("Viewer role already belongs to another Person");

        p.assignViewer(newRole);
        p.removePlayer();
        return newRole;
    }



    public void setHeight(double height) {
        if (height <= 0) {
            throw new NegativeNumberException("Height", height);
        }
        if (height < 1.2 || height > 2.5) {
            throw new InvalidValueException("Height should be between 1.2m and 2.5m.");
        }
        this.height = height;
    }

    public void setWeight(double weight) {
        if (weight <= 0) {
            throw new NegativeNumberException("Weight", weight);
        }
        if (weight < 30 || weight > 200) {
            throw new InvalidValueException("Weight should be between 30kg and 200kg.");
        }
        this.weight = weight;
    }

    public void setPosition(String position) {
        if (position == null || position.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Position");
        }
        String trimmed = position.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException("Position must contain at least 2 characters.");
        }
        this.position = trimmed;
    }

    public void setShirtNum(int shirtNum) {
        if (shirtNum <= 0) {
            throw new NegativeNumberException("Shirt number", shirtNum);
        }
        if (shirtNum > 99) {
            throw new InvalidValueException("Shirt number must be between 1 and 99.");
        }
        this.shirtNum = shirtNum;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public String getPosition() {
        return position;
    }

    public int getShirtNum() {
        return shirtNum;
    }
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person newPerson) {
        this.person = newPerson;
    }
    public void delete() {
        if (person == null) {
            return;
        }

        Person owner = person;
        owner.removePlayer();
    }

    // Contract history helpers

    void internalAddPlayerContract(ContractPlayer c) {
        if (c == null) throw new NullObjectException("ContractPlayer");
        if (!playerContracts.contains(c)) playerContracts.add(c);
    }

    void internalRemovePlayerContract(ContractPlayer c) {
        playerContracts.remove(c);
    }

    public List<ContractPlayer> getPlayerContracts() {
        return Collections.unmodifiableList(playerContracts);
    }
}
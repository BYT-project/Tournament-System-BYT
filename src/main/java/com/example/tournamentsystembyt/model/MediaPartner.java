package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;
import com.example.tournamentsystembyt.exceptions.InvalidEmailException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.util.ArrayList;
import java.util.List;

public class MediaPartner {

    private String name;
    private String email;
    private String type;

    private Tournament tournament;

    public Tournament getTournament() {
        return tournament;
    }

    private static final List<MediaPartner> extent = new ArrayList<>();

    private static void addMediaPartner(MediaPartner mp) {
        if (mp == null) {
            throw new IllegalArgumentException("MediaPartner cannot be null");
        }
        extent.add(mp);
    }

    public static List<MediaPartner> getExtent() {
        return new ArrayList<>(extent); // encapsulated copy
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(MediaPartner.class, extent);
    }

    public static void loadExtent() {
        List<MediaPartner> loaded = ExtentPersistence.loadExtent(MediaPartner.class);
        extent.clear();
        extent.addAll(loaded);
    }

    public MediaPartner(String name, String email, String type) {
        setName(name);
        setEmail(email);
        setType(type);
        addMediaPartner(this);
    }
    public MediaPartner() {
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Media partner name");
        }
        String trimmed = name.trim();

        if (trimmed.length() < 2) {
            throw new InvalidValueException("Media partner name must contain at least 2 characters.");
        }

        if (!trimmed.matches("[A-Za-zÀ-ÖØ-öø-ÿ0-9\\-\\s]+")) {
            throw new InvalidValueException("Media partner name contains invalid characters.");
        }

        this.name = trimmed;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Email");
        }

        String trimmed = email.trim();

        // Standard email validation regex
        if (!trimmed.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new InvalidEmailException(trimmed);
        }

        this.email = trimmed;
    }

    public void setType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Media partner type");
        }

        String trimmed = type.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException("Media partner type must contain at least 2 characters.");
        }

        this.type = trimmed;
    }

    public void setTournament(Tournament newTournament) {
        if (this.tournament == newTournament) {
            return; // nothing to do
        }

        // detach from old tournament
        if (this.tournament != null) {
            Tournament old = this.tournament;
            this.tournament = null;   // avoid loops
            old.internalRemoveMediaPartner(this);
        }

        // attach to new tournament
        this.tournament = newTournament;
        if (newTournament != null) {
            newTournament.internalAddMediaPartner(this);
        }
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }
}
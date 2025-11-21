package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Viewer extends Person {
    private static final List<Viewer> extent = new ArrayList<>();

    private static void addViewer(Viewer v) {
        if (v == null) {
            throw new IllegalArgumentException("Viewer cannot be null");
        }
        extent.add(v);
    }

    public static List<Viewer> getExtent() {
        return new ArrayList<>(extent); // defensive copy
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(Viewer.class, extent);
    }

    public static void loadExtent() {
        List<Viewer> loaded = ExtentPersistence.loadExtent(Viewer.class);
        extent.clear();
        extent.addAll(loaded);
    }

    public Viewer(String firstName,
                  String lastName,
                  LocalDate dateOfBirth,
                  String email,
                  String phone) {
        super(firstName, lastName, dateOfBirth, email, phone);
        addViewer(this);
    }
    public Viewer() {
        super();
    }
}
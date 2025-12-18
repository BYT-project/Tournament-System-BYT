package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidDateException;
import com.example.tournamentsystembyt.exceptions.InvalidEmailException;
import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.exceptions.NullOrEmptyStringException;

import java.time.LocalDate;
import java.time.Period;

public  class Person {
    private static final int MAX_HUMAN_AGE_YEARS = 120;

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;

    private Player playerComponent;
    private Coach coachComponent;
    private Referee refereeComponent;
    private Staff staffComponent;
    private Viewer viewerComponent;


    public Person(String firstName,
                  String lastName,
                  LocalDate dateOfBirth,
                  String email,
                  String phone) {
        setFirstName(firstName);
        setLastName(lastName);
        setDateOfBirth(dateOfBirth);
        setEmail(email);
        setPhone(phone);
    }
    protected Person(){


    }

    // Validation, Person should have at least one role
    private void validateComplete() {
        if (playerComponent == null &&
                coachComponent == null &&
                refereeComponent == null &&
                staffComponent == null &&
                viewerComponent == null) {
            throw new InvalidValueException("Person must have at least one role");
        }
    }

    // Deleting Person object
    public void delete() {
        if (playerComponent != null) {
            playerComponent.setPerson(null);
            playerComponent = null;
        }
        if (coachComponent != null) {
            coachComponent.setPerson(null);
            coachComponent = null;
        }
        if (refereeComponent != null) {
            refereeComponent.setPerson(null);
            refereeComponent = null;
        }
        if (staffComponent != null) {
            staffComponent.setPerson(null);
            staffComponent = null;
        }
        if (viewerComponent != null) {
            viewerComponent.setPerson(null);
            viewerComponent = null;
        }
    }


    public void assignPlayer(Player player) {
        if (player == null) throw new NullObjectException("Player");
        if (this.playerComponent != null) throw new InvalidValueException("Player role already assigned.");
        if (player.getPerson() != null && player.getPerson() != this) {
            throw new InvalidValueException("Player role already belongs to another Person.");
        }
        this.playerComponent = player;
        if (player.getPerson() != this) {
            player.setPerson(this);
        }
    }

    public void removePlayer() {
        if (this.playerComponent == null) throw new InvalidValueException("Player role is not assigned.");
        Player old = this.playerComponent;
        this.playerComponent = null;
        if (old.getPerson() == this) {
            old.setPerson(null);
        }
        validateComplete();
    }
    public void assignCoach(Coach coach) {
        if (coach == null) throw new NullObjectException("Coach");
        if (this.coachComponent != null) throw new InvalidValueException("Coach role already assigned.");
        if (coach.getPerson() != null && coach.getPerson() != this) {
            throw new InvalidValueException("Coach role already belongs to another Person.");
        }
        this.coachComponent = coach;
        if (coach.getPerson() != this) {
            coach.setPerson(this);
        }
    }

    public void removeCoach() {
        if (this.coachComponent == null) throw new InvalidValueException("Coach role is not assigned.");
        Coach old = this.coachComponent;
        this.coachComponent = null;
        if (old.getPerson() == this) {
            old.setPerson(null);
        }
        validateComplete();
    }
    public void assignReferee(Referee referee) {
        if (referee == null) throw new NullObjectException("Referee");
        if (this.refereeComponent != null) throw new InvalidValueException("Referee role already assigned.");
        if (referee.getPerson() != null && referee.getPerson() != this) {
            throw new InvalidValueException("Referee role already belongs to another Person.");
        }
        this.refereeComponent = referee;
        if (referee.getPerson() != this) {
            referee.setPerson(this);
        }
    }

    public void removeReferee() {
        if (this.refereeComponent == null) throw new InvalidValueException("Referee role is not assigned.");
        Referee old = this.refereeComponent;
        this.refereeComponent = null;
        if (old.getPerson() == this) {
            old.setPerson(null);
        }
        validateComplete();
    }
    public void assignStaff(Staff staff) {
        if (staff == null) throw new NullObjectException("Staff");
        if (this.staffComponent != null) throw new InvalidValueException("Staff role already assigned.");
        if (staff.getPerson() != null && staff.getPerson() != this) {
            throw new InvalidValueException("Staff role already belongs to another Person.");
        }
        this.staffComponent = staff;
        if (staff.getPerson() != this) {
            staff.setPerson(this);
        }
    }

    public void removeStaff() {
        if (this.staffComponent == null) throw new InvalidValueException("Staff role is not assigned.");
        Staff old = this.staffComponent;
        this.staffComponent = null;
        if (old.getPerson() == this) {
            old.setPerson(null);
        }
        validateComplete();
    }
    public void assignViewer(Viewer viewer) {
        if (viewer == null) throw new NullObjectException("Viewer");
        if (this.viewerComponent != null) throw new InvalidValueException("Viewer role already assigned.");
        if (viewer.getPerson() != null && viewer.getPerson() != this) {
            throw new InvalidValueException("Viewer role already belongs to another Person.");
        }
        this.viewerComponent = viewer;
        if (viewer.getPerson() != this) {
            viewer.setPerson(this);
        }
    }

    public void removeViewer() {
        if (this.viewerComponent == null) throw new InvalidValueException("Viewer role is not assigned.");
        Viewer old = this.viewerComponent;
        this.viewerComponent = null;
        if (old.getPerson() == this) {
            old.setPerson(null);
        }
        validateComplete();
    }



    // Derived attribute
    public int getAge() {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new NullOrEmptyStringException("First name");
        }
        String trimmed = firstName.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException("First name must contain at least 2 characters.");
        }
        if (!trimmed.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\-\\s]+")) {
            throw new InvalidValueException("First name contains invalid characters.");
        }
        this.firstName = trimmed;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Last name");
        }
        String trimmed = lastName.trim();
        if (trimmed.length() < 2) {
            throw new InvalidValueException("Last name must contain at least 2 characters.");
        }
        if (!trimmed.matches("[A-Za-zÀ-ÖØ-öø-ÿ\\-\\s]+")) {
            throw new InvalidValueException("Last name contains invalid characters.");
        }
        this.lastName = trimmed;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new NullObjectException("Date of birth");
        }

        LocalDate today = LocalDate.now();
        if (dateOfBirth.isAfter(today)) {
            throw new InvalidDateException("Date of birth cannot be in the future.");
        }

        if (dateOfBirth.isBefore(today.minusYears(MAX_HUMAN_AGE_YEARS))) {
            throw new InvalidDateException("Date of birth is unrealistically old.");
        }

        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Email");
        }
        String trimmed = email.trim();
        if (!trimmed.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new InvalidEmailException(trimmed);
        }
        this.email = trimmed;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new NullOrEmptyStringException("Phone number");
        }
        String trimmed = phone.trim();
        if (!trimmed.matches("^[+0-9][0-9\\-\\s]{5,}$")) {
            throw new InvalidValueException("Phone number has an invalid format.");
        }
        this.phone = trimmed;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
    public Player getPlayerComponent() { return playerComponent; }
    public Coach getCoachComponent() { return coachComponent; }
    public Referee getRefereeComponent() { return refereeComponent; }
    public Staff getStaffComponent() { return staffComponent; }
    public Viewer getViewerComponent() { return viewerComponent; }

}
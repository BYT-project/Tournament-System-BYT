package com.example.tournamentsystembyt.model;

import com.example.tournamentsystembyt.exceptions.InvalidValueException;
import com.example.tournamentsystembyt.exceptions.NegativeNumberException;
import com.example.tournamentsystembyt.exceptions.NullObjectException;
import com.example.tournamentsystembyt.helpers.ExtentPersistence;

import java.util.ArrayList;
import java.util.List;

public class TournamentTicket extends Ticket {

    private Stadium stadium;
    private Integer seatNumber; // 0..1 (optional)
    private Tournament tournament;
    private Match match;

    private static final List<TournamentTicket> extent = new ArrayList<>();

    private static void addTournamentTicket(TournamentTicket t) {
        if (t == null) throw new IllegalArgumentException("TournamentTicket cannot be null");
        extent.add(t);
    }

    public static List<Ticket> getExtent() {
        return new ArrayList<>(extent); // defensive copy
    }

    public static void clearExtent() {
        extent.clear();
    }

    public static boolean saveExtent() {
        return ExtentPersistence.saveExtent(TournamentTicket.class, extent);
    }

    public static void loadExtent() {
        List<TournamentTicket> loaded = ExtentPersistence.loadExtent(TournamentTicket.class);
        extent.clear();
        extent.addAll(loaded);
    }

    public TournamentTicket(String id,
                            double price,
                            String status,
                            Tournament tournament,
                            Stadium stadium,
                            Integer seatNumber) {

        super(id, price, "TOURNAMENT", status);

        setTournament(tournament);
        setStadium(stadium);
        setSeatNumber(seatNumber); // may be null
        addTournamentTicket(this);
    }

    public TournamentTicket() {
        super();
    }

    // NEW
    public Tournament getTournament() {
        return tournament;
    }

    // NEW – composition: cannot be null, cannot change owner
    public void setTournament(Tournament tournament) {
        if (tournament == null) {
            throw new NullObjectException("Tournament");
        }
        if (this.tournament == tournament) {
            return;
        }
        if (this.tournament != null && this.tournament != tournament) {
            throw new InvalidValueException("TournamentTicket cannot change its tournament once set.");
        }
        this.tournament = tournament;
        tournament.internalAddTournamentTicket(this);
    }

    // NEW – used from Tournament.delete()
//    public void delete() {
//        if (tournament != null) {
//            Tournament oldTournament = tournament;
//            tournament = null;
//            oldTournament.internalRemoveTournamentTicket(this);
//        }
//        // If TicketOrder has this ticket, remove it
//        for (TicketOrder order : TicketOrder.getExtent()) {
//            order.internalRemoveTicket(this);
//        }
//        extent.remove(this);
//    }

    private void validateSeatNumber(Integer seatNumber) {
        if (seatNumber == null) return; // optional
        if (seatNumber <= 0) {
            throw new NegativeNumberException("Seat number", seatNumber);
        }
        if (stadium != null && seatNumber > stadium.getCapacity()) {
            throw new InvalidValueException(
                    "Seat number must be between 1 and " + stadium.getCapacity() + "."
            );
        }
    }

    public Match getMatch() {
        return match;
    }

    // *** NEW: setMatch handles reverse connection and validation ***
    public void setMatch(Match match) {

        // If same match → do nothing
        if (this.match == match) return;

        // ticket cannot move to another match once assigned
        if (this.match != null && match != null && this.match != match) {
            throw new InvalidValueException("Ticket is already assigned to a different match.");
        }

        // If detaching match
        if (match == null) {
            Match old = this.match;
            this.match = null;
            if (old != null) {
                old.internalRemoveTournamentTicket(this);
            }
            return;
        }

        // Attach to new match (initial assignment only)
        this.match = match;
        match.internalAddTournamentTicket(this);

        // Re-validate seat number now that a match exists
        validateSeatNumber(this.seatNumber);
    }

    public Stadium getStadium() {
        return stadium;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setStadium(Stadium stadium) {
        if (stadium == null) {
            throw new NullObjectException("Stadium");
        }
        this.stadium = stadium;

        // If seat is already chosen, re-validate it with the new stadium
        validateSeatNumber(this.seatNumber);
    }

    public void setSeatNumber(Integer seatNumber) {

        boolean isConstructorInitialization = this.match == null && this.seatNumber == null;

        // seatNumber may be set only after match assignment,
        // EXCEPT when called during object construction
        if (seatNumber != null && this.match == null && !isConstructorInitialization) {
            throw new InvalidValueException("Cannot assign seat number before ticket is linked to a match.");
        }

        this.seatNumber = seatNumber;
        validateSeatNumber(this.seatNumber);
    }

    public void delete() {

        // detach from match
        if (match != null) {
            Match oldMatch = match;
            match = null;
            oldMatch.internalRemoveTournamentTicket(this);
        }

        // detach from tournament
        if (tournament != null) {
            Tournament old = tournament;
            tournament = null;
            old.internalRemoveTournamentTicket(this);
        }

        // remove from ticket orders if present
        for (TicketOrder order : TicketOrder.getExtent()) {
            order.internalRemoveTicket(this);
        }

        extent.remove(this);
    }
}
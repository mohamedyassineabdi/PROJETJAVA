package com.mycompany.cinema.models;

import java.sql.Timestamp;

public class Reservation {
    private int idReservation;
    private int idClient;
    private int idSeance;
    private String statut;
    private int nbPlaces; // 🔴 AJOUT OBLIGATOIRE
    private Timestamp dateReservation;

    public Reservation(int idReservation, int idClient, int idSeance, int nbPlaces, String statut, Timestamp dateReservation) {
    this.idReservation = idReservation;
    this.idClient = idClient;
    this.idSeance = idSeance;
    this.nbPlaces = nbPlaces;
    this.statut = statut;
    this.dateReservation = dateReservation;
}
   public Reservation(int idReservation, int idClient, int idSeance, String statut, Timestamp dateReservation) {
    this.idReservation = idReservation;
    this.idClient = idClient;
    this.idSeance = idSeance;
    this.statut = statut;
    this.dateReservation = dateReservation;
}


    // Getters
    public int getIdReservation() {
        return idReservation;
    }

    public int getIdClient() {
        return idClient;
    }

    public int getIdSeance() {
        return idSeance;
    }

    public String getStatut() {
        return statut;
    }

    public Timestamp getDateReservation() {
        return dateReservation;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    // Setters
    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public void setIdSeance(int idSeance) {
        this.idSeance = idSeance;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setDateReservation(Timestamp dateReservation) {
        this.dateReservation = dateReservation;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }
}

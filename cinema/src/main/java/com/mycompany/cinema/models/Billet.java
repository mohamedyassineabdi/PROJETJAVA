package com.mycompany.cinema.models;

public class Billet {
    private int idBillet;
    private String numeroBillet;
    private int idReservation;

    public Billet(int idBillet, String numeroBillet, int idReservation) {
        this.idBillet = idBillet;
        this.numeroBillet = numeroBillet;
        this.idReservation = idReservation;
    }

    // Getters et Setters
    public int getIdBillet() { return idBillet; }
    public void setIdBillet(int idBillet) { this.idBillet = idBillet; }

    public String getNumeroBillet() { return numeroBillet; }
    public void setNumeroBillet(String numeroBillet) { this.numeroBillet = numeroBillet; }

    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }
}


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema.models;

public class Paiement {
    private int idPaiement;
    private double montant;
    private String statut;
    private int idReservation;

    public Paiement(int idPaiement, double montant, String statut, int idReservation) {
        this.idPaiement = idPaiement;
        this.montant = montant;
        this.statut = statut;
        this.idReservation = idReservation;
    }

    // Getters
    public int getIdPaiement() { return idPaiement; }
    public double getMontant() { return montant; }
    public String getStatut() { return statut; }
    public int getIdReservation() { return idReservation; }

    // Setters
    public void setIdPaiement(int idPaiement) { this.idPaiement = idPaiement; }
    public void setMontant(double montant) { this.montant = montant; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }
}


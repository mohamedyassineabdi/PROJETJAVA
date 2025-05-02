/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema.models;

public class Salle {
    private int idSalle;
    private String nom;
    private int capacite;

    public Salle(int idSalle, String nom, int capacite) {
        this.idSalle = idSalle;
        this.nom = nom;
        this.capacite = capacite;
    }

    // Getters
    public int getIdSalle() {
        return idSalle;
    }

    public String getNom() {
        return nom;
    }

    public int getCapacite() {
        return capacite;
    }

    // Setters
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    @Override
    public String toString() {
        return nom; // utile pour affichage dans les JComboBox
    }
}

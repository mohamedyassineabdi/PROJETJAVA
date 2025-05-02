/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema.models;

/**
 *
 * @author yassi
 */


public class Client extends Utilisateur {
    private int id_client; // sera attribué automatiquement par MySQL

    public Client(String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse);
        // on ne demande pas id_client ici
    }

    // Getters et setters
    public int getIdClient() {
        return id_client;
    }

    public void setIdClient(int id) {
        this.id_client = id;
    }
     // Fonctionnalités futures (à coder plus tard)
    /*
    public Reservation reserverSeance(Seance seance, int nbPlaces) {
        // Réserver une séance
    }

    public List<Reservation> consulterReservations() {
        // Consulter les réservations du client
    }
    */



}


   
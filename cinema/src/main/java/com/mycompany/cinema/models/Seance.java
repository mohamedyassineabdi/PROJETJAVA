/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema.models;

public class Seance {
       private int idSeance;
    private String dateSeance;
    private String heure;
    private String langue;
    private int idFilm;
    private int idSalle;
    private int placesDisponibles;
    private String nomSalle; // ✅ nouveau champ

    public Seance(int idSeance, String dateSeance, String heure, String langue, int idFilm, int idSalle, int placesDisponibles, String nomSalle) {
        this.idSeance = idSeance;
        this.dateSeance = dateSeance;
        this.heure = heure;
        this.langue = langue;
        this.idFilm = idFilm;
        this.idSalle = idSalle;
        this.placesDisponibles = placesDisponibles;
        this.nomSalle = nomSalle;
    }

    public String getNomSalle() {
        return nomSalle;
    }

    // Getters
    public int getIdSeance() { return idSeance; }
    public String getDateSeance() { return dateSeance; }
    public String getHeure() { return heure; }
    public String getLangue() { return langue; }
    public int getIdFilm() { return idFilm; }
    public int getIdSalle() { return idSalle; }
    public int getPlacesDisponibles() { return placesDisponibles; }

    // Setters
    public void setPlacesDisponibles(int placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }

    @Override
    public String toString() {
        return dateSeance + " - " + heure + " (" + langue + ")";
    }
}

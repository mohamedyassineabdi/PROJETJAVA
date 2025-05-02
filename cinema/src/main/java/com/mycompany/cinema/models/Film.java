/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema.models;

public class Film {

    private int idFilm;
    private String titre;
    private String genre;
    private int duree;
    private String resume;

    // Constructeur
    public Film(int idFilm, String titre, String genre, int duree, String resume) {
        this.idFilm = idFilm;
        this.titre = titre;
        this.genre = genre;
        this.duree = duree;
        this.resume = resume;
    }

    // Surcharge : constructeur vide (pratique pour DAO)
    public Film() {
    }

    // Getters
    public int getIdFilm() {
        return idFilm;
    }

    public String getTitre() {
        return titre;
    }

    public String getGenre() {
        return genre;
    }

    public int getDuree() {
        return duree;
    }

    public String getResume() {
        return resume;
    }

    // Setters
    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    // Méthode de validation
    public boolean estValide() {
        return (titre != null && !titre.isEmpty()) &&
               (genre != null && !genre.isEmpty()) &&
               (duree > 0);
    }

    @Override
    public String toString() {
        return "Film{" +
               "idFilm=" + idFilm +
               ", titre='" + titre + '\'' +
               ", genre='" + genre + '\'' +
               ", duree=" + duree +
               ", resume='" + resume + '\'' +
                '}';
    }
}


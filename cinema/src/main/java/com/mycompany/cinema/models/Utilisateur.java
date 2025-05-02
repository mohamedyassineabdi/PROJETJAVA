package com.mycompany.cinema.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author yassi
 */
public abstract class Utilisateur {
    protected String nom;
    protected String prenom;
    protected String email;
    protected String motDePasse; // stocké en version HASHÉE

    public Utilisateur(String nom, String prenom, String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = hasherMotDePasse(motDePasse); // Hasher directement à la création
    }

    // Getters
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; } // Attention : retourne déjà hashé

    // Setters
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }

    public void setMotDePasse(String motDePasse) { 
        this.motDePasse = hasherMotDePasse(motDePasse); // Hash lors du changement
    }

    // Authentification sécurisée
    public boolean authentifier(String mdpSaisi) {
        String hashedInput = hasherMotDePasse(mdpSaisi);
        return this.motDePasse.equals(hashedInput);
    }

    public boolean estValide() {
        return (nom != null && !nom.isEmpty()) &&
               (prenom != null && !prenom.isEmpty()) &&
               (email != null && !email.isEmpty()) &&
               (motDePasse != null && motDePasse.length() >= 6);
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    // ====== Hashing ======
    private String hasherMotDePasse(String motDePasse) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(motDePasse.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur de hachage du mot de passe", e);
        }
    }
}

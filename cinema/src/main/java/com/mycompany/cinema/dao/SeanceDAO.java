/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cinema.dao;

import com.mycompany.cinema.database.Database;
import com.mycompany.cinema.models.Seance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeanceDAO {

    private Connection getConnection() throws SQLException {
        return Database.getConnection();
    }

    public List<Seance> getSeancesParFilm(String titreFilm) {
        List<Seance> seances = new ArrayList<>();
        String sql = """
            SELECT s.id_seance, s.date_seance, s.heure, s.langue, s.id_film, s.id_salle, s.places_disponibles
            FROM seances s
            JOIN film f ON s.id_film = f.id_film
            WHERE f.titre = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, titreFilm);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
seances.add(new Seance(
    rs.getInt("id_seance"),
    rs.getString("date_seance"),
    rs.getString("heure"),
    rs.getString("langue"),
    rs.getInt("id_film"),
    rs.getInt("id_salle"),
    rs.getInt("places_disponibles"),
    rs.getString("nom_salle") // ici le nom de salle
));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seances;
    }

    public List<Seance> getSeancesParFilmEtFiltres(String titreFilm, String date, String salle) {
        List<Seance> seances = new ArrayList<>();
        String sql = """
    SELECT s.id_seance, s.date_seance, s.heure, s.langue, s.id_film, s.id_salle,
           s.places_disponibles, sa.nom AS nom_salle
    FROM seances s
    JOIN films f ON s.id_film = f.id_film
    JOIN salles sa ON s.id_salle = sa.id_salle
    WHERE f.titre = ?
    AND (? = '' OR s.date_seance = ?)
    AND (? = '' OR sa.nom = ?)
""";


        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, titreFilm);
            stmt.setString(2, date);
            stmt.setString(3, date);
            stmt.setString(4, salle);
            stmt.setString(5, salle);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
               seances.add(new Seance(
    rs.getInt("id_seance"),
    rs.getString("date_seance"),
    rs.getString("heure"),
    rs.getString("langue"),
    rs.getInt("id_film"),
    rs.getInt("id_salle"),
    rs.getInt("places_disponibles"),
    rs.getString("nom_salle") // ici le nom de salle
));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seances;
    }

    public void decrementerPlaces(int idSeance, int nbPlaces) {
        String sql = "UPDATE seances SET places_disponibles = places_disponibles - ? WHERE id_seance = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, nbPlaces);
            stmt.setInt(2, idSeance);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Seance getSeanceById(int idSeance) {
         Seance seance = null;
    String sql = """
        SELECT s.id_seance, s.date_seance, s.heure, s.langue, s.id_film, s.id_salle,
               s.places_disponibles, sa.nom AS nom_salle
        FROM seances s
        JOIN salles sa ON s.id_salle = sa.id_salle
        WHERE s.id_seance = ?
    """;

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, idSeance);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            seance = new Seance(
                rs.getInt("id_seance"),
                rs.getString("date_seance"),
                rs.getString("heure"),
                rs.getString("langue"),
                rs.getInt("id_film"),
                rs.getInt("id_salle"),
                rs.getInt("places_disponibles"),
                rs.getString("nom_salle")
            );
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return seance;
    }
    
    public void incrementerPlaces(int idSeance, int nbPlaces) {
    String sql = "UPDATE seances SET places_disponibles = places_disponibles + ? WHERE id_seance = ?";

    try (Connection conn = Database.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, nbPlaces);
        stmt.setInt(2, idSeance);
        stmt.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}

